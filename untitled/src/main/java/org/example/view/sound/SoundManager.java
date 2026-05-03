package org.example.view.sound;

import javafx.scene.media.AudioClip;

import java.util.Objects;

public class SoundManager {

    private final AudioClip disparoClip;
    private final AudioClip misilClip;
    private final AudioClip fondoClip;
    private final AudioClip gameOverClip;
    private final AudioClip victoryClip;

    public SoundManager() {
        disparoClip  = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/disparo_short.mp3")).toExternalForm());
        misilClip    = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/misil_short.mp3")).toExternalForm());
        fondoClip    = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/fondo.mp3")).toExternalForm());
        gameOverClip = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/game_over.mp3")).toExternalForm());
        victoryClip  = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/victory.mp3")).toExternalForm());

        disparoClip.setVolume(0.6);
        misilClip.setVolume(0.6);
        fondoClip.setVolume(0.15);
        fondoClip.setCycleCount(AudioClip.INDEFINITE);
        gameOverClip.setVolume(0.7);
        victoryClip.setVolume(0.7);
    }

    public void playDisparo()  { disparoClip.play(); }
    public void playMisil()    { misilClip.play(); }
    public void startMusic()   { fondoClip.play(); }
    public void stopMusic()    { fondoClip.stop(); }
    public void playGameOver() { gameOverClip.play(); }
    public void playVictory()  { victoryClip.play(); }
}