package org.example.model.game;

import org.example.model;

import java.util.List;
import java.util.ArrayList;

public class Game {

    private List<Enemy> enemies;
    private List<Tower> towers;
    private List<Projectile> projectiles;
    private Base base;

    public Game() {
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.base = new Base()
    }

    public void update(double deltaTime) {
        for (Enemy enemy : enemies) {
            enemy.update(path, deltaTime);
            if enemy.hasReachedEnd(){
                base.
            }
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
    }
}

