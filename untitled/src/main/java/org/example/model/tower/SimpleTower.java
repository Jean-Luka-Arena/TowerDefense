package org.example.model.tower;

public class SimpleTower extends Tower {

    private static final int DAMAGE = 1;
    private static final int PRICE = 50;
    private static final int RANGE = 100;
    private static final int SHOTSPEED = 1000;

    public SimpleTower() {
        super(DAMAGE, PRICE, RANGE, SHOTSPEED);
    }

}