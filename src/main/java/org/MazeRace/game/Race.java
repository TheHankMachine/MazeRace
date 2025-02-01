package org.MazeRace.game;

import javafx.event.Event;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.MazeRace.control.Controller;

import java.util.Arrays;

public class Race {
    public static final int winningScore = 2;
    public static final int maxRounds = 5;

    private static final int circuitDisplayWidth = 200;
    private static final int split = 100;
    private static final int header = 50;

    private enum State{
        RACE,
        PRE_RESET,
        RESET,
        END,
        FINISHED
    }

    private final Text text = new Text();
    private final Circuit[] circuits;

    private final boolean displayed;

    private Circuit winner;
    private State state = State.RACE;
    private int pause = 50;
    private int round = 0;

    /**
     * @param mazeWidth the width for the maze
     * @param mazeHeight the height for the maze
     * @param seed the seed for all circuits. use null for a random seed
     * @param displayed whether the race is displayed using javafx, or just
     *                  printed to the terminal (this will be primarily used by me)
     * @param controller the controller(s) that are racing
     */
    @SafeVarargs
    public Race(int mazeWidth, int mazeHeight, Long seed, boolean displayed, Class<? extends Controller> ...controller) {
        this.displayed = displayed;

        if(seed == null) seed = (long) (Math.random() * Long.MAX_VALUE);

        circuits = new Circuit[controller.length];
        for(int i = 0; i < circuits.length; i++){
            circuits[i] = new Circuit(controller[i], circuitDisplayWidth / mazeWidth, mazeWidth, mazeHeight, seed);
            circuits[i].setPosition(
                i * circuitDisplayWidth + (i >= circuits.length / 2? split : 0),
                header
            );
        }

        Font font = Font.font("Consolas", 0.125 * split);
        text.setFont(font);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.BLACK);

        Display.addNode(text);

        if(displayed){
            Display.start(
                circuitDisplayWidth * circuits.length + split,
                header + circuitDisplayWidth * mazeHeight / mazeWidth,
                this::periodic
            );
        }else{
            if(circuits.length == 1) throw new RuntimeException("no thanks"); //prevents infinite loop

            while(!isFinished()) periodic(null);
            System.out.println(this);
        }
    }

    boolean isFinished(){
        return state == State.FINISHED;
    }

    void periodic(Event event){
        if(displayed && --pause > 0) return;

        switch(state){
            case RACE -> {
                for (Circuit circuit : circuits) circuit.update();
                if(Arrays.stream(circuits).allMatch(Circuit::hasEnded)) state = State.PRE_RESET;

                setText("go!");
            }
            case PRE_RESET -> {
                int bestTime = Integer.MAX_VALUE;
                Circuit winningCircuit = null;
                for (Circuit circuit : circuits) {
                    int time = circuit.isDNF()? Integer.MAX_VALUE : circuit.getTime();
                    if(time < bestTime) {
                        winningCircuit = circuit;
                        bestTime = time;
                    }else if(time == bestTime) winningCircuit = null; //tie
                }

                if(winningCircuit != null) winningCircuit.awardPoint(round);

                pause = 50;
                state = State.RESET;

                setText("");
            }
            case RESET -> {
                if(
                    circuits.length > 1 &&
                    (++round >= maxRounds || Arrays.stream(circuits).anyMatch(e -> e.getScore() >= winningScore))
                ){
                    state = State.END;
                    break;
                }

                for (Circuit circuit : circuits) circuit.reset(round > 1);

                state = State.RACE;
                pause = 50;

                setText("ready?");
            }
            case END -> {
                int bestScore = Integer.MIN_VALUE;
                Circuit winningCircuit = null;
                for (Circuit circuit : circuits) {
                    if(circuit.getScore() > bestScore) {
                        winningCircuit = circuit;
                        bestScore = circuit.getScore();
                    }else if(circuit.getScore() == bestScore) winningCircuit = null; //tie
                }

                winner = winningCircuit;

                setText(this.toString());

                state = State.FINISHED;

                if(displayed) Display.stopTimer();
            }
        }
    }

    private void setText(String t){
        if(text.getText().equals(t)) return;

        text.setText(t);
        text.setX((circuits.length / 2) * circuitDisplayWidth + (split - text.getBoundsInLocal().getWidth()) / 2.0);
        text.setY(header);
    }

    @Override public String toString(){
        if(winner == null){
            return "Draw";
        }else{
            return String.format(
                "Winner:\n%s\n(%s)",
                winner.getController().getName(),
                winner.getController().getAuthor()
            );
        }
    }

}
