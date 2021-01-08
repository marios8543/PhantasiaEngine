package com.tza.phantasia.Renderer;

import com.tza.phantasia.Main;
import com.tza.phantasia.Renderer.SwingRenderer.SwingRenderable;

public class VisibleEntity {
    private String resourceName;
    private int x_pos = 0;
    private int y_pos = 0;
    private double scale = 1;
    private int renderId;
    private boolean camerable = true;

    public VisibleEntity(int rid) {
        renderId = rid;
    }

    public SwingRenderable getRenderable() {
        SwingRenderable renderable;
        try {
            renderable = new SwingRenderable(resourceName);
        }
        catch (NullPointerException e){
            renderable = new SwingRenderable();
        };
        renderable.setScale(scale);
        renderable.setY_pos(y_pos);
        renderable.setX_pos(x_pos);
        renderable.setCamerable(camerable);
        return renderable;
    }

    public void remove() {
        Main.getRenderer().internalRenderer.getRenderList().remove(renderId);
    }

    private void update() {
        Main.getRenderer().internalRenderer.getRenderList().remove(renderId);
        Main.getRenderer().internalRenderer.getRenderList().put(renderId, getRenderable());
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

    @Override
    protected void finalize() {
        remove();
    }

    public String getResourceName() { return resourceName; }
    public int getX_pos() { return x_pos; }
    public int getY_pos() { return y_pos; }
    public double getScale() { return scale; }
}
