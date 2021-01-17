package com.tza.phantasia.Entities;

import com.tza.phantasia.Main;
import com.tza.phantasia.Renderer.VisibleEntity;

public class GenericEntity {
    private int x_pos = 0;
    private int y_pos = 0;
    private final VisibleEntity visibleEntity = Main.getRenderer().addVisibleEntity();
    private boolean autoUpdate = true;

    public VisibleEntity getVisibleEntity() {
        return visibleEntity;
    }

    public int getX() {
        return x_pos;
    }

    public int getY() {
        return y_pos;
    }

    public void update() {
        visibleEntity.setCoords(x_pos, y_pos);
    }

    public void setX(int x) {
        x_pos = x;
        if (autoUpdate) update();
    }

    public void setY(int y) {
        y_pos = y;
        if (autoUpdate) update();
    }

    public void setAutoUpdate(boolean autoUpdate1) {
        autoUpdate = autoUpdate1;
    }

    public void remove() {
        visibleEntity.remove();
    }
}
