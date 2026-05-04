package org.example.model.base;

public class Base {

    private int health;
    private final double x;
    private final double y;

    public Base(double x, double y) {
        this.health = 10;
        this.x = x;
        this.y = y;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public double getX() {return x;}
    public double getY() {return y;}
}