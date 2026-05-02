package org.example.model.level;


import org.example.model.enemy.EnemyChance;

import java.util.List;

public class Level {
    private final List<EnemyChance> chances;
    private final int totalEnemies;
    private final double spawnInterval;

    public Level(List<EnemyChance> chances,int totalEnemies,double spawnInterval) {
        this.chances = chances;
        this.totalEnemies = totalEnemies;
        this.spawnInterval = spawnInterval;
    }
    public List<EnemyChance> getChances() {
        return chances;
    }
    public int getTotalEnemies() {
        return totalEnemies;
    }

    public double getSpawnInterval() {
        return spawnInterval;
    }

}