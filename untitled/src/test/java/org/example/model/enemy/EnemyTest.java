package org.example.model.enemy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    @Test
    void weakEnemyHasCorrectStats() {
        Enemy enemy = new WeakEnemy();
        assertEquals(1, enemy.getHealth());
        assertEquals(1, enemy.getDamage());
        assertEquals(10, enemy.getReward());
        assertEquals(80.0, enemy.getSpeed());
    }

    @Test
    void enemyDiesWhenHealthReachesZero() {
        Enemy enemy = new WeakEnemy();
        enemy.takeDamage(1);
        assertTrue(enemy.isDead());
    }

    @Test
    void enemyDoesNotDieWithPartialDamage() {
        Enemy enemy = new FastEnemy();
        enemy.takeDamage(1);
        assertTrue(enemy.isAlive());
    }

    @Test
    void factoryCreatesCorrectTypes() {
        EnemyFactory factory = new EnemyFactory();
        assertInstanceOf(WeakEnemy.class,   factory.create("weak"));
        assertInstanceOf(FastEnemy.class, factory.create("medium"));
        assertInstanceOf(TankEnemy.class, factory.create("strong"));
    }

    @Test
    void factoryThrowsOnUnknownType() {
        EnemyFactory factory = new EnemyFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.create("unknown"));
    }
}