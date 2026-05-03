package org.example.view;
import org.example.view.sound.SoundManager;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.game.Game;
import org.example.model.level.*;
import org.example.model.point.Point;
import org.example.model.tower.*;

import java.util.ArrayList;
import java.util.List;

public class GameView {

    private final Scene scene;
    private Game game;
    private int currentLevel;
    private final Stage stage;
    private String selectedTowerType = null;
    private final SoundManager sounds;

    public GameView(Stage stage, int levelNumber, int accumulatedScore, int accumulatedMoney, int baseHealth, List<Tower> previousTowers) {
        this.stage = stage;
        this.currentLevel = levelNumber;
        this.sounds = new SoundManager();

        LevelLoader loader = new LevelLoader();
        TowerFactory towerFactory = new TowerFactory();
        LevelData data = loader.load("/nivel" + levelNumber + ".xml");

        int initialMoney = accumulatedMoney > 0 ? accumulatedMoney : data.getInitialMoney();
        this.game = new Game(data.getRoute(), data.getLevel(), initialMoney);
        game.getBase().setHealth(baseHealth);

        if (accumulatedScore > 0) {
            game.getPlayer().addScore(accumulatedScore);
        }

        for (InitialTower it : data.getInitialTowers()) {
            game.addTower(towerFactory.create(it.getType()), it.getSlot());
        }

        if (previousTowers != null) {
            for (Tower t : previousTowers) {
                game.addTower(towerFactory.create(t.getType()), new Point((int)t.getX(), (int)t.getY()));
            }
        }

        Canvas canvas = new Canvas(670, 520);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Label moneyLabel = new Label("$ " + game.getPlayer().getMoney());
        Label scoreLabel = new Label("Score: " + game.getPlayer().getScore());
        Label hpLabel    = new Label("❤ " + game.getBase().getHealth());

        moneyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        hpLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");

        HBox hud = new HBox(20, moneyLabel, scoreLabel, hpLabel);
        hud.setPadding(new Insets(6, 12, 6, 12));
        hud.setStyle("-fx-background-color: #222222;");
        hud.setAlignment(Pos.CENTER_LEFT);

        Label warningLabel = new Label("");
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
        warningLabel.setWrapText(true);
        warningLabel.setMaxWidth(120);

        VBox towerBar = buildTowerBar(towerFactory, warningLabel);
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

        GameRenderer renderer = new GameRenderer(gc);

        sounds.startMusic();

        canvas.setOnMouseClicked(e -> {
            if (selectedTowerType == null) {
                warningLabel.setText("⚠ Seleccioná una torreta primero");
                return;
            }

            double clickX = e.getX();
            double clickY = e.getY();

            Point nearestSlot = null;
            double minDist = Double.MAX_VALUE;

            for (Point slot : game.getRoute().getTowerSpots()) {
                double dist = Math.hypot(slot.getX() - clickX, slot.getY() - clickY);
                if (dist < minDist) {
                    minDist = dist;
                    nearestSlot = slot;
                }
            }

            if (nearestSlot != null && minDist <= 32) {
                Tower tower = towerFactory.create(selectedTowerType);
                int cost = tower.getPrice();

                if (game.getPlayer().getMoney() < cost) {
                    warningLabel.setText("⚠ Sin dinero ($" + cost + ")");
                } else {
                    try {
                        game.placeOrReplaceTower(tower, nearestSlot);
                        game.getPlayer().spend(cost);
                        warningLabel.setText("");
                        moneyLabel.setText("$ " + game.getPlayer().getMoney());
                        selectedTowerType = null;
                    } catch (IllegalArgumentException ex) {
                        if (ex.getMessage().equals("Misma torre")) {
                            warningLabel.setText("⚠ Ya hay misma torre");
                        } else {
                            warningLabel.setText("⚠ Slot inválido");
                        }
                    }
                }
            } else {
                warningLabel.setText("⚠ Sin slot acá");
            }
        });

        long[] lastTime = {System.nanoTime()};

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime[0]) / 1_000_000_000.0;
                lastTime[0] = now;

                game.update(deltaTime);

                // sonidos
                if (game.wasShotFiredSimple())   sounds.playDisparo();
                if (game.wasShotFiredPowerful()) sounds.playMisil();

                moneyLabel.setText("$ " + game.getPlayer().getMoney());
                scoreLabel.setText("Score: " + game.getPlayer().getScore());
                hpLabel.setText("❤ " + game.getBase().getHealth());

                renderer.render(game, deltaTime);

                if (game.isGameOver()) {
                    stop();
                    showDefeat();
                } else if (game.isWin()) {
                    stop();
                    showVictory();
                }
            }
        };
        timer.start();
    }

    private VBox buildTowerBar(TowerFactory towerFactory, Label warningLabel) {
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

            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
            iv.setFitWidth(48);
            iv.setFitHeight(48);
            iv.setPreserveRatio(true);

            Label lbl = new Label(label);
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
            lbl.setAlignment(Pos.CENTER);

            VBox btn = new VBox(4, iv, lbl);
            btn.setAlignment(Pos.CENTER);
            btn.setPadding(new Insets(6));

            btn.setOnMouseClicked(e -> {
                selectedTowerType = type;
                warningLabel.setText("");
            });

            bar.getChildren().add(btn);
        }

        bar.getChildren().add(warningLabel);
        return bar;
    }

    private void showVictory() {
        sounds.stopMusic();
        if (currentLevel >= 3) {
            VictoryView view = new VictoryView(stage);
            SceneTransition.fadeTo(stage, view.getScene());
            return;
        }

        GameView next = new GameView(
                stage,
                currentLevel + 1,
                game.getPlayer().getScore(),
                game.getPlayer().getMoney(),
                game.getBase().getHealth(),
                new ArrayList<>(game.getTowers())
        );
        stage.setScene(next.getScene());
    }

    private void showDefeat() {
        sounds.stopMusic();
        DefeatView view = new DefeatView(stage);
        SceneTransition.fadeTo(stage, view.getScene());
    }

    public Scene getScene() { return scene; }
}