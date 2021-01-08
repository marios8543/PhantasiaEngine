package com.tza.phantasia.Renderer;

public interface Renderable {
    void setX_pos(int x);

    void setY_pos(int y);

    void setScale(double s);

    void setCamerable(boolean camerable);

    int getX_pos();

    int getY_pos();

    int getWidth();

    int getHeight();
}
