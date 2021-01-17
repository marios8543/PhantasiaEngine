package com.tza.phantasia.Renderer;

public interface ScrollInterface {
    void scroll(Camera.ScrollAction action);

    void stop(Camera.ScrollAction action);

    void stopAll();

    void getCoords(int x, int y);
}
