package com.tza.phantasia.Sound;

import com.tza.phantasia.Entities.GenericEntity;
import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import javax.swing.*;
import java.util.Objects;

import static com.tza.phantasia.Phantasia.getWorld;

public class SoundObserver {
    private String currentResource = getWorld().getSoundMap().DEFAULT_SOUND;
    private Sound sample = TinySound.loadSound(currentResource);
    private final Walker walker;
    private double volume = 1;
    private double pan = 0;
    private boolean playing = false;
    private SampleAcquireInterface getSample = null;
    private final Timer timer = new Timer( (int)(SoundUtils.getSoundLength(currentResource) * 1000), e -> sample.play(volume, pan));

    public SoundObserver(Walker w, GenericEntity pointOfReference) {
        walker = w;
        timer.stop();
        walker.addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                if (Objects.nonNull(getSample)) updateCoords(x, y);
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
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        });
    }

    public SoundObserver(Walker w) {
        this(w, null);
    }

    public SoundObserver setSampleAcquire(SampleAcquireInterface sampleAcquireInterface) {
        getSample = sampleAcquireInterface;
        return this;
    }

    public SoundObserver setPersistentSample(String persistentSample) {
        currentResource = persistentSample;
        sample = TinySound.loadSound(currentResource);
        return this;
    }

    private void playSound() {
        sample.play(volume, pan);
        timer.setDelay((int)(SoundUtils.getSoundLength(currentResource) * 1000));
        timer.start();
    }

    private void updateCoords(int x, int y) {
        String resource = getSample.getSampleFromCoords(x, y);
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
        }
    }

    private void stopSound() {
        if (!walker.isWalking()) {
            timer.stop();
            playing = false;
        }
    }
}
