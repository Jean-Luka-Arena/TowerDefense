package org.example.model.enemy;

public class WeakEnemy extends Enemy {

    private static final int HEALTH = 1;
    private static final int DAMAGE = 1;
    private static final int REWARD = 10;
    private static final int SCORE_VALUE = 10;
    private static final double SPEED = 60.0;

    public WeakEnemy() {
        super(HEALTH, DAMAGE, REWARD, SCORE_VALUE,SPEED);
    }

    @Override
    public String getType() {
        return "weak";
    }
}