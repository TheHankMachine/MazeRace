package org.MazeRace.control;

import org.MazeRace.game.Circuit;
import org.MazeRace.game.Ship;

public class ExampleController extends Controller {

    private static final double maxSpeed = 0.03;

    private int turnDirection = 0;
    private int turnFrames = 0;

    public ExampleController(Circuit circuit) {
        super(circuit);
    }

    @Override
    public Decision getDecision(Ship ship) {
        boolean thrust = false;
        int turn = 0;

        // turning
        if(turnFrames > 0){
            turn = turnDirection;
            turnFrames--;
        }else{
            // if close to a wall, rotate the ship for the next couple frames
            if(ship.getRaycast() <= 1){
                turnFrames = Ship.totalRotationTicks / 4;
                // randomise the turn direction to prevent getting stuck in cycles
                turnDirection = (int) Math.signum(circuit.random() - 0.5);
            }else{
                // accelerate if not going very fast
                thrust = ship.getSpeed() < maxSpeed;
            }
        }
        return new Decision(thrust, turn);
    }

    @Override
    public void onReset() {
        turnFrames = 0;
    }

    @Override
    public String getAuthor() {
        return "Hank";
    }

    @Override
    public long getColorHex() {
        return 0x463782;
    }
}
