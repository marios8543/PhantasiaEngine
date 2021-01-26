package com.tza.phantasia.Renderer;

import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.tza.phantasia.Phantasia.getConfig;

public class Camera implements ScrollInterface {
    public enum ScrollAction {
        SCROLLUP,
        SCROLLDOWN,
        SCROLLLEFT,
        SCROLLRIGHT
    }

    private int x_pos = 1;
    private int y_pos = 1;
    private final List<ScrollAction> actions = new ArrayList<>();
    private final List<ScrollInterface> scrollListeners = new ArrayList<>();
    private final Timer timer = new Timer(getConfig().SCROLL_INTERVAL, e -> actions.iterator().forEachRemaining((action -> {
        singleScroll(action);
        scrollListeners.iterator().forEachRemaining(listener -> listener.getCoords(x_pos, y_pos));
    })));
    public int getX_pos() {
        return x_pos;
    }

    public void setX_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public int getY_pos() {
        return y_pos;
    }

    public void setY_pos(int y_pos) {
        this.y_pos = y_pos;
    }

    public void singleScroll(ScrollAction action) {
        switch (action) {
            case SCROLLUP -> y_pos -= getConfig().SCROLL_OFFSET;
            case SCROLLDOWN -> y_pos += getConfig().SCROLL_OFFSET;
            case SCROLLLEFT -> x_pos -= getConfig().SCROLL_OFFSET;
            case SCROLLRIGHT -> x_pos += getConfig().SCROLL_OFFSET;
        }
    }

    @Override
    public void scroll(ScrollAction action) {
        if (!actions.contains(action)) {
            actions.add(action);
            scrollListeners.iterator().forEachRemaining(listener -> listener.scroll(action));
        }
        if (actions.size() == 1) timer.start();
    }

    @Override
    public void stop(ScrollAction action) {
        actions.remove(action);
        scrollListeners.iterator().forEachRemaining(listener -> listener.stop(action));
        if (actions.size() == 0) timer.stop();
    }

    @Override
    public void stopAll() {
        actions.clear();
        timer.stop();
    }

    @Override
    public void getCoords(int x, int y) {}

    public boolean isScrolling() {
        return !actions.isEmpty();
    }

    public boolean isScrolling(ScrollAction action) {
        return actions.contains(action);
    }

    public void addScrollListener(ScrollInterface scrollInterface) {
        scrollListeners.add(scrollInterface);
    }

    public void hookToWalker(Walker walker) {
        walker.addMovementListener(new MovementListenerInterface() {
            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {
                switch (action) {
                    case MOVELEFT -> singleScroll(Camera.ScrollAction.SCROLLRIGHT);
                    case MOVERIGHT -> singleScroll(Camera.ScrollAction.SCROLLLEFT);
                    case MOVEUP -> singleScroll(Camera.ScrollAction.SCROLLDOWN);
                    case MOVEDOWN -> singleScroll(Camera.ScrollAction.SCROLLUP);
                }
            }

            @Override
            public void positionUpdate(int x, int y) {}
            @Override
            public void collision(World.Collision collision) {}
            @Override
            public void move(KeypressHelper.KeyAction action) {}
            @Override
            public void stop(KeypressHelper.KeyAction action) {}
            @Override
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        });
    }
}
