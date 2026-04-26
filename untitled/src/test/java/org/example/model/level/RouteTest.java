package org.example.model.level;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class RouteTest {

    @Test
    void routeHasCorrectSize() {
        Route route = new Route(List.of(
                new Point(0, 0),
                new Point(100, 0),
                new Point(100, 100)
        ));
        assertEquals(3, route.size());
    }

    @Test
    void spawnPointIsFirstPoint() {
        Route route = new Route(List.of(
                new Point(50, 50),
                new Point(200, 200)
        ));
        assertEquals(50, route.getSpawnPoint().x());
        assertEquals(50, route.getSpawnPoint().y());
    }

    @Test
    void basePointIsLastPoint() {
        Route route = new Route(List.of(
                new Point(0, 0),
                new Point(600, 400)
        ));
        assertEquals(600, route.getBasePoint().x());
    }

    @Test
    void isLastPointDetectsEnd() {
        Route route = new Route(List.of(
                new Point(0, 0),
                new Point(100, 0),
                new Point(200, 0)
        ));
        assertFalse(route.isLastPoint(1));
        assertTrue(route.isLastPoint(2));
    }

    @Test
    void routeThrowsWithLessThanTwoPoints() {
        assertThrows(IllegalArgumentException.class, () ->
                new Route(List.of(new Point(0, 0)))
        );
    }
}
