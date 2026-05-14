package org.example.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.example.model.base.Base;
import org.example.model.enemy.Enemy;
import org.example.model.game.Game;
import org.example.model.projectile.Projectile;
import org.example.model.tower.Tower;

import java.util.Objects;

public class GameRenderer {

    private final GraphicsContext gc;

    private final Image baseSprite;
    // sprites enemigos
    private final Image weakSprite;
    private final Image fastSprite;
    private final Image tankSprite;

    // sprites torretas
    private final Image towerSimple;
    private final Image towerFast;
    private final Image towerPowerful0;
    private final Image towerPowerful1;

    // sprites proyectiles
    private final Image bulletFast;
    private final Image bulletPowerful;

    // tiles mapa
    private final Image grass;
    private final Image path;
    private final Image slot;
    private final Image bush1;
    private final Image bush2;

    private static final int FRAME_COLS = 3;
    private static final int FRAME_ROWS = 4;
    private static final int TILE_SIZE = 64;
    private static final int TOWER_SIZE = 48;
    private static final int BASE_SIZE = 48;
    private static final int BULLET_SIZE = 20;

    private double globalTime = 0;

    public GameRenderer(GraphicsContext gc) {
        this.gc = gc;

        baseSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_base/tower_round.png")));

        weakSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/zombie_weak_sheet.png")));
        fastSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/zombie_fast_sheet.png")));
        tankSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/zombie_tank_sheet.png")));

        towerSimple    = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/tower_simple.png")));
        towerFast      = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/tower_fast.png")));
        towerPowerful0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/tower_powerful_0.png")));
        towerPowerful1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/tower_powerful_1.png")));

        bulletFast     = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/bullet_fast.png")));
        bulletPowerful = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites_tower/bullet_powerful.png")));

        grass = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass.png")));
        path  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/greypath.png")));
        slot  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/towerSlot.png")));
        bush1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bush1.png")));
        bush2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bush2.png")));
    }


    private void drawBase(Game game) {
        Base base = game.getBase();
        double x = base.getX() - BASE_SIZE / 2.0;
        double y = base.getY() - BASE_SIZE;

        gc.drawImage(baseSprite, x, y, TILE_SIZE, TILE_SIZE);
    }

    private void drawEnemy(Enemy enemy) {
        Image sheet = switch (enemy.getType()) {
            case "weak"   -> weakSprite;
            case "medium" -> fastSprite;
            case "strong" -> tankSprite;
            default       -> weakSprite;
        };

        double frameW = sheet.getWidth() / FRAME_COLS;
        double frameH = sheet.getHeight() / FRAME_ROWS;

        double dx = enemy.getDx();
        double dy = enemy.getDy();

        int row;
        if (Math.abs(dx) > Math.abs(dy)) {
            row = dx > 0 ? 1 : 3;
        } else {
            row = dy > 0 ? 0 : 2;
        }

        double animSpeed = enemy.getType().equals("strong") ? 8.0 : 2.0;
        int col = (int)(enemy.getAnimTime() * animSpeed) % FRAME_COLS;

        double srcX = col * frameW;
        double srcY = row * frameH;
        double destX = enemy.getX() - frameW / 2;
        double destY = enemy.getY() - frameH / 2;

        gc.drawImage(sheet, srcX, srcY, frameW, frameH, destX, destY, frameW, frameH);
    }

    private void drawTower(Tower tower) {
        double cx = tower.getX();
        double cy = tower.getY();
        double half = TOWER_SIZE / 2.0;

        double angle = 0;
        Enemy target = tower.getCurrentTarget();
        if (target != null && target.isAlive()) {
            double dx = target.getX() - cx;
            double dy = target.getY() - cy;
            angle = Math.toDegrees(Math.atan2(dy, dx));
        }

        switch (tower.getType()) {
            case "simple" -> {
                gc.save();
                gc.translate(cx, cy);
                gc.rotate(angle);
                gc.drawImage(towerSimple, -half, -half, TOWER_SIZE, TOWER_SIZE);
                gc.restore();
            }
            case "fast" -> {
                gc.save();
                gc.translate(cx, cy);
                gc.rotate(angle+90);
                gc.drawImage(towerFast, -half, -half, TOWER_SIZE, TOWER_SIZE);
                gc.restore();
            }
            case "powerful" -> {
                Image frame;
                if (tower.getCurrentTarget() != null && tower.getCurrentTarget().isAlive()) {
                    frame = (int)(globalTime * 2) % 2 == 0 ? towerPowerful0 : towerPowerful1;
                } else {
                    frame = towerPowerful1;
                }
                gc.save();
                gc.translate(cx, cy);
                gc.rotate(angle+90);
                gc.drawImage(frame, -half, -half, TOWER_SIZE, TOWER_SIZE);
                gc.restore();
            }
        }
    }


    private void drawProjectile(Projectile p) {
        Image bullet = switch (p.getTowerType()) {
            case "powerful" -> bulletPowerful;
            default         -> bulletFast;
        };

        int size = p.getTowerType().equals("powerful") ? 64 : BULLET_SIZE;

        double dx = p.getDx();
        double dy = p.getDy();
        double angle = Math.toDegrees(Math.atan2(dy, dx)) + 90;

        gc.save();
        gc.translate(p.getX(), p.getY());
        gc.rotate(angle);
        gc.drawImage(bullet, -size / 2.0, -size / 2.0, size, size);
        gc.restore();
    }


    private void drawGrass() {
        for (int x = 0; x < 800; x += TILE_SIZE) {
            for (int y = 0; y < 520; y += TILE_SIZE) {
                gc.drawImage(grass, x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPath(Game game) {
        var route = game.getRoute();
        for (int i = 0; i < route.size() - 1; i++) {
            var p1 = route.getPoint(i);
            var p2 = route.getPoint(i + 1);

            if (p1.getX() == p2.getX()) {
                double yStart = Math.min(p1.getY(), p2.getY());
                double yEnd   = Math.max(p1.getY(), p2.getY());
                for (double y = yStart; y <= yEnd; y += TILE_SIZE) {
                    gc.drawImage(path, p1.getX() - TILE_SIZE / 2, y - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
                }
            } else {
                double xStart = Math.min(p1.getX(), p2.getX());
                double xEnd   = Math.max(p1.getX(), p2.getX());
                for (double x = xStart; x <= xEnd; x += TILE_SIZE) {
                    gc.drawImage(path, x - TILE_SIZE / 2, p1.getY() - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void drawSlots(Game game) {
        for (var p : game.getRoute().getTowerSpots()) {
            gc.drawImage(slot, p.getX() - TILE_SIZE / 2, p.getY() - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawDecor() {
        gc.drawImage(bush1, 30,  64,  TILE_SIZE, TILE_SIZE);
        gc.drawImage(bush2, 450, 120, TILE_SIZE, TILE_SIZE);
        gc.drawImage(bush2, 300, 30,  TILE_SIZE, TILE_SIZE);
        gc.drawImage(bush1, 256, 192, TILE_SIZE, TILE_SIZE);
        gc.drawImage(bush1, 150, 400, TILE_SIZE, TILE_SIZE);
        gc.drawImage(bush1, 700, 256, TILE_SIZE, TILE_SIZE);
    }

    public void render(Game game, double deltaTime) {
        globalTime += deltaTime;
        drawGrass();
        drawPath(game);
        drawSlots(game);
        drawDecor();

        drawBase(game);

        for (Tower t : game.getTowers()) {
            drawTower(t);
        }

        for (Enemy e : game.getEnemies()) {
            drawEnemy(e);
        }

        for (Projectile p : game.getProjectiles()) {
            drawProjectile(p);
        }
    }
}