package org.MazeRace.game;

import javafx.scene.shape.Circle;
import org.MazeRace.control.Controller;
import org.MazeRace.control.Decision;

public class Ship {

    public static final double velocityDecay = 0.05;//0.05;
    public static final double acceleration = 0.005;
    public static final double radius = 0.1;
    public static final int totalRotationTicks = 24;

    private final Circle cutout = new Circle(radius * 0.8);
    private final Circle ship = new Circle(radius);
    private final Controller controller;
    private final Circuit circuit;

    private final double initialX, initialY;
    private final int initialRotationStep;

    private double vx = 0, vy = 0;
    private double x, y;
    private int rotationStep;

    Ship(int x, int y, Circuit circuit, Controller controller, boolean startRight){
        this.x = x + 0.5;
        this.y = y + 0.5;

        this.circuit = circuit;
        this.controller = controller;

        rotationStep = startRight? 0 : totalRotationTicks / 4;

        initialX = this.x;
        initialY = this.y;
        initialRotationStep = rotationStep;

        ship.setFill(controller.getColor());
        cutout.setFill(Display.Palette.BACKGROUND.paint);

        circuit.addNode(ship, cutout);
    }

    void reset(){
        x = initialX;
        y = initialY;
        vx = 0;
        vy = 0;
        rotationStep = initialRotationStep;
        updatePosition();
    }

    void updatePosition(){
        double angle = getAngle();

        ship.setCenterX(x);
        ship.setCenterY(y);

        cutout.setCenterX(x - Math.cos(angle) * radius);
        cutout.setCenterY(y - Math.sin(angle) * radius);
    }

    void update(){
        Decision decision = controller.getDecision(this);

        if(decision != null) {
            rotationStep += (int) Math.signum(decision.turn());
            rotationStep = (rotationStep + totalRotationTicks) % totalRotationTicks;

            double angle = getAngle();

            if (decision.thrust()) {
                vx += Math.cos(angle) * acceleration;
                vy += Math.sin(angle) * acceleration;
            }
        }

        vx *= 1 - velocityDecay;
        vy *= 1 - velocityDecay;

        if(!circuit.validMove(x, y, x + vx, y + vy)){
            circuit.crash();
        }

        x += vx;
        y += vy;

        updatePosition();
    }

    /**
     * @return the angle of the ship's current heading
     */
    public double getAngle(){
        return Math.PI * 2 * rotationStep / totalRotationTicks;
    }

    /**
     * for rounding error purposes, the ship's angle is stored as an integer
     * akin to the gradian.
     * @return the rotation step
     */
    public int getRotationStep(){
        return rotationStep;
    }

    /**
     * @return the distance to a wall from the ship in the direction of the ship's heading
     */
    public double getRaycast(){
        return circuit.getRaycast(this);
    }

    /**
     * @return the x component of the ship's position
     */
    public double getX(){
        return x;
    }

    /**
     * @return the y component of the ship's position
     */
    public double getY(){
        return y;
    }

    /**
     * @return the x component of the ship's velocity
     */
    public double getVX(){
        return vx;
    }

    /**
     * @return the y component of the ship's velocity
     */
    public double getVY(){
        return vy;
    }

    /**
     * @return the magnitude of the ship's velocity
     */
    public double getSpeed(){
        return Math.hypot(vx, vy);
    }

}