package org.example.model.enemy;

public class EnemyFactory {

    public Enemy create(String type) {
        return switch (type.toLowerCase()) {
            case "weak"   -> new WeakEnemy();
            case "medium" -> new MediumEnemy();
            case "strong" -> new StrongEnemy();
            default -> throw new IllegalArgumentException("Unknown enemy type: " + type);
        };
    }
}