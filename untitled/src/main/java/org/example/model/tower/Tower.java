package org.example.model.tower;

import org.example.model.enemy.Enemy;
import org.example.model.projectile.Projectile;

import java.util.List;

public abstract class Tower {

    private final int damage; /// danio de la torreta
    private final int price; /// precio de la torreta
    private final int range; /// rango de la torreta
    private final int shotSpeed; /// cadencia de disparo
    private double x,y; /// posicion de la torreta
    private long timeLastShot; /// tiempo ultimo disparo

    protected Tower(int damage, int price, int range, int shotSpeed) {
        this.damage=damage;
        this.price=price;
        this.range=range;
        this.shotSpeed=shotSpeed;
    }

    public Projectile shoot(Enemy enemy){
        if (enemy == null) return null;
        if (!canShoot()) return null;
        timeLastShot = System.currentTimeMillis();
        return new Projectile(x, y, enemy, damage);
    }

    public boolean canShoot(){
        long currentTime = System.currentTimeMillis(); //tiempo transcurrido actual
        return (currentTime - timeLastShot) >= shotSpeed;
    }

    public boolean isInRange(Enemy enemy){
        double dx = this.x - enemy.getX();
        double dy = this.y - enemy.getY();

        double distance = dx * dx + dy * dy;
        double rangeSquared = this.range * this.range;

        return distance <= rangeSquared;
    }

    public Enemy findFirstEnemyInRange(List<Enemy> enemies){
        Enemy bestTarget=null;
        int bestRIndex=-1;
        for (Enemy enemy : enemies){
            if (!enemy.isAlive()){continue;}
            if (isInRange(enemy)){
                int newRouteIndex=enemy.getRouteIndex();
                if (newRouteIndex>bestRIndex){
                    bestRIndex=newRouteIndex;
                    bestTarget=enemy;
                }
            }
        }
        return bestTarget;
    }

    public Projectile update(List<Enemy> enemies){
        Enemy enemy = findFirstEnemyInRange(enemies);
        return shoot(enemy);
    }


    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getDamage() { return damage; }
    public int getPrice() { return price; }
    public int getRange() { return range; }
    public int getShotSpeed() { return shotSpeed; }

    public double getX() { return x; }
    public double getY() { return y; }
    public long getTimeLastShot() { return timeLastShot; }

}