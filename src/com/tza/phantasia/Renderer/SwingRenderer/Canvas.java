package com.tza.phantasia.Renderer.SwingRenderer;

import com.tza.phantasia.Renderer.Camera;
import com.tza.phantasia.Renderer.Renderable;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.util.Map;


public class Canvas extends JComponent {
    private final Map<Long, Renderable> renderableList;
    private final Camera camera;
    public Canvas(Map<Long, Renderable> renderables, Camera camera1) {
        renderableList = renderables;
        camera = camera1;
    }

    public synchronized void paintComponent(Graphics graphics) {
        renderableList.values().iterator().forEachRemaining((renderable -> {
            if (renderable.getClass().getSimpleName().equals("SwingTextRenderable")) {
                graphics.drawString(
                        renderable.getString(),
                        renderable.getX_pos()+(renderable.getCamerable() ? camera.getX_pos() : 0),
                        renderable.getY_pos()+(renderable.getCamerable() ? camera.getY_pos() : 0)
                );
            }
            else if (renderable.getClass().getSimpleName().equals("SwingImageRenderable")) {
                graphics.drawImage(
                        renderable.getImage(),
                        renderable.getX_pos()+(renderable.getCamerable() ? camera.getX_pos() : 0),
                        renderable.getY_pos()+(renderable.getCamerable() ? camera.getY_pos() : 0),
                        renderable.getWidth(),
                        renderable.getHeight(),
                        this
                );
            }
        }));
    }
}
