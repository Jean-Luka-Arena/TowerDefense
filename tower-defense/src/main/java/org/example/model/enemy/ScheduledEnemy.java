package org.example.model.enemy;

public class ScheduledEnemy {
    private final EnemyType type;
    private final int delayMs;

    public ScheduledEnemy(EnemyType type, int delayMs) {
        this.type = type;
        this.delayMs = delayMs;
    }

    public EnemyType getType() { return type; }
    public int getDelayMs() { return delayMs; }
}