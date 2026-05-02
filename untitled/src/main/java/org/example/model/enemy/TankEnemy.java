package org.example.model.enemy;

public class TankEnemy extends Enemy {

    private static final int HEALTH = 3;
    private static final int DAMAGE = 3;
    private static final int REWARD = 30;
    private static final int SCORE_VALUE = 30;
    private static final double SPEED = 40.0;

    public TankEnemy() {
        super(HEALTH, DAMAGE, REWARD, SCORE_VALUE,SPEED);
    }

    @Override
    public String getType() {
        return "strong";
    }
}