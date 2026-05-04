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
        assertEquals(60.0, enemy.getSpeed());
    }

    @Test
    void fastEnemyHasCorrectStats() {
        Enemy enemy = new FastEnemy();
        assertEquals(2, enemy.getHealth());
        assertEquals(2, enemy.getDamage());
        assertEquals(20, enemy.getReward());
        assertEquals(90.0, enemy.getSpeed());
    }

    @Test
    void tankEnemyHasCorrectStats() {
        Enemy enemy = new TankEnemy();
        assertEquals(6, enemy.getHealth());
        assertEquals(3, enemy.getDamage());
        assertEquals(30, enemy.getReward());
        assertEquals(40.0, enemy.getSpeed());
    }

    @Test
    void enemyDiesWhenHealthReachesZero() {
        Enemy enemy = new WeakEnemy();
        enemy.takeDamage(1);
        assertTrue(enemy.isDead());
        assertFalse(enemy.isAlive());
    }

    @Test
    void enemyHealthDoesNotGoBelowZero() {
        Enemy enemy = new WeakEnemy();
        enemy.takeDamage(999);
        assertEquals(0, enemy.getHealth());
    }

    @Test
    void enemyDoesNotDieWithPartialDamage() {
        Enemy enemy = new FastEnemy(); // tiene 2 de vida
        enemy.takeDamage(1);
        assertTrue(enemy.isAlive());
        assertEquals(1, enemy.getHealth());
    }

    @Test
    void factoryCreatesCorrectTypesWithEnum() {
        EnemyFactory factory = new EnemyFactory();
        assertInstanceOf(WeakEnemy.class,  factory.create(EnemyType.WEAK));
        assertInstanceOf(FastEnemy.class,  factory.create(EnemyType.FAST));
        assertInstanceOf(TankEnemy.class,  factory.create(EnemyType.TANK));
    }

    @Test
    void enemyMovesToCorrectPosition() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(100, 200);
        assertEquals(100.0, enemy.getX());
        assertEquals(200.0, enemy.getY());
    }

    @Test
    void enemyStartsAlive() {
        Enemy enemy = new TankEnemy();
        assertTrue(enemy.isAlive());
        assertFalse(enemy.isDead());
    }

    @Test
    void enemyTypeStringsAreCorrect() {
        assertEquals("weak",   new WeakEnemy().getType());
        assertEquals("medium", new FastEnemy().getType());
        assertEquals("strong", new TankEnemy().getType());
    }
}


