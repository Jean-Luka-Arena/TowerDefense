package org.example;

import org.example.model.game.Game;
import org.example.model.level.InitialTower;
import org.example.model.level.LevelData;
import org.example.model.level.LevelLoader;
import org.example.model.tower.Tower;
import org.example.model.tower.TowerFactory;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        LevelLoader loader = new LevelLoader();
        TowerFactory towerFactory = new TowerFactory();
        int currentLevel = 1;

        LevelData data = loader.load("/nivel1.xml");
        Game game = new Game(data.getRoute(), data.getLevel(), data.getInitialMoney());

        // colocar torretas iniciales del XML
        for (InitialTower it : data.getInitialTowers()) {
            Tower tower = towerFactory.create(it.getType());
            game.addTower(tower, it.getSlot());
        }

        while (true) {
            game.update(0.016);

            System.out.println("Nivel: " + currentLevel);
            System.out.println("Enemigos activos: " + game.getEnemies().size());
            System.out.println("Base HP: " + game.getBase().getHealth());
            System.out.println("Dinero: " + game.getPlayer().getMoney());
            System.out.println("Puntaje: " + game.getPlayer().getScore());
            System.out.println("-----");

            if (game.isGameOver()) {
                System.out.println("PERDISTE");
                break;
            }

            if (game.isWin()) {
                System.out.println("Nivel " + currentLevel + " completado!");
                currentLevel++;

                if (currentLevel > 3) {
                    System.out.println("GANASTE TODO");
                    break;
                }

                data = loader.load("/nivel" + currentLevel + ".xml");
                game = new Game(data.getRoute(), data.getLevel(), data.getInitialMoney());
                for (InitialTower it : data.getInitialTowers()) {
                    Tower tower = towerFactory.create(it.getType());
                    game.addTower(tower, it.getSlot());
                }
            }

            Thread.sleep(16);
        }
    }
}