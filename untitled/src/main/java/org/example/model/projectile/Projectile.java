package org.example.model.projectile;
import org.example.model.enemy.Enemy;

public class Projectile {
    private double x; /// posicion actual del proyectil en pantalla
    private double y; /// posicion actual del proyectil en pantalla
    private final int damage; /// dano que va a aplicarle a enemigo cuando impacte
    private final double speed; /// velocidad a la que va (en pixeles por segundo)
    private Enemy target; /// referencia al enemigo al que persigue, se va a ir actualizando a medida que este avance (persigue al enemigo basicamente)
    private boolean active; /// estado del proyectil, se desactiva cuando impacta al enemigo o cuando el enemigo muere antes de que llegue.

    private static final double SPEED = 200.0;

    public Projectile(double x, double y, Enemy target, int damage) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.damage = damage;
        this.speed = SPEED;
        this.active = true;
    }

    public void update(double deltaTime) {
        if (!active) return;

        if (target.isDead()) {
            active = false;
            return;
        }

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed * deltaTime) {
            target.takeDamage(damage);
            active = false;
            return;
        }

        double ratio = speed * deltaTime / distance;
        x += dx * ratio;
        y += dy * ratio;
    }

    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getDamage() { return damage; }
    public Enemy getTarget() { return target; }
}
