package com.tza.phantasia.Renderer;

import com.tza.phantasia.Renderer.SwingRenderer.SwingRenderer;

import javax.swing.*;

public class Renderer {
    private int visibleEntityIncrementor = 0;
    protected SwingRenderer internalRenderer;
    private final Camera camera = new Camera();

    public Renderer(JFrame window) {
        internalRenderer = new SwingRenderer(window, camera);
        internalRenderer.start();
    }

    public VisibleEntity addVisibleEntity() {
        return new VisibleEntity(visibleEntityIncrementor++);
    }

    public SwingRenderer getInternalRenderer() {
        return internalRenderer;
    }

    public Camera getCamera() { return camera; }
}
