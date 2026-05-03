package org.example.model.game;

import org.example.model.enemy.*;
import org.example.model.level.Level;
import org.example.model.level.LevelData;
import org.example.model.player.Player;
import org.example.model.point.Point;
import org.example.model.route.Route;
import org.example.model.tower.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//dejo el codigo comentado para facilitar la lectura/correccion del codigo (mas que nada en este archivo que es el de integracion)

/**
 * Test de integración: verifica la interacción entre Game, Tower, Enemy, Player y Route.
 * tesyeamos el flujo completo:
 * - colocar torretas
 * - spawnear enemigos
 * - update del juego
 * - recompensa al jugador
 * - condiciones de victoria/derrota
 */
public class GameIntegrationTest {

    private Route route;
    private Level level;
    private Game game;

    @BeforeEach
    void setUp() {
        // Ruta simple: spawn en (0,0), un waypoint en (50,0), base en (200,0)
        List<Point> routePoints = List.of(
                new Point(0, 0),
                new Point(50, 0),
                new Point(200, 0)
        );
        List<Point> towerSlots = List.of(
                new Point(50, 50),
                new Point(100, 50),
                new Point(150, 50)
        );
        route = new Route(routePoints, towerSlots);

        // Nivel vacío (sin enemies preagendados); los spawneamos manualmente en cada test
        level = new Level(List.of(), 0.0);

        game = new Game(route, level, 200);
    }

    // Integración: Player + Game

    @Test
    void gameStartsWithCorrectMoney() {
        assertEquals(200, game.getPlayer().getMoney());
    }

    @Test
    void gameStartsWithZeroScore() {
        assertEquals(0, game.getPlayer().getScore());
    }

    // Integracion: Game + Tower

    @Test
    void placingTowerInValidSlotAddsTower() {
        Point slot = new Point(50, 50);
        Tower tower = new SimpleTower();
        game.addTower(tower, slot);
        assertEquals(1, game.getTowers().size());
    }

    @Test
    void placingTowerInInvalidSlotThrows() {
        Point invalidSlot = new Point(999, 999);
        Tower tower = new SimpleTower();
        assertThrows(IllegalArgumentException.class, () -> game.addTower(tower, invalidSlot));
    }

    @Test
    void cannotPlaceTowerOnOccupiedSlot() {
        Point slot = new Point(50, 50);
        game.addTower(new SimpleTower(), slot);
        assertFalse(game.canPlaceTower(slot));
    }

    @Test
    void replacingTowerSwapsItInPlace() {
        Point slot = new Point(50, 50);
        game.addTower(new SimpleTower(), slot);

        Tower powerful = new PowerfulTower();
        game.placeOrReplaceTower(powerful, slot);

        assertEquals(1, game.getTowers().size());
        assertEquals("powerful", game.getTowers().get(0).getType());
    }

    // Integracion: Game + Enemy

    @Test
    void spawnedEnemyAppearsAtRouteStart() {
        game.spawnEnemy(new WeakEnemy());
        Enemy e = game.getEnemies().get(0);
        assertEquals(route.getSpawnPoint().getX(), e.getX());
        assertEquals(route.getSpawnPoint().getY(), e.getY());
    }

    @Test
    void enemyMovesAfterUpdate() {
        game.spawnEnemy(new WeakEnemy());
        Enemy enemy = game.getEnemies().get(0);
        int routeIndexInicial = enemy.getRouteIndex();

        game.update(1.0); // delta grande para asegurar movimiento

        // el enemigo avanzó al menos un punto de la ruta
        assertTrue(enemy.getRouteIndex() > routeIndexInicial || enemy.getX() > 0);
    }

    // Integracion: Tower + Enemy + Projectile

    @Test
    void towerShootsEnemyInRangeAfterUpdate() {
        // Colocar torreta en slot (50,50), radio 100
        Point slot = new Point(50, 50);
        game.addTower(new SimpleTower(), slot);
        game.getTowers().get(0).setPosition(50, 50);

        // Spawnear enemigo y moverlo al alcance de la torreta
        Enemy enemy = new WeakEnemy();
        game.spawnEnemy(enemy);
        enemy.moveTo(60, 50); // dentro del radio de 100 de la torreta

        game.update(0.016);

        // debe haberse generado al menos un proyectil
        assertFalse(game.getProjectiles().isEmpty());
    }

    // Integracion: Enemy muerto → Player recibe recompensa


    @Test
    void playerEarnsMoneyWhenEnemyDies() {
        game.spawnEnemy(new WeakEnemy()); // recompensa = 10
        Enemy enemy = game.getEnemies().get(0);
        enemy.takeDamage(999); // matar manualmente

        int moneyBefore = game.getPlayer().getMoney();
        game.update(0.016); // el game debe detectar que esta muerto y dar recompensa

        assertEquals(moneyBefore + 10, game.getPlayer().getMoney());
    }

    @Test
    void playerEarnsScoreWhenEnemyDies() {
        game.spawnEnemy(new WeakEnemy()); // score = 10
        game.getEnemies().get(0).takeDamage(999);

        game.update(0.016);

        assertEquals(10, game.getPlayer().getScore());
    }

    @Test
    void playerEarnsCorrectRewardForTankEnemy() {
        game.spawnEnemy(new TankEnemy()); // recompensa = 30
        game.getEnemies().get(0).takeDamage(999);
        int moneyBefore = game.getPlayer().getMoney();

        game.update(0.016);

        assertEquals(moneyBefore + 30, game.getPlayer().getMoney());
    }

    // Integracion: condiciones de fin de juego

    @Test
    void gameIsNotOverAtStart() {
        assertFalse(game.isGameOver());
    }

    @Test
    void gameIsOverWhenBaseIsDestroyed() {
        // Dañar la base directamente
        game.getBase().takeDamage(999);
        assertTrue(game.isGameOver());
    }

    @Test
    void gameIsWinWhenAllEnemiesDefeated() {
        // level sin enemies programados y lista vacía → victoria inmediata
        assertTrue(game.isWin());
    }

    @Test
    void gameIsNotWinWhileEnemiesAlive() {
        game.spawnEnemy(new WeakEnemy());
        assertFalse(game.isWin());
    }

    @Test
    void updateDoesNothingWhenGameIsOver() {
        game.getBase().takeDamage(999);
        assertTrue(game.isGameOver());

        // Spawnear un enemigo y verificar que no se procesa
        game.spawnEnemy(new WeakEnemy());
        double xBefore = game.getEnemies().get(0).getX();

        game.update(1.0); // gran delta

        // El enemigo no debería haberse movido
        assertEquals(xBefore, game.getEnemies().get(0).getX());
    }
}
