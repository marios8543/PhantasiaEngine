package com.tza.phantasia.Renderer.SwingRenderer;

import com.tza.phantasia.Renderer.Camera;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.util.Map;


public class Canvas extends JComponent {
    private final Map<Integer, SwingRenderable> renderableList;
    private final Camera camera;
    public Canvas(Map<Integer, SwingRenderable> renderables, Camera camera1) {
        renderableList = renderables;
        camera = camera1;
    }

    public synchronized void paintComponent(Graphics graphics) {
        renderableList.values().iterator().forEachRemaining((swingRenderable -> {
            graphics.drawImage(
                    swingRenderable.getImage(),
                    swingRenderable.getX_pos()+(swingRenderable.getCamerable() ? camera.getX_pos() : 0),
                    swingRenderable.getY_pos()+(swingRenderable.getCamerable() ? camera.getY_pos() : 0),
                    swingRenderable.getWidth(),
                    swingRenderable.getHeight(),
                    this
            );
        }));
    }
}
