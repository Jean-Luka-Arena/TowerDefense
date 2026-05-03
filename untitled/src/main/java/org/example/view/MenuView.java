package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuView {

    private final Scene scene;

    public MenuView(Stage stage) {

        // 🎮 título
        Text title = new Text("TOWER DEFENSE");
        title.setStyle("""
                -fx-font-size: 42px;
                -fx-fill: white;
                -fx-font-weight: bold;
        """);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.DARKGREEN);
        glow.setRadius(20);
        title.setEffect(glow);

        // 🔘 botones
        Button startBtn = createButton("INICIAR JUEGO");
        Button exitBtn  = createButton("SALIR");

        // acciones (SIN fade)
        startBtn.setOnAction(e -> {
            GameView gameView = new GameView(stage, 1, 0, 0, 10,null);
            stage.setScene(gameView.getScene());
        });

        exitBtn.setOnAction(e -> stage.close());

        // layout
        VBox root = new VBox(30, title, startBtn, exitBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1e1e;");

        scene = new Scene(root, 800, 600);
    }

    private Button createButton(String text) {
        Button btn = new Button(text);

        btn.setPrefWidth(220);
        btn.setPrefHeight(50);

        btn.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
        """);

        // hover efecto
        btn.setOnMouseEntered(e ->
                btn.setStyle("""
                    -fx-background-color: #27ae60;
                    -fx-text-fill: white;
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 10;
                """)
        );

        btn.setOnMouseExited(e ->
                btn.setStyle("""
                    -fx-background-color: #2ecc71;
                    -fx-text-fill: black;
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 10;
                """)
        );

        return btn;
    }

    public Scene getScene() {
        return scene;
    }
}