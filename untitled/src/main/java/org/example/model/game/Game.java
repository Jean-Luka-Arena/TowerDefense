package org.example.model.game;

import org.example.model.base.Base;
import org.example.model.enemy.*;
import org.example.model.level.Level;
import org.example.model.enemy.ScheduledEnemy;
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

    private double elapsedTimeMs = 0;
    private int nextEnemyIndex = 0;

    private boolean shotFiredSimple = false;
    private boolean shotFiredPowerful = false;
    private boolean enemyReachedBase = false;

    public Game(Route route, Level level, int initialMoney) {
        this.player = new Player(initialMoney);
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.route = route;
        this.level = level;
        this.enemyFactory = new EnemyFactory();

        Point basePoint = route.getBasePoint();
        this.base = new Base(basePoint.getX(), basePoint.getY());
    }

    public void update(double deltaTime) {
        if (isGameOver()) return;

        shotFiredSimple = false;
        shotFiredPowerful = false;
        enemyReachedBase = false;

        elapsedTimeMs += deltaTime * 1000;

        List<ScheduledEnemy> scheduled = level.getScheduledEnemies();
        while (nextEnemyIndex < scheduled.size()) {
            ScheduledEnemy next = scheduled.get(nextEnemyIndex);
            if (elapsedTimeMs >= next.getDelayMs()) {
                spawnEnemy(enemyFactory.create(next.getType()));
                nextEnemyIndex++;
            } else {
                break;
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(route, deltaTime);
        }

        for (Tower tower : towers) {
            Projectile p = tower.update(enemies);
            if (p != null) {
                projectiles.add(p);
                if (tower.getType().equals("powerful")) {
                    shotFiredPowerful = true;
                } else {
                    shotFiredSimple = true;
                }
            }
        }

        for (Projectile p : projectiles) {
            p.update(deltaTime);
        }

        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                player.earnMoney(enemy.getReward());
                player.addScore(enemy.getScoreValue());
                toRemove.add(enemy);
            } else if (enemy.hasReachedEnd(route)) {
                base.takeDamage(enemy.getDamage());
                enemyReachedBase = true;
                toRemove.add(enemy);
            }
        }
        enemies.removeAll(toRemove);
        projectiles.removeIf(p -> !p.isActive());
    }

    public void spawnEnemy(Enemy enemy) {
        Point spawn = route.getSpawnPoint();
        enemy.moveTo(spawn.getX(), spawn.getY());
        enemies.add(enemy);
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

    public void placeOrReplaceTower(Tower newTower, Point point) {
        for (int i = 0; i < towers.size(); i++) {
            Tower existing = towers.get(i);
            if ((int)existing.getX() == point.getX() &&
                    (int)existing.getY() == point.getY()) {
                if (existing.getType().equals(newTower.getType())) {
                    throw new IllegalArgumentException("Misma torre");
                }
                newTower.setPosition(point.getX(), point.getY());
                towers.set(i, newTower);
                return;
            }
        }

        if (!route.getTowerSpots().contains(point)) {
            throw new IllegalArgumentException("Slot inválido");
        }

        newTower.setPosition(point.getX(), point.getY());
        towers.add(newTower);
    }

    public void addTower(Tower tower, Point point) {
        if (canPlaceTower(point)) {
            tower.setPosition(point.getX(), point.getY());
            towers.add(tower);
            return;
        }
        throw new IllegalArgumentException("Casillero no disponible");
    }

    public boolean isGameOver() { return base.isDestroyed(); }
    public boolean isWin() { return enemies.isEmpty() && nextEnemyIndex >= level.getTotalEnemies(); }

    public boolean wasShotFiredSimple()   { return shotFiredSimple; }
    public boolean wasShotFiredPowerful() { return shotFiredPowerful; }

    public List<Projectile> getProjectiles() { return projectiles; }
    public Route getRoute() { return route; }
    public Base getBase() { return base; }
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Tower> getTowers() { return towers; }
}