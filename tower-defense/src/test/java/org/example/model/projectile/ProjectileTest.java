package org.example.model.projectile;

import org.example.model.enemy.Enemy;
import org.example.model.enemy.WeakEnemy;
import org.example.model.enemy.FastEnemy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {

    @Test
    void projectileIsActiveOnCreation() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(200, 200);
        Projectile p = new Projectile(0, 0, enemy, 1, "simple");
        assertTrue(p.isActive());
    }

    @Test
    void projectileDeactivatesWhenTargetDies() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(200, 200);
        Projectile p = new Projectile(0, 0, enemy, 1, "simple");
        enemy.takeDamage(10); // matar al enemigo antes del impacto
        p.update(0.016);
        assertFalse(p.isActive());
    }

    @Test
    void projectileDamagesEnemyOnImpact() {
        Enemy enemy = new WeakEnemy(); // 1 de vida
        enemy.moveTo(1, 0);
        Projectile p = new Projectile(0, 0, enemy, 1, "simple");
        p.update(1.0); // delta grande para que alcance al instante
        assertTrue(enemy.isDead());
    }

    @Test
    void projectileMovesTowardsTarget() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(500, 0);
        Projectile p = new Projectile(0, 0, enemy, 1, "simple");
        p.update(0.016);
        assertTrue(p.getX() > 0);
        assertEquals(0.0, p.getY(), 0.001); // se mueve solo en X
    }

    @Test
    void projectileHasCorrectDamage() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(100, 100);
        Projectile p = new Projectile(0, 0, enemy, 5, "powerful");
        assertEquals(5, p.getDamage());
    }

    @Test
    void projectileRecordsTowerType() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(100, 0);
        Projectile p = new Projectile(0, 0, enemy, 2, "fast");
        assertEquals("fast", p.getTowerType());
    }

    @Test
    void projectileDoesNothingIfAlreadyInactive() {
        Enemy enemy = new WeakEnemy();
        enemy.moveTo(1, 0);
        Projectile p = new Projectile(0, 0, enemy, 1, "simple");
        p.update(1.0); // impacta y se desactiva
        assertFalse(p.isActive());
        // segunda llamada a update no lanza excepcioon
        assertDoesNotThrow(() -> p.update(1.0));
    }

    @Test
    void projectileKillsFastEnemyWithSufficientDamage() {
        Enemy enemy = new FastEnemy(); // 2 de vida
        enemy.moveTo(1, 0);
        Projectile p = new Projectile(0, 0, enemy, 2, "powerful");
        p.update(1.0);
        assertTrue(enemy.isDead());
    }
}

