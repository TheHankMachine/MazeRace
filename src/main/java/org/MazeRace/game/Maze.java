package org.MazeRace.game;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Maze {

    // trust me, this is optimal
    // trust
    private static final int[][] searchOrderPermutations = {
            {0,1,2,3}, {1,0,2,3}, {2,0,1,3}, {0,2,1,3}, {1,2,0,3}, {2,1,0,3},
            {2,1,3,0}, {1,2,3,0}, {3,2,1,0}, {2,3,1,0}, {1,3,2,0}, {3,1,2,0},
            {3,0,2,1}, {0,3,2,1}, {2,3,0,1}, {3,2,0,1}, {0,2,3,1}, {2,0,3,1},
            {1,0,3,2}, {0,1,3,2}, {3,1,0,2}, {1,3,0,2}, {0,3,1,2}, {3,0,1,2}
    };
    // trust
    private static final int[][] directionIndex = {
            {1,0}, {0,-1}, {-1,0}, {0,1},
    };

    private final List<Cell> viableEndings = new ArrayList<>();
    private final Stack<Cell> stack = new Stack<>();
    private final Group group = new Group();

    private final Cell[][] cells;
    private final Circuit circuit;
    private final int width, height;

    Maze(int width, int height, Circuit circuit) {
        this.width = width;
        this.height = height;
        this.circuit = circuit;

        cells = new Cell[height][width];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                cells[y][x] = new Cell(x, y);
            }
        }

        generate();
    }

    /**
     * stack approach adapted from wikipedia
     * https://en.wikipedia.org/wiki/Maze_generation_algorithm
     */
    private void generate(){
        stack.push(cells[0][0]);
        while(!stack.isEmpty()) {
            dfsStep();
        }
    }

    private void dfsStep(){
        Cell current = stack.pop();

        boolean firstTime = !current.observed;
        int permutation = (int) (circuit.random() * 24);
        int deadends = 0;

        current.observed = true;

        for(int search : searchOrderPermutations[permutation]){
            int[] dir = directionIndex[search];
            Cell neighbor = getCell(current.x + dir[0], current.y + dir[1]);

            if(neighbor == null || neighbor.observed){
                if(firstTime && ++deadends == 4) viableEndings.add(current);
                continue;
            }

            current.state |= 1 << search;
            neighbor.state |= 1 << (search + 2) % 4;

            stack.push(current);
            stack.push(neighbor);

            return;
        }
    }

    int getWidth(){
        return width;
    }

    int getHeight(){
        return height;
    }

    boolean startRight(){
        return cells[0][0].state % 2 == 1;
    }

    Cell getNextViableEnding(){
        int index = (int) (circuit.random() * viableEndings.size());
        if(index >= viableEndings.size()) return null;
        return viableEndings.remove(index);
    }

    /*
     * More efficient 2d raycasting adapted from the clever bloke at:
     * https://theshoemaker.de/posts/ray-casting-in-2d-grids
     */
    double raycast(double x, double y, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        int signX = (int) Math.signum(cos);
        int signY = (int) Math.signum(sin);

        int floorXOffset = cos > 0? 1 : 0;
        int floorYOffset = sin > 0? 1 : 0;

        int floorX = (int) x;
        int floorY = (int) y;

        double mx = x;
        double my = y;

        double t = 0;

        while(floorX >= 0 && floorY >= 0 && floorX < width && floorY < height){
            double dtX = cos == 0? Double.MAX_VALUE : (floorX + floorXOffset - mx) / cos;
            double dtY = sin == 0? Double.MAX_VALUE : (floorY + floorYOffset - my) / sin;

            if(dtX < dtY){
                t += dtX;
                if((cells[floorY][floorX].state & (1 << (cos > 0? 0 : 2))) == 0) break;
                floorX += signX;
            }else{
                t += dtY;
                if((cells[floorY][floorX].state & (1 << (sin > 0? 3 : 1))) == 0) break;
                floorY += signY;
            }

            mx = x + cos * t;
            my = y + sin * t;
        }

        return t;
    }

    boolean validMove(int x1, int y1, int x2, int y2){
        double checkerboardDistance = Math.abs(x1 - x2) + Math.abs(y1 - y2);

        if(checkerboardDistance == 0) return true;

        //you movin' to fast                (or you tried to take a corner)
        if(checkerboardDistance > 1) return false;

        if(x1 != x2){
            return (cells[y1][Math.min(x1, x2)].state & 0b0001) != 0;
        }
        return (cells[Math.min(y1, y2)][x1].state & 0b1000) != 0;
    }

    private Cell getCell(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }
        return cells[y][x];
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        for(Cell[] row : cells){
            for(Cell cell : row){
                result.append(cell.toString());
            }
            result.append("\n");
        }
        return result.toString().trim();
    }

    void initDisplay() {
        // purely cosmetic (wall spacing)/(wall thickness)
        final double s = 18;

        Scale scale = new Scale(1 / s, 1/s);
        group.getTransforms().add(scale);
        circuit.addNode(group);

        ObservableList<Node> c = group.getChildren();

        c.add(new Line(0, 0, 0, height * s));
        c.add(new Line(0, 0, width * s, 0));

        c.add(new Line(width * s, 0, width * s, height * s));
        c.add(new Line(0, height * s, width * s, height * s));

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(x < width - 1 && cells[y][x].state % 2 == 0){
                    c.add(new Line(x * s + s,y * s,x * s + s,y * s + s));
                }
                if(y < height - 1 && cells[y][x].state < 8){
                    c.add(new Line(x * s,y * s + s,x * s + s,y * s + s));
                }
            }
        }

        c.forEach(e -> ((Line) e).setStroke(Display.Palette.WALL.paint));
    }

    static class Cell {

        final int x, y;

        private boolean observed = false;
        private int state = 0;

        private Cell(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            char[] s = "x╺╹┗╸━┛┻╻┏┃┣┓┳┫╋".toCharArray();

            if(state == -1) {
                return "x";
            }

            return Character.toString(s[state]);
        }
    }
}