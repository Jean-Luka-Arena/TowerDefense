package org.example.view.sound;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private final AudioClip disparoClip;
    private final AudioClip misilClip;
    private final AudioClip fondoClip;

    public SoundManager() {
        disparoClip = new AudioClip(getClass().getResource("/sounds/disparo_short.mp3").toExternalForm());
        misilClip   = new AudioClip(getClass().getResource("/sounds/misil_short.mp3").toExternalForm());
        fondoClip   = new AudioClip(getClass().getResource("/sounds/fondo.mp3").toExternalForm());

        disparoClip.setVolume(0.6);
        misilClip.setVolume(0.6);
        fondoClip.setVolume(0.15);
        fondoClip.setCycleCount(AudioClip.INDEFINITE);
    }

    public void playDisparo() { disparoClip.play(); }
    public void playMisil()   { misilClip.play(); }
    public void startMusic()  { fondoClip.play(); }
    public void stopMusic()   { fondoClip.stop(); }
}