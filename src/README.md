please forgive all my shitty spaghetticode

## OBJECTIVE

You are challenged write a class extending [`Controller`](main/java/org/MazeRace/control/Controller.java)
to control a ship through a randomly generated maze. Your controller will
race other people's controllers through identical mazes.

To get started, check out [`Controller`](main/java/org/MazeRace/control/Controller.java) and 
[`ExampleController`](main/java/org/MazeRace/control/ExampleController.java) after you have 
finished this readme.

### RACES

Each race is made up of at least 3 rounds where two controllers race
to a target position. Before the first round, a new instance of your 
controller is initialised. The controller starts without any information 
regarding the maze and must explore it using only the methods it has 
access to. In all subsequent rounds, the controller can use any cached 
information from prior rounds to reach the target faster.

For the first two rounds, the target position remains constant. For 
subsequent rounds, the target position will be moved to new location.
The first controller to reach the target is awarded a point. If both 
controllers get a DNF, no points are awarded. First ship to 2 points 
wins the race. 

There are two ways to get a DNF: crashing into a wall or exceeding the
time limit.

### CLASSES & METHODS

Your controller will have access to instances of the following classes:
 
- [`Ship`](main/java/org/MazeRace/game/Ship.java) : a ship akin to the one in Asteroids that is navigating the maze.
- [`Circuit`](main/java/org/MazeRace/game/Circuit.java) : a container class for the maze, ship, point keeping, and display.

In which, your controller can use the following methods:

In [`Ship`](main/java/org/MazeRace/game/Ship.java):
 - `Ship.getX()` : the x position of the ship
 - `Ship.getY()` : the y position of the ship
 - `Ship.getVX()` : the x component of the ship's velocity
 - `Ship.getVY()` : the y component of the ship's velocity 
 - `Ship.getSpeed()` : the magnitude of the ship's velocity
 - `Ship.getAngle()` : the current heading of the ship
 - `Ship.getRotationStep()` : (see javadoc)
 - `Ship.getRaycast()` : the distance to the nearest wall in the direction that the ship is facing

In [`Circuit`](main/java/org/MazeRace/game/Ship.java):
 - `Circuit.random()` : random double `[0, 1)` seeded to the circuit. Please use this 
instead of `Math.random()` or `Random` so that race results are consistent
 - `Circuit.getTargetX()` : the x position of the target
 - `Circuit.getTargetY()` : the y position of the target
 - `Circuit.addNode(...node)` : adds javafx nodes to the circuit group

### GIVENS

 - You always start in the top left corner of the maze
 - You always start facing the direction of the first corridor
 - The 'tile' size for the mazes is always 1
 - The target is always in the center of a tile

### COMPETITION

If there is enough interest, I am planning to make a double-elimination 
tournament bracket.

All participants would have to submit their finalised code at some deadline yet to be determined.
Each following week, one round of the bracket will be simulated. Those who lose their matches and 
are eliminated will be give the opportunity to revise their code before being placed in a losers bracket.

Winner gets bragging rights or something idk.

### FINAL NOTES

Please reach out regarding any bugs, problems, or questions.
I hope this is a fun challenge. Good luck.<br/>
Sincerely,<br/>
-- Hank