package com.tza.phantasia.Entities;

import com.tza.phantasia.Renderer.VisibleEntity;

public class GenericEntity {
    protected int x_pos = 0;
    protected int y_pos = 0;
    protected VisibleEntity visibleEntity;

    public GenericEntity() {

    }

    public int getX_pos() {
        return x_pos;
    }

    public int getY_pos() {
        return y_pos;
    }

    public VisibleEntity getVisibleEntity() {
        return visibleEntity;
    }
}
