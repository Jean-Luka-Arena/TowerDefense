package org.example.model.enemy;

public class MediumEnemy extends Enemy {

    private static final int HEALTH = 2;
    private static final int DAMAGE = 2;
    private static final int REWARD = 20;
    private static final int SCORE_VALUE = 20;
    private static final double SPEED = 60.0;

    public MediumEnemy() {
        super(HEALTH, DAMAGE, REWARD, SCORE_VALUE);
    }

    @Override
    public double getSpeed() {
        return SPEED;
    }

    @Override
    public String getType() {
        return "medium";
    }
}