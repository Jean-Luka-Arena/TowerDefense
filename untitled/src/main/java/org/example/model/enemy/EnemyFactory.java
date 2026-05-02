package org.example.model.enemy;

//creador de tipo de enemigo

public class EnemyFactory {

    public Enemy create(EnemyType type) {
        return switch (type) {
            case WEAK   -> new WeakEnemy();
            case FAST   -> new FastEnemy();
            case TANK   -> new TankEnemy();
        };
    }
}