ample.model.tower;

public class TowerFactory {

    public Tower create(String type) {
        return switch (type.toLowerCase()) {
            case "simple"   -> new SimpleTower();
            case "fast" -> new FastTower();
            case "powerful" -> new PowerfulTower();
            default -> throw new IllegalArgumentException("Unknown tower type: " + type);
        };
    }
}