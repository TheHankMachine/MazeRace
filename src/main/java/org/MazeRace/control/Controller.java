package org.MazeRace.control;

import javafx.scene.paint.Color;
import org.MazeRace.game.Circuit;
import org.MazeRace.game.Ship;

public abstract class Controller {

    protected Circuit circuit;
    private Color color;

    public Controller(Circuit circuit){
        this.circuit = circuit;
    }

    /**
     * @return a decision to control the ship (see org.example.control.decision)
     */
    public abstract Decision getDecision(Ship ship);

    /**
     * @return the name or tag of the person who authored the controller code
     */
    public abstract String getAuthor();

    /**
     * customisation :)
     * @return the hex code of the color of your ship
     */
    public abstract long getColorHex();

    /**
     * Called on circuit reset
     */
    public void onReset(){;}

    /**
     * @return returns a color for the ship
     */
    public final Color getColor(){
        if (color == null) {
            long rgb = getColorHex();
            color = Color.rgb(
                (int) ((rgb >> 16) & 0xff),
                (int) ((rgb >> 8) & 0xff),
                (int) (rgb & 0xff)
            );
        }
        return color;
    }

    /**
     * @return the name of the java class
     */
    public final String getName(){
        String fullName = getClass().getName();
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

}