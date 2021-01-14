package com.tza.phantasia.Renderer;

import com.tza.phantasia.Main;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    private final Timer timer = new Timer(Main.SCROLL_INTERVAL, e -> actions.iterator().forEachRemaining((action -> {
        switch (action) {
            case SCROLLUP -> y_pos -= Main.SCROLL_OFFSET;
            case SCROLLDOWN -> y_pos += Main.SCROLL_OFFSET;
            case SCROLLLEFT -> x_pos -= Main.SCROLL_OFFSET;
            case SCROLLRIGHT -> x_pos += Main.SCROLL_OFFSET;
        }
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

    @Override
    public void scroll(ScrollAction action) {
        if (!actions.contains(action)) actions.add(action);
        if (actions.size() == 1) timer.start();
    }

    @Override
    public void stop(ScrollAction action) {
        actions.remove(action);
        if (actions.size() == 0) timer.stop();
    }

    @Override
    public void stopAll() {
        actions.clear();
        timer.stop();
    }

    public boolean isScrolling() {
        return !actions.isEmpty();
    }

    public boolean isScrolling(ScrollAction action) {
        return actions.contains(action);
    }
}
