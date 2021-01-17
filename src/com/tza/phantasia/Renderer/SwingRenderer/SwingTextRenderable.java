package com.tza.phantasia.Renderer.SwingRenderer;

import com.tza.phantasia.Renderer.Renderable;

import java.awt.*;

public class SwingTextRenderable implements Renderable {
    private int x;
    private int y;
    private boolean camerable = true;
    private String string;

    @Override
    public void setX_pos(int x) {
        this.x = x;
    }

    @Override
    public void setY_pos(int y) {
        this.y = y;
    }

    @Override
    public void setScale(double s) {}

    @Override
    public void setCamerable(boolean camerable) {
        this.camerable = camerable;
    }

    public void setString(String s) {
        string = s;
    }

    @Override
    public boolean getCamerable() {
        return camerable;
    }

    @Override
    public int getX_pos() {
        return x;
    }

    @Override
    public int getY_pos() {
        return y;
    }

    @Override
    public int getWidth() {
        return string.length();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public String getString() {
        return string;
    }
}
