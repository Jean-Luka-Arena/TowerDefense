package org.example.model.level;

import org.example.model.enemy.ScheduledEnemy;
import java.util.List;

public class Level {
    private final List<ScheduledEnemy> scheduledEnemies;


    public Level(List<ScheduledEnemy> scheduledEnemies) {
        this.scheduledEnemies = scheduledEnemies;

    }

    public List<ScheduledEnemy> getScheduledEnemies() {
        return scheduledEnemies;
    }

    public int getTotalEnemies() {
        return scheduledEnemies.size();
    }
}