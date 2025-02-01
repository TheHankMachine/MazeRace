package org.MazeRace.game;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import org.MazeRace.control.Controller;

import java.util.Random;

public class Circuit {

    private final Group group = new Group();
    private final Text infoText = new Text();
    private final Text authorText = new Text();

    private final Controller controller;
    private final Random random;
    private final Maze maze;
    private final Ship ship;

    private final int timeLimit;

    private Target target;

    private boolean finished = false;
    private boolean dnf = false;
    private int score = 0;
    private int frame = 0;

    public Circuit(Class<? extends Controller> controller, int scale, int mazeWidth, int mazeHeight, Long seed) {
        //This constructor is really messy, but I want a lot of these things to be final
        try {
            this.controller = controller.getConstructor(Circuit.class).newInstance(this);
        }catch(Exception e){
            throw new RuntimeException("Controller constructor does not take in the proper arguments");
        }

        random = new Random(seed);
        maze = new Maze(mazeWidth, mazeHeight, this);
        ship = new Ship(0, 0, this, this.controller, maze.startRight());

        ship.updatePosition();
        maze.initDisplay();

        changeTarget();
        initText();

        group.getTransforms().add(new Scale(scale, scale));
        Display.addNode(group);

        // arbitrary time limit (roughly a second for each cell)
        timeLimit = maze.getWidth() * maze.getHeight() * 50;
    }

    /**
     * replacement for Math.random(), seeded for each circuit instance
     * @return random double [0, 1)
     */
    public double random(){
        return random.nextDouble();
    }

    /**
     * @return the x position of the target
     */
    public double getTargetX(){
        return target.x;
    }

    /**
     * @return the y position of the target
     */
    public double getTargetY(){
        return target.y;
    }

    /**
     * Allows you to add nodes to the scene.
     */
    public void addNode(Node... node){
        group.getChildren().addAll(node);
    }

    public void removeNode(Node... node){
        group.getChildren().removeAll(node);
    }

    void update(){
        if(hasEnded()) return;

        ship.update();
        target.checkFinish();

        if(frame++ > timeLimit){
            infoText.setText("\nDNF\n(time limit)");
            dnf = true;
        }
    }

    void reset(boolean moveTarget){
        dnf = false;
        finished = false;
        frame = 0;

        ship.reset();
        controller.onReset();

        infoText.setText("");

        if(moveTarget) changeTarget();
    }

    void crash() {
        infoText.setText("\nDNF\n(crash)");
        dnf = true;
    }

    void setPosition(double x, double y){
        group.setTranslateX(x);
        group.setTranslateY(y);
    }

    void awardPoint(int round){
        //TOOD: change this; this sucks
        authorText.setText(authorText.getText() + "*");
        score++;
    }

    double getRaycast(Ship ship){
        return maze.raycast(ship.getX(), ship.getY(), ship.getAngle());
    }

    int getScore(){
        return score;
    }

    int getTime(){
        return frame;
    }

    boolean validMove(double x1, double y1, double x2, double y2){
        if(x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) return false;
        return maze.validMove((int) x1, (int) y1, (int) x2, (int) y2);
    }

    boolean isDNF(){
        return dnf;
    }

    boolean hasEnded(){
        return finished || dnf;
    }

    Controller getController(){
        return controller;
    }

    private void initText(){
        addNode(infoText, authorText);

        Font font = Font.font("Consolas", 0.06 * maze.getWidth());

        infoText.setX(maze.getWidth() / 2.0);

        infoText.setY(-0.2 * maze.getWidth());
        authorText.setY(-0.2 * maze.getWidth());

        infoText.setFont(font);
        authorText.setFont(font);

        infoText.setFill(controller.getColor());
        authorText.setFill(controller.getColor());

        authorText.setText(controller.getName() + '\n' + controller.getAuthor() + "\t");
    }

    void changeTarget(){
        Maze.Cell targetCell = maze.getNextViableEnding();
        if(targetCell == null) return;

        if(target == null){
            target = new Target(targetCell.x + 0.5, targetCell.y + 0.5);
        }else{
            target.move(targetCell.x + 0.5, targetCell.y + 0.5);
        }

    }

    private class Target{

        private final Circle circle;

        private double x, y;

        private Target(double x, double y){
            this.x = x;
            this.y = y;

            circle = new Circle(x, y, Ship.radius * 2);
            circle.setFill(Color.color(0, 0, 0, 0));
            circle.setStroke(controller.getColor());
            circle.setStrokeWidth(0.5 / 18.0);
            addNode(circle);
        }

        private void move(double x, double y){
            this.x = x;
            this.y = y;
            circle.setCenterX(x);
            circle.setCenterY(y);
        }

        private void checkFinish(){
            if(Math.hypot(x - ship.getX(), y - ship.getY()) < Ship.radius * 2){
                finished = true;
                infoText.setText("\nFinished in \n" + frame + " frames");
            }
        }
    }
}