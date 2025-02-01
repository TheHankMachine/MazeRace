package org.MazeRace.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Display extends Application {

    public enum Palette{
        WALL(       0x000000),
        BACKGROUND( 0xe8e7cb),
        HIGHLIGHT(  0xf89020),
        SHIP(       0xf89020);

        final public Color paint;
        Palette(long rgb){
            paint = Color.rgb(
                (int) ((rgb >> 16) & 0xff),
                (int) ((rgb >> 8) & 0xff),
                (int) (rgb & 0xff)
            );
        }
    }

    private static final List<Node> nodeQueue = new LinkedList<>();

    private static Pane root;
    private static Scene scene;
    private static Timeline timer;
    private static EventHandler callback;
    private static int width, height;

    @Override
    public void start(Stage stage) {
        root = new Pane();

        scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(_ -> {
            Platform.exit();
            System.exit(0);
        });

        root.setBackground(new Background(new BackgroundFill(Palette.BACKGROUND.paint, null, null)));

        root.getChildren().addAll(nodeQueue);
        nodeQueue.clear();

        timer = new Timeline(new KeyFrame(Duration.millis(20), callback));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    static void stopTimer(){
        timer.stop();
    }

    public static void addNode(Node...node){
        if(root == null){
            nodeQueue.addAll(Arrays.asList(node));
            return;
        }
        root.getChildren().addAll(node);
    }

    public static void start(int width, int height, EventHandler callback){
        Display.width = width;
        Display.height = height;
        Display.callback = callback;
        launch();
    }

}