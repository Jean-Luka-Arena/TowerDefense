package org.example.model.player;

import org.example.model.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void playerStartsWithCorrectMoney() {
        Player player = new Player(100);
        assertEquals(100, player.getMoney());
    }

    @Test
    void playerStartsWithZeroScore() {
        Player player = new Player(100);
        assertEquals(0, player.getScore());
    }

    @Test
    void playerCanAffordWhenEnoughMoney() {
        Player player = new Player(100);
        assertTrue(player.canAfford(50));
    }

    @Test
    void playerCannotAffordWhenNotEnoughMoney() {
        Player player = new Player(100);
        assertFalse(player.canAfford(150));
    }

    @Test
    void spendReducesMoney() {
        Player player = new Player(100);
        player.spend(40);
        assertEquals(60, player.getMoney());
    }

    @Test
    void spendThrowsWhenNotEnoughMoney() {
        Player player = new Player(100);
        assertThrows(IllegalStateException.class, () -> player.spend(150));
    }

    @Test
    void earnMoneyIncreasesMoney() {
        Player player = new Player(100);
        player.earnMoney(30);
        assertEquals(130, player.getMoney());
    }

    @Test
    void addScoreIncreasesScore() {
        Player player = new Player(100);
        player.addScore(20);
        assertEquals(20, player.getScore());
    }
}
