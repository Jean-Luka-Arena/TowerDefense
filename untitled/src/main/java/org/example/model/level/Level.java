package org.example.model.level;

import org.example.model.enemy.ScheduledEnemy;
import java.util.List;

public class Level {
    private final List<ScheduledEnemy> scheduledEnemies;
    private final double spawnInterval;

    public Level(List<ScheduledEnemy> scheduledEnemies, double spawnInterval) {
        this.scheduledEnemies = scheduledEnemies;
        this.spawnInterval = spawnInterval;
    }

    public List<ScheduledEnemy> getScheduledEnemies() {
        return scheduledEnemies;
    }

    public int getTotalEnemies() {
        return scheduledEnemies.size();
    }

    public double getSpawnInterval() {
        return spawnInterval;
    }
}