package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.game.Game;
import org.example.model.tower.TowerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class GameView {

    private final Scene scene;
    private final Game game;
    private final int currentLevel;
    private final Stage stage;
    private final Label moneyLabel;
    private final Label scoreLabel;
    private final Label hpLabel;
    private final Label warningLabel;
    private final Canvas canvas;
    private final GameRenderer renderer;

    private Consumer<String> onTowerSelected = type -> {};

    public GameView(Stage stage, int levelNumber, Game game, TowerFactory towerFactory) {
        this.stage = stage;
        this.currentLevel = levelNumber;
        this.game = game;

        this.canvas = new Canvas(670, 520);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.renderer = new GameRenderer(gc);

        this.moneyLabel = new Label("$ " + game.getPlayer().getMoney());
        this.scoreLabel = new Label("Score: " + game.getPlayer().getScore());
        this.hpLabel    = new Label("❤ " + game.getBase().getHealth());

        moneyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        hpLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");

        HBox hud = new HBox(20, moneyLabel, scoreLabel, hpLabel);
        hud.setPadding(new Insets(6, 12, 6, 12));
        hud.setStyle("-fx-background-color: #222222;");
        hud.setAlignment(Pos.CENTER_LEFT);

        this.warningLabel = new Label("");
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
        warningLabel.setWrapText(true);
        warningLabel.setMaxWidth(120);

        VBox towerBar = buildTowerBar(towerFactory);
        towerBar.setStyle("-fx-background-color: #333333;");
        towerBar.setPadding(new Insets(12, 8, 12, 8));
        towerBar.setAlignment(Pos.TOP_CENTER);
        towerBar.setPrefWidth(130);

        BorderPane root = new BorderPane();
        root.setTop(hud);
        root.setCenter(canvas);
        root.setRight(towerBar);
        root.setStyle("-fx-background-color: #222222;");
        this.scene = new Scene(root, 800, 600);
    }

    public void render(double deltaTime) {
        renderer.render(game, deltaTime);
    }

    public void updateHUD() {
        moneyLabel.setText("$ " + game.getPlayer().getMoney());
        scoreLabel.setText("Score: " + game.getPlayer().getScore());
        hpLabel.setText("❤ " + game.getBase().getHealth());
    }

    public void showVictory() {
        SceneTransition.fadeTo(stage, new VictoryView(
                stage, currentLevel,
                game.getPlayer().getScore(),
                game.getPlayer().getMoney(),
                game.getBase().getHealth(),
                new ArrayList<>(game.getTowers())
        ).getScene());
    }

    public void showDefeat() {
        SceneTransition.fadeTo(stage, new DefeatView(stage).getScene());
    }

    public void setOnCanvasClick(Consumer<MouseEvent> handler) {
        canvas.setOnMouseClicked(handler::accept);
    }

    public void setOnTowerSelected(Consumer<String> handler) {
        this.onTowerSelected = handler;
    }

    public void showWarning(String msg) { warningLabel.setText(msg); }
    public void clearWarning()          { warningLabel.setText(""); }
    public void refreshMoney()          { moneyLabel.setText("$ " + game.getPlayer().getMoney()); }

    private VBox buildTowerBar(TowerFactory towerFactory) {
        VBox bar = new VBox(12);
        bar.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Torretas");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        bar.getChildren().add(title);

        String[][] towers = {
                {"simple",   "/sprites_tower/tower_simple.png",    "Simple\n$50"},
                {"fast",     "/sprites_tower/tower_fast.png",      "Rápida\n$75"},
                {"powerful", "/sprites_tower/tower_powerful_0.png","Poderosa\n$100"},
        };

        for (String[] t : towers) {
            String type    = t[0];
            String imgPath = t[1];
            String label   = t[2];

            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgPath))));
            iv.setFitWidth(48);
            iv.setFitHeight(48);
            iv.setPreserveRatio(true);

            Label lbl = new Label(label);
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
            lbl.setAlignment(Pos.CENTER);

            VBox btn = new VBox(4, iv, lbl);
            btn.setAlignment(Pos.CENTER);
            btn.setPadding(new Insets(6));

            btn.setOnMouseClicked(e -> { clearWarning(); onTowerSelected.accept(type); });

            bar.getChildren().add(btn);
        }

        bar.getChildren().add(warningLabel);
        return bar;
    }

    public Scene getScene() { return scene; }
}