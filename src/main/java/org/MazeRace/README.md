please forgive all my shitty spaghetticode

## OBJECTIVE

You are challenged write a class extending [`Controller`](control/Controller.java)
to control a ship through a randomly generated maze. Your controller will
race other people's controllers through identical mazes.

More information is in [`Controller`](control/Controller.java).
Check out [`ExampleController`](control/ExampleController.java) for an example.

### RACES

Each race is made up of at least 3 rounds where two controllers race
to a target position. Before the first round, a new instance of your 
controller is initialised. The controller starts without any information 
regarding the maze and must explore it using only the methods it has 
access to. In all subsequent rounds, the controller can use cached any 
information from prior rounds.

For the first two rounds, the target position remains constant. For 
subsequent rounds, the target position will be moved to new location.
The first controller to reach the target is awarded a point. If both 
controllers get a DNF, no points are awarded. First ship to 2 points 
wins the race.

There are two ways to get a DNF: crashing into a wall or exceeding the
time limit.

### CLASSES

There are a couple classes you have access to that are maybe, perchance, not intuitively 
named. Here are some explanations of each of them:
 
- [`Ship`](game/Ship.java) : a ship akin to the one in Asteroids that is navigating the maze.
- [`Circuit`](game/Circuit.java) : a container class for the maze, ship, point keeping, and display

huh... i guess thats it

### METHODS

Your controller will have access to the following methods:

In [`Ship`](game/Ship.java):
 - `Ship.getX()` : the x position of the ship
 - `Ship.getY()` : the y position of the ship
 - `Ship.getVX()` : the x component of the ship's velocity
 - `Ship.getVY()` : the y component of the ship's velocity 
 - `Ship.getSpeed()` : the magnitude of the ship's velocity
 - `Ship.getAngle()` : the current heading of the ship
 - `Ship.getRotationStep()` : (see javadoc)
 - `Ship.getRaycast()` : the distance to the nearest wall in the direction that the ship is facing

In [`Circuit`](game/Ship.java):
 - `Circuit.random()` : random double `[0, 1)` seeded to the circuit. Please use this 
instead of `Math.random()` or `Random` so that race results are consistent
 - `Circuit.getTargetX()` : the x position of the target
 - `Circuit.getTargetY()` : the y position of the target
 - `Circuit.addNode(...node)` : adds javafx nodes to the circuit

### GIVENS

 - You always start in the top left corner of the maze
 - You always start facing the direction of the first corridor
 - The 'tile' size for the mazes is always 1
 - The target is always in the center of a tile