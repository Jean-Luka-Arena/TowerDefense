package org.example.model.enemy;
import org.example.model.route.Route;
import org.example.model.point.Point;

public abstract class Enemy {

    private int health;
    private final int maxHealth; ///esto desp vemos, es x si queremos poner una barra de vida
    private final int damage; /// esto es para saber que danio nos hace si no lo matamos y llega a la base
    private final int reward; ///son las monedas que ganás al matar un enemigo
    private final int scoreValue; ///es el puntaje que podemos mostrar x pantalla que vamos acumulando, mepa que es opcional pero lo pongo x las dudas
    private double x;
    private double y; /// este y x es para mover al frame a frame siguiendo la ruta
    private final double speed;
    private int routeIndex; /// esto es xq la ruta va a tener una lista de punts y necesitamos un numero dice hacia que punto se está moviendo el enemigo
    private boolean alive; /// esto es para saber si sigue vivo o caduco

    protected Enemy(int health, int damage, int reward, int scoreValue, double speed) {
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.reward = reward;
        this.scoreValue = scoreValue;
        this.speed = speed;
        this.routeIndex = 0;
        this.alive = true;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.alive = false;
        }
    }

    // maneja logica de todos los metodos, funcionamiento independiente a main
    public void update(Route route, double deltaTime) {
        if (!alive) return;
        if (routeIndex >= route.size()) return;

        Point target = route.getPoint(routeIndex);
        double dx = target.getX() - x;
        double dy = target.getY() - y;

        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < speed * deltaTime) {
            moveTo(target.getX(), target.getY());
            routeIndex++;
            return;
        }

        // movimiento normal, ajusta para q no haya bugs con fps, q no se pase
        double ratio = (speed * deltaTime) / distance;
        double newX = x + dx * ratio;
        double newY = y + dy * ratio;
        moveTo(newX, newY);
    }

    public boolean hasReachedEnd(Route route) {
        Point base = route.getBasePoint();
        return this.x == base.getX() && this.y == base.getY();
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void advanceRouteIndex() {
        this.routeIndex++;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public int getReward() { return reward; }
    public int getScoreValue() { return scoreValue; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getRouteIndex() { return routeIndex; }

    public double getSpeed(){return speed;};
    public abstract String getType();
}