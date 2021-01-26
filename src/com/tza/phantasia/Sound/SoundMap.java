package com.tza.phantasia.Sound;

import com.tza.phantasia.Entities.GenericEntity;
import com.tza.phantasia.Entities.Walker;
import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tza.phantasia.Utilities.ResourceUtils.getResource;

public class SoundMap {
    public final String DEFAULT_SOUND;
    private BufferedImage soundMap;
    private final Map<Integer, String> soundBinds = new HashMap<>();

    public SoundMap(String resourceName, JSONObject object) {
        DEFAULT_SOUND = (String) object.getOrDefault("default", "sounds/defaultSound.wav");
        try {
            soundMap = ImageIO.read(Objects.requireNonNull(getResource(String.format("maps/%s/map.sound", resourceName))));
            object.forEach((k,v) -> soundBinds.put(Integer.parseInt((String) k), (String) v));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSampleFromCoords(int x, int y) {
        int color = soundMap.getRGB(x, y);
        return soundBinds.getOrDefault(color, DEFAULT_SOUND);
    }

    public SoundObserver addHandler(Walker walker) {
        return new SoundObserver(walker).setSampleAcquire(this::getSampleFromCoords);
    }

    public SoundObserver addHandler(Walker walker, GenericEntity pointOfReference) {
        return new SoundObserver(walker, pointOfReference).setSampleAcquire(this::getSampleFromCoords);
    }
}
