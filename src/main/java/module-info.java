module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.MazeRace to javafx.fxml;
    exports org.MazeRace;
    exports org.MazeRace.game;
    opens org.MazeRace.game to javafx.fxml;
}