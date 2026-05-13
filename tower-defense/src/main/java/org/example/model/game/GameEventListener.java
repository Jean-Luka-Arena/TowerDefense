package org.example.model.game;

public interface GameEventListener {
    void onShotFiredSimple();
    void onShotFiredPowerful();
    void onVictory();
    void onDefeat();
}