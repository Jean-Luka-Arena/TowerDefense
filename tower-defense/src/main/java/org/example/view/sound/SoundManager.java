package org.example.view.sound;

import org.example.model.game.GameEventListener;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager implements GameEventListener {

    private final URL disparoUrl;
    private final URL misilUrl;
    private final URL fondoUrl;
    private final URL gameOverUrl;
    private final URL victoryUrl;

    private Clip fondoClip;

    public SoundManager() {
        disparoUrl  = getClass().getResource("/sounds/disparo_short.wav");
        misilUrl    = getClass().getResource("/sounds/misil_short.wav");
        fondoUrl    = getClass().getResource("/sounds/fondo.wav");
        gameOverUrl = getClass().getResource("/sounds/game_over.wav");
        victoryUrl  = getClass().getResource("/sounds/victory.wav");
    }

    private void playOnce(URL url) {
        if (url == null) return;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error reproduciendo sonido: " + e.getMessage());
        }
    }

    public void startMusic() {
        if (fondoUrl == null) return;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(fondoUrl);
            fondoClip = AudioSystem.getClip();
            fondoClip.open(ais);
            FloatControl volume = (FloatControl) fondoClip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-15.0f);
            fondoClip.loop(Clip.LOOP_CONTINUOUSLY);
            fondoClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error con música de fondo: " + e.getMessage());
        }
    }


    public void stopMusic() {
        if (fondoClip != null && fondoClip.isRunning()) {
            fondoClip.stop();
            fondoClip.close();
        }
    }

    @Override
    public void onShotFiredSimple()   { playOnce(disparoUrl); }

    @Override
    public void onShotFiredPowerful() { playOnce(misilUrl); }

    @Override
    public void onVictory()  { stopMusic(); playOnce(victoryUrl); }

    @Override
    public void onDefeat()   { stopMusic(); playOnce(gameOverUrl); }
}