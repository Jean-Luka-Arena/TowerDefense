package org.example.model.level;

import org.example.model.enemy.EnemyChance;
import org.example.model.enemy.EnemyType;

import java.util.List;

public class Levels {

    public static final Level LEVEL_1 = new Level(List.of(
            new EnemyChance(EnemyType.WEAK, 0.8),
            new EnemyChance(EnemyType.FAST, 0.2)
    ),5,1);

    public static final Level LEVEL_2 = new Level(List.of(
            new EnemyChance(EnemyType.WEAK, 0.5),
            new EnemyChance(EnemyType.FAST, 0.3),
            new EnemyChance(EnemyType.TANK, 0.2)
    ),5,1);

    public static final Level LEVEL_3 = new Level(List.of(
            new EnemyChance(EnemyType.WEAK, 0.1),
            new EnemyChance(EnemyType.FAST, 0.4),
            new EnemyChance(EnemyType.TANK, 0.5)
    ),5,1);
}