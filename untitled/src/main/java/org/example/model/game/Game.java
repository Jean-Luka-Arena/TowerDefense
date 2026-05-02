package org.example.model.game;

import org.example.model.base.Base;
import org.example.model.enemy.Enemy;
import org.example.model.level.Point;
import org.example.model.level.Route;
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

    public Game(Route route) {
        this.player = new Player();
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.route = route;
        Point basePoint = route.getBasePoint();
        this.base = new Base(basePoint.getX(), basePoint.getY());
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public boolean isGameOver() {
        return base.isDestroyed();
    }

    public boolean isWin() {
        return enemies.isEmpty(); // sin spawn pendiente
    }

    public void update(double deltaTime) {
        if (isGameOver()){return;}
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


