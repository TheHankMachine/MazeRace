package org.MazeRace;

import org.MazeRace.control.*;
import org.MazeRace.game.*;

public class Main {

    public static void main(String[] args) {

        new Race(3, 3, null, true,
            ExampleController.class // <-- replace with your own class
        );

//        new Race(2, 2, null, false,
//                ExampleController.class, // <-- insert multiple classes to have a race
//                ExampleController.class
//        );
    }

}