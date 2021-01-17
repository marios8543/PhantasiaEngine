package com.tza.phantasia.Renderer.SwingRenderer;

import com.tza.phantasia.Main;
import com.tza.phantasia.Renderer.Renderable;

import javax.swing.ImageIcon;
import java.util.Objects;

public class SwingImageRenderable extends ImageIcon implements Renderable {
    private int x_pos;
    private int y_pos;
    private double scale;
    private boolean camerable;

    public SwingImageRenderable() {
        super();
    }

    public SwingImageRenderable(String resourceName) {
        super(Objects.requireNonNull(Main.class.getClassLoader().getResource(resourceName)));
    }

    public void setX_pos(int x) {
        this.x_pos = x;
    }

    public void setY_pos(int y) {
        this.y_pos = y;
    }

    public void setScale(double s) {
        this.scale = s;
    }

    public void setCamerable(boolean camerable) {
        this.camerable = camerable;
    }

    public int getX_pos() {
        return x_pos;
    }

    public int getY_pos() {
        return y_pos;
    }

    public int getWidth() {
        return (int)(this.getImage().getWidth(null) * scale);
    }

    public int getHeight() {
        return (int)(this.getImage().getHeight(null) * scale);
    }

    @Override
    public String getString() {
        return super.toString();
    }

    @Override
    public void setString(String string) {}

    public boolean getCamerable() { return camerable; }
}
