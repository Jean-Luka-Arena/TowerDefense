package org.example.view;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneTransition {

    public static void fadeTo(Stage stage, Scene newScene) {

        Scene currentScene = stage.getScene();
        Parent root = currentScene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {

            stage.setScene(newScene);

            Parent newRoot = newScene.getRoot();
            newRoot.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }
}