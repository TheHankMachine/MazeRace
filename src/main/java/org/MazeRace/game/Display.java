package org.MazeRace.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
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
    private static final Group group = new Group();
    private static final Scale scale = new Scale();
    private static Rectangle background;

    private static Pane root;
    private static Timeline timer;
    private static Scene scene;
    private static EventHandler callback;

    private static int width, height;
    private static double aspectRatio;

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

        background = new Rectangle(0, 0, width, height);
        background.setFill(Palette.BACKGROUND.paint);

        group.getChildren().addAll(background);
        group.getChildren().addAll(nodeQueue);
        root.getChildren().addAll(group);
        group.getTransforms().add(scale);

        nodeQueue.clear();

        scene.widthProperty().addListener((_, _, newSceneWidth) -> resize(newSceneWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newSceneHeight) -> resize(scene.getWidth(), newSceneHeight.doubleValue()));

        timer = new Timeline(new KeyFrame(Duration.millis(20), callback));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    static void resize(double width, double height){
        double w = Math.min(width, height * aspectRatio);
        double h = w / aspectRatio;

        scale.setX(w / Display.width);
        scale.setY(h / Display.height);

        group.setTranslateX((width - w) / 2);
        group.setTranslateY((height - h) / 2);
    }

    static void stopTimer(){
        timer.stop();
    }

    public static void addNode(Node...node){
        if(root == null){
            nodeQueue.addAll(Arrays.asList(node));
            return;
        }
        group.getChildren().addAll(node);
    }

    public static void start(int width, int height, EventHandler callback){
        Display.width = width;
        Display.height = height;
        Display.aspectRatio = (double) width / height;
        Display.callback = callback;
        launch();
    }

}