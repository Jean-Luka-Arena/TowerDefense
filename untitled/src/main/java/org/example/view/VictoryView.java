package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VictoryView {

    private final Scene scene;

    public VictoryView(Stage stage) {

        Label title = new Label("¡GANASTE!");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: green; -fx-font-weight: bold;");

        Button menuBtn = new Button("Volver al menú");

        menuBtn.setOnAction(e -> {
            MenuView menu = new MenuView(stage);
            SceneTransition.fadeTo(stage, menu.getScene());
        });

        VBox root = new VBox(20, title, menuBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}