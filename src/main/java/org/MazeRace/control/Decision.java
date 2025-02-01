package org.MazeRace.control;

/**
 * @param thrust whether the ship will thrust on the next frame
 * @param turn the sign of param turn will determine in
 *             which direction the ship will turn on the
 *             next frame. (0 for no movement)
 */
public record Decision(boolean thrust, int turn) { }