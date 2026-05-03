package org.example.model.tower;

public class PowerfulTower extends Tower {

    private static final int DAMAGE = 2;
    private static final int PRICE = 100;
    private static final int RANGE = 100;
    private static final int SHOTSPEED = 1000;

    public PowerfulTower() {
        super(DAMAGE, PRICE, RANGE, SHOTSPEED);
    }

    @Override
    public String getType() { return "powerful"; }

}