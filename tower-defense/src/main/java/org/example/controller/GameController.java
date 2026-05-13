package org.example.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.game.Game;
import org.example.model.level.*;
import org.example.model.point.Point;
import org.example.model.tower.Tower;
import org.example.model.tower.TowerFactory;
import org.example.view.GameView;
import org.example.view.sound.SoundManager;

import java.util.List;

public class GameController {

    private final Game game;
    private final TowerFactory towerFactory;
    private final GameView view;
    private final SoundManager soundManager;

    private String selectedTowerType = null;

    public GameController(Stage stage, int levelNumber, int accumulatedScore, int accumulatedMoney, int baseHealth, List<Tower> previousTowers) {

        LevelLoader loader = new LevelLoader();
        this.towerFactory = new TowerFactory();
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
                game.addTower(towerFactory.create(t.getType()), new Point((int) t.getX(), (int) t.getY()));
            }
        }

        this.soundManager = new SoundManager();
        game.addListener(soundManager);
        soundManager.startMusic();

        this.view = new GameView(stage, levelNumber, game, towerFactory);

        view.setOnTowerSelected(type -> selectedTowerType = type);
        view.setOnCanvasClick(e -> handleTowerPlacement(e.getX(), e.getY()));

        startGameLoop();
    }

    private void startGameLoop() {
        long[] lastTime = {System.nanoTime()};

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime[0]) / 1_000_000_000.0;
                lastTime[0] = now;

                game.update(deltaTime);
                view.updateHUD();
                view.render(deltaTime);

                if (game.isGameOver()) {
                    stop();
                    PauseTransition pause = new PauseTransition(Duration.millis(100));
                    pause.setOnFinished(ev -> view.showDefeat());
                    pause.play();
                } else if (game.isWin()) {
                    stop();
                    PauseTransition pause = new PauseTransition(Duration.millis(100));
                    pause.setOnFinished(ev -> view.showVictory());
                    pause.play();
                }
            }
        };
        timer.start();
    }

    private void handleTowerPlacement(double clickX, double clickY) {
        if (selectedTowerType == null) {
            view.showWarning("⚠ Seleccioná una torreta primero");
            return;
        }

        Point nearestSlot = null;
        double minDist = Double.MAX_VALUE;

        for (Point slot : game.getRoute().getTowerSpots()) {
            double dist = Math.hypot(slot.getX() - clickX, slot.getY() - clickY);
            if (dist < minDist) {
                minDist = dist;
                nearestSlot = slot;
            }
        }

        if (nearestSlot == null || minDist > 32) {
            view.showWarning("⚠ Sin slot acá");
            return;
        }

        Tower tower = towerFactory.create(selectedTowerType);
        int cost = tower.getPrice();

        if (game.getPlayer().getMoney() < cost) {
            view.showWarning("⚠ Sin dinero ($" + cost + ")");
            return;
        }

        try {
            game.placeOrReplaceTower(tower, nearestSlot);
            game.getPlayer().spend(cost);
            view.clearWarning();
            view.refreshMoney();
            selectedTowerType = null;
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().equals("Misma torre")) {
                view.showWarning("⚠ Ya hay misma torre");
            } else {
                view.showWarning("⚠ Slot inválido");
            }
        }
    }

    public Scene getScene() {
        return view.getScene();
    }
}