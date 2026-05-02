package org.example.model.level;

import org.example.model.point.Point;
import org.example.model.route.Route;
import java.util.List;

public class LevelData {
    private final Level level;
    private final Route route;
    private final int initialMoney;
    private final List<InitialTower> initialTowers;

    public LevelData(Level level, Route route, int initialMoney, List<InitialTower> initialTowers) {
        this.level = level;
        this.route = route;
        this.initialMoney = initialMoney;
        this.initialTowers = initialTowers;
    }

    public Level getLevel() { return level; }
    public Route getRoute() { return route; }
    public int getInitialMoney() { return initialMoney; }
    public List<InitialTower> getInitialTowers() { return initialTowers; }
}