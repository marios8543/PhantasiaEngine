package com.tza.phantasia.Utilities;

import com.tza.phantasia.Entities.GenericEntity;
import com.tza.phantasia.Main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class SoundUtils {
    private static final Map<String, Double> durationCache = new HashMap<>();

    public static double calculatePanFromEntity(GenericEntity entity, int x) {
        int wX = entity.getX();
        double distance = x - wX;
        double pan = -(distance/Main.SOUND_FADE_DISTANCE);
        if (pan < 0) return -Math.max(pan, -1.0);
        return -Math.min(pan, 1.0);
    }

    public static double calculateVolumeFromEntity(GenericEntity entity, int x, int y) {
        int wX = entity.getX();
        int wY = entity.getY();
        double distance = Math.sqrt(Math.pow(wX - x ,2) + Math.pow(wY - y ,2));
        if (distance > Main.SOUND_FADE_DISTANCE) return 0;
        return -(100 * distance/Main.SOUND_FADE_DISTANCE)+100;
    }

    public static double getSoundLength(String resource) {
        if (durationCache.containsKey(resource)) return durationCache.get(resource);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream =
                    AudioSystem.getAudioInputStream(Objects.requireNonNull(Main.class.getClassLoader().getResource(resource)));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return 0;
        }
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate();
        durationCache.put(resource, durationInSeconds);
        return durationInSeconds;
    }
}
