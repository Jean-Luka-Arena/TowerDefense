package org.example.model.route;
import org.example.model.point.Point;

import java.util.List;

public class Route {

    private final List<Point> points;
    private final List<Point> towerSpots;

    public Route(List<Point> points, List<Point> towerSpots) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("la ruta debe tener al menos dos puntos");
        }
        if (towerSpots == null || towerSpots.size() < 2) {
            throw new IllegalArgumentException("No hay casilleros para torretas");
        }
        this.points = List.copyOf(points);
        this.towerSpots = List.copyOf(towerSpots);
    }

    public List<Point> getTowerSpots() {
        return towerSpots;
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int size() {
        return points.size();
    }

    public boolean isLastPoint(int index) {
        return index >= points.size() - 1;
    }

    public Point getSpawnPoint() {
        return points.getFirst();
    }

    public Point getBasePoint() {
        return points.getLast();
    }
}
