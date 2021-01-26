package com.tza.phantasia.Renderer;

import com.tza.phantasia.Renderer.SwingRenderer.SwingImageRenderable;
import com.tza.phantasia.Renderer.SwingRenderer.SwingTextRenderable;

import java.awt.*;
import java.util.Objects;

import static com.tza.phantasia.Phantasia.getRenderer;

@SuppressWarnings("UnusedReturnValue")
public class VisibleEntity {
    private String resourceName = "entities/empty.bmp";
    private int x_pos = 0;
    private int y_pos = 0;
    private double scale = 1;
    private final long renderId;
    private boolean camerable = true;
    private String string = null;

    public VisibleEntity(long rid) {
        renderId = rid;
    }

    public Renderable getRenderable() {
        Renderable renderable;
        if (Objects.nonNull(string)) {
            renderable = new SwingTextRenderable();
            renderable.setString(string);
        }
        else renderable = new SwingImageRenderable(resourceName);
        renderable.setScale(scale);
        renderable.setY_pos(y_pos);
        renderable.setX_pos(x_pos);
        renderable.setCamerable(camerable);
        return renderable;
    }

    public void remove() {
        getRenderer().internalRenderer.getRenderList().remove(renderId);
    }

    private void update() {
        EventQueue.invokeLater(() -> {
            getRenderer().internalRenderer.getRenderList().remove(renderId);
            getRenderer().internalRenderer.getRenderList().put(renderId, getRenderable());
        });
    }

    public VisibleEntity setResourceName(String resourceName) {
        this.resourceName = resourceName;
        update();
        return this;
    }

    public VisibleEntity setCoords(int x, int y) {
        x_pos = x;
        y_pos = y;
        update();
        return this;
    }

    public VisibleEntity setX_pos(int x_pos) {
        this.x_pos = x_pos;
        update();
        return this;
    }

    public VisibleEntity setY_pos(int y_pos) {
        this.y_pos = y_pos;
        update();
        return this;
    }

    public VisibleEntity setScale(double scale) {
        this.scale = scale;
        update();
        return this;
    }

    public VisibleEntity setCamerable(boolean camerable1) {
        camerable = camerable1;
        update();
        return this;
    }

    public VisibleEntity setString(String s) {
        string = s;
        update();
        return this;
    }

    public String getResourceName() { return resourceName; }
    public int getX_pos() { return x_pos; }
    public int getY_pos() { return y_pos; }
    public double getScale() { return scale; }
}
