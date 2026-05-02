package org.example.model.enemy;

//clase para depues evaluar chance random de aparicion

public class EnemyChance {
    private final EnemyType type;
    private final double probability;

    public EnemyChance(EnemyType  type, double probability) {
        this.type = type;
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
    public EnemyType getType() { return type;}
}
