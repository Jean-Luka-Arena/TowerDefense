package org.example.model.level;
import java.util.List;

public class Route {

    private final List<Point> points;

    public Route(List<Point> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("la ruta debe tener al menos dos puntos");
        }
        this.points = List.copyOf(points);
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
