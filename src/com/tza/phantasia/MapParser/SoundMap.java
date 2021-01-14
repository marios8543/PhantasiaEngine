package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.GenericEntity;
import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.Main;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import com.tza.phantasia.Utilities.SoundUtils;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SoundMap {
    private class SoundObserver {
        private Sound sample = TinySound.loadSound(defaultSound);
        private String currentResource = defaultSound;
        private final Walker walker;
        private double volume = 1;
        private double pan = 0;
        private boolean playing = false;
        private final Timer timer = new Timer( (int)(SoundUtils.getSoundLength(currentResource) * 1000), e -> {
            sample.play(volume, pan);
        });

        public SoundObserver(Walker w, GenericEntity pointOfReference) {
            walker = w;
            timer.stop();
            walker.addMovementListener(new MovementListenerInterface() {
                @Override
                public void positionUpdate(int x, int y) {
                    updateCoords(x, y);
                    if (Objects.nonNull(pointOfReference)) {
                        volume = SoundUtils.calculateVolumeFromEntity(pointOfReference, x, y) / 100;
                        pan = SoundUtils.calculatePanFromEntity(pointOfReference, x);
                    }
                }

                @Override
                public void move(KeypressHelper.KeyAction action) {
                    startSound();
                }

                @Override
                public void stop(KeypressHelper.KeyAction action) {
                    stopSound();
                }

                @Override
                public void movementUpdate(KeypressHelper.KeyAction action) {}
                @Override
                public void collision(World.Collision collision) {}
                @Override
                public void action1() {}
                @Override
                public void action2() {}
                @Override
                public void genericAction(char c) {}
            });
        }

        public SoundObserver(Walker w) {
            this(w, null);
        }

        private void playSound() {
            sample.play(volume, pan);
            timer.setDelay((int)(SoundUtils.getSoundLength(currentResource) * 1000));
            timer.start();
        }

        private void updateCoords(int x, int y) {
            String resource = getSampleFromCoords(x, y);
            if (!resource.equals(currentResource)) {
                currentResource = resource;
                stopSound();
                sample = TinySound.loadSound(resource);
                timer.setDelay((int)(SoundUtils.getSoundLength(currentResource) * 1000));
                playSound();
            }
        }

        private void startSound() {
            if (!playing) {
                playSound();
                playing = true;
            };
        }

        private void stopSound() {
            if (!walker.isWalking()) {
                timer.stop();
                playing = false;
            }
        }
    }

    private BufferedImage soundMap;
    private final Map<Integer, String> soundBinds = new HashMap<>();
    private final Map<Integer, SoundObserver> observers = new HashMap<>();
    private static final String defaultSound = "defaultSound.wav";

    public SoundMap(String resourceName) {
        try {
            soundMap = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResource(resourceName + ".sound")));
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(
                    Main.class.getClassLoader().getResourceAsStream(resourceName + ".sound.json")));
            JSONObject object = (JSONObject) (new JSONParser()).parse(streamReader);
            object.forEach((k,v) -> {
                soundBinds.put(Integer.parseInt((String) k), (String) v);
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSampleFromCoords(int x, int y) {
        int color = soundMap.getRGB(x, y);
        return soundBinds.getOrDefault(color, defaultSound);
    }

    public void addHandler(Walker walker) {
        observers.put(walker.hashCode(), new SoundObserver(walker));
    }

    public void addHandler(Walker walker, Walker pointOfReference) {
        observers.put(walker.hashCode(), new SoundObserver(walker, pointOfReference));
    }
}
