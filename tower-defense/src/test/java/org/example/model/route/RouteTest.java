package org.example.model.route;

import org.example.model.point.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class RouteTest {

    private Route buildRoute(List<Point> points) {
        List<Point> slots = List.of(new Point(50, 0), new Point(150, 0));
        return new Route(points, slots);
    }

    @Test
    void routeHasCorrectSize() {
        Route route = buildRoute(List.of(
                new Point(0, 0),
                new Point(100, 0),
                new Point(100, 100)
        ));
        assertEquals(3, route.size());
    }

    @Test
    void spawnPointIsFirstPoint() {
        Route route = buildRoute(List.of(
                new Point(50, 50),
                new Point(200, 200)
        ));
        assertEquals(50.0, route.getSpawnPoint().getX());
        assertEquals(50.0, route.getSpawnPoint().getY());
    }

    @Test
    void basePointIsLastPoint() {
        Route route = buildRoute(List.of(
                new Point(0, 0),
                new Point(600, 400)
        ));
        assertEquals(600.0, route.getBasePoint().getX());
        assertEquals(400.0, route.getBasePoint().getY());
    }

    @Test
    void isLastPointDetectsEnd() {
        Route route = buildRoute(List.of(
                new Point(0, 0),
                new Point(100, 0),
                new Point(200, 0)
        ));
        assertFalse(route.isLastPoint(1));
        assertTrue(route.isLastPoint(2));
    }

    @Test
    void routeThrowsWithLessThanTwoPoints() {
        List<Point> slots = List.of(new Point(0, 0), new Point(50, 0));
        assertThrows(IllegalArgumentException.class, () ->
                new Route(List.of(new Point(0, 0)), slots)
        );
    }

    @Test
    void routeThrowsWithNullTowerSpots() {
        assertThrows(IllegalArgumentException.class, () ->
                new Route(List.of(new Point(0, 0), new Point(100, 0)), null)
        );
    }

    @Test
    void towerSpotsAreImmutable() {
        List<Point> mutableSlots = new java.util.ArrayList<>();
        mutableSlots.add(new Point(10, 10));
        mutableSlots.add(new Point(20, 20));
        Route route = new Route(
                List.of(new Point(0, 0), new Point(100, 100)),
                mutableSlots
        );
        mutableSlots.add(new Point(999, 999));
        assertEquals(2, route.getTowerSpots().size());
    }
}


