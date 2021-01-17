package com.tza.phantasia.Renderer;

import com.tza.phantasia.Renderer.SwingRenderer.SwingImageRenderable;

import java.awt.*;

public interface Renderable {
    void setX_pos(int x);

    void setY_pos(int y);

    void setScale(double s);

    void setCamerable(boolean camerable);

    boolean getCamerable();

    int getX_pos();

    int getY_pos();

    int getWidth();

    int getHeight();

    Image getImage();

    String getString();

    void setString(String string);
}
