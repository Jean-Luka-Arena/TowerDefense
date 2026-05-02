package org.example.model.game;

import org.example.model.base.Base;
import org.example.model.enemy.*;
import org.example.model.level.Level;
import org.example.model.point.Point;
import org.example.model.route.Route;
import org.example.model.player.Player;
import org.example.model.projectile.Projectile;
import org.example.model.tower.Tower;

import java.util.List;
import java.util.ArrayList;

public class Game {

    private final Player player;
    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Projectile> projectiles;
    private final Route route;
    private final Base base;
    private final Level level;
    private final EnemyFactory enemyFactory;
    private double spawnTimer = 0;
    private final double spawnInterval; // segundos
    private int enemiesToSpawn; // total de enemigos del nivel

    public Game(Route route,Level level) {
        this.player = new Player();
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.route = route;
        this.level = level;
        this.enemyFactory = new EnemyFactory();
        this.enemiesToSpawn=level.getTotalEnemies();
        this.spawnInterval=level.getSpawnInterval();


        Point basePoint = route.getBasePoint();
        this.base = new Base(basePoint.getX(), basePoint.getY());
    }

    //usando la libreria math, usamos random para calcular numeros de 0 a 1 y evaluar que enemigo crear de forma aleatoria
    private Enemy getRandomEnemy() {
        double r = Math.random();
        double cumulative = 0;

        for (EnemyChance ec : level.getChances()) {
            cumulative += ec.getProbability();
            if (r <= cumulative) {
                return enemyFactory.create(ec.getType());
            }
        }

        return enemyFactory.create(EnemyType.WEAK); // fallback
    }

    public void spawnEnemy(Enemy enemy) {
        Point spawn = route.getSpawnPoint();
        enemy.moveTo(spawn.getX(), spawn.getY());
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean canPlaceTower(Point p) {
        if (!route.getTowerSpots().contains(p)) return false;
        for (Tower tower : towers) {
            if (tower.getX() == p.getX() && tower.getY() == p.getY()) {
                return false;
            }
        }
        return true;
    }

    public void addTower(Tower tower,Point point) {
        if (canPlaceTower(point)){
            tower.setPosition(point.getX(), point.getY());
            towers.add(tower);
            return;
        }
        throw new IllegalArgumentException("Casillero no disponible");
    }


    public boolean isGameOver() {
        return base.isDestroyed();
    }
    public boolean isWin() {
        return enemies.isEmpty() && enemiesToSpawn==0; // sin spawn pendiente
    }

    public void update(double deltaTime) {
        if (isGameOver()){return;}
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval && enemiesToSpawn > 0) {
            spawnEnemy(getRandomEnemy());
            spawnTimer = 0;
            enemiesToSpawn--;
        }
        //actualizamos todas las clases, con sus movimientos
        for (Enemy enemy : enemies) {
            enemy.update(route, deltaTime);
        }
        for (Tower tower : towers) {
            Projectile p = tower.update(enemies);
            if (p != null) {
                projectiles.add(p);
            }
        }
        for (Projectile p : projectiles) {
            p.update(deltaTime);
        }

        List<Enemy> toRemove = new ArrayList<>(); //lista para eliminar enemigos muertos o inmolados

        for (Enemy enemy : enemies) {//////////ASI O CON ITERADOR////////// DEFINIR
            if (enemy.isDead()) {
                player.earnMoney(enemy.getReward());
                player.addScore(enemy.getScoreValue());
                toRemove.add(enemy);
            } else if (enemy.hasReachedEnd(route)) {
                base.takeDamage(enemy.getDamage());
                toRemove.add(enemy);
            }
        }
        enemies.removeAll(toRemove);
        projectiles.removeIf(p -> !p.isActive()); //remueve proyectiles que ya no sirven
    }


    public Base getBase() {return base;}
    public Player getPlayer() {return player;}
}


