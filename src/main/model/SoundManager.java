package main.model;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NotePad.by on 19.05.2017.
 */
public class SoundManager {

    private static final String SHOOT_FILE = "src/audio/shoot.wav";
    private static final String SHIFT_BACK_FILE = "src/audio/shiftback.wav";
    private static final String LOSE_FILE = "src/audio/fail.wav";



    private static void playFile(String fileName) {
        try {
            InputStream in = new FileInputStream(fileName);
            AudioStream stream = new AudioStream(in);
            AudioPlayer.player.start(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playShootSound() {
//        new Thread(() -> playFile(SHOOT_FILE)).start();
        playFile(SHOOT_FILE);
    }

    public static void playShiftBackSound() {
        playFile(SHIFT_BACK_FILE);
    }

    public static void playLoseSound() {
        playFile(LOSE_FILE);
    }
}
