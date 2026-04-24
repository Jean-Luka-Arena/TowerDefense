package org.example.model.projectile;

import org.example.model.enemy.Enemy;
import org.example.model.enemy.WeakEnemy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {

    @Test
    void projectileIsActiveOnCreation() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(200, 200);
        Projectile p = new Projectile(0, 0, enemy, 1);
        assertTrue(p.isActive());
    }

    @Test
    void projectileDeactivatesWhenTargetDies() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(200, 200);
        Projectile p = new Projectile(0, 0, enemy, 1);
        enemy.takeDamage(10);
        p.update(0.016);
        assertFalse(p.isActive());
    }

    @Test
    void projectileDamagesEnemyOnImpact() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(1, 0);
        Projectile p = new Projectile(0, 0, enemy, 1);
        p.update(1.0);
        assertTrue(enemy.isDead());
    }

    @Test
    void projectileMovesTowardsTarget() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(500, 0);
        Projectile p = new Projectile(0, 0, enemy, 1);
        p.update(0.016);
        assertTrue(p.getX() > 0);
    }
}