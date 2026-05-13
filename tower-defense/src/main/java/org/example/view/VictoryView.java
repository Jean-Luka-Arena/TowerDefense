package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.GameController;
import org.example.model.tower.Tower;

import java.util.List;

public class VictoryView {

    private final Scene scene;

    public VictoryView(Stage stage, int currentLevel, int score, int money, int baseHealth, List<Tower> towers) {
        Label title = new Label("¡VICTORIA!");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: green; -fx-font-weight: bold;");

        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button menuBtn = new Button("Volver al menú");
        menuBtn.setOnAction(e -> {
            MenuView menu = new MenuView(stage);
            SceneTransition.fadeTo(stage, menu.getScene());
        });

        VBox root = new VBox(20, title, scoreLabel);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        if (currentLevel < 3) {
            Button nextBtn = new Button("Siguiente nivel");
            nextBtn.setOnAction(e -> {
                GameController controller = new GameController(stage, currentLevel + 1, score, money, baseHealth, towers);
                SceneTransition.fadeTo(stage, controller.getScene());
            });
            root.getChildren().add(nextBtn);
        }

        root.getChildren().add(menuBtn);
        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() { return scene; }
}