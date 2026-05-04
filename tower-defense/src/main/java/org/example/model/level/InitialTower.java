package org.example.model.level;

import org.example.model.point.Point;

public class InitialTower {
    private final String type;
    private final Point slot;

    public InitialTower(String type, Point slot) {
        this.type = type;
        this.slot = slot;
    }

    public String getType() { return type; }
    public Point getSlot() { return slot; }
}