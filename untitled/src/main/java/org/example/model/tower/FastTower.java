package org.example.model.tower;

public class FastTower extends Tower {

    private static final int DAMAGE = 1;
    private static final int PRICE = 75;
    private static final int RANGE = 100;
    private static final int SHOTSPEED = 500;

    public FastTower() {
        super(DAMAGE, PRICE, RANGE, SHOTSPEED);
    }

}