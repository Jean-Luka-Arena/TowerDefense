package org.example;

import org.example.model.game.Game;
import org.example.model.level.Levels;
import org.example.model.point.Point;
import org.example.model.route.Route;
import org.example.model.enemy.*;
import org.example.model.tower.*;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 1. crear ruta
        List<Point> points = List.of(
                new Point(0, 0),
                new Point(100, 0),
                new Point(200, 0),
                new Point(300, 0)
        );
        List<Point> towerSpots = List.of(
                new Point(100, 4),new Point(200,1),new Point(300,2),new Point(80, 2)
        );
        Route route = new Route(points, towerSpots);

        // 2. crear juego
        Game game = new Game(route, Levels.LEVEL_1);
        int currentLevel = 1;
        game.addTower(new SimpleTower(),new Point(100,4));
        game.addTower(new FastTower(),new Point(200,1));
        game.addTower(new FastTower(),new Point(80,2));
        game.addTower(new PowerfulTower(),new Point(300,2));

        while (true) {

            game.update(0.016);

            System.out.println("Nivel: " + currentLevel);
            System.out.println("Enemigos: " + game.getEnemies().size());
            System.out.println("Base HP: " + game.getBase().getHealth());
            System.out.println("-----");

            if (game.isGameOver()) {
                System.out.println("PERDISTE");
                break;
            }

            if (game.isWin()) {
                currentLevel++;

                if (currentLevel == 2) {
                    game = new Game(route, Levels.LEVEL_2);
                } else if (currentLevel == 3) {
                    game = new Game(route, Levels.LEVEL_3);
                } else {
                    System.out.println("GANASTE TODO");
                    break;
                }
            }

            Thread.sleep(16);
        }
    }
}