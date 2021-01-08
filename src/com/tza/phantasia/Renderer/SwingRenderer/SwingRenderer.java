package com.tza.phantasia.Renderer.SwingRenderer;

import com.tza.phantasia.Renderer.Camera;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class SwingRenderer{
    private JFrame window;
    private final Map<Integer, SwingRenderable> renderItems = new HashMap<>();
    private final GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final int refreshRate = (screen.getDisplayMode().getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN) ?
            Integer.parseInt(System.getenv("FPS") == null ? "60" : System.getenv("FPS")) :
            screen.getDisplayMode().getRefreshRate();
    private final long ms = (long) ((1. / ((double) refreshRate)) * 1000);
    private final Thread renderThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                window.repaint();
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public SwingRenderer(JFrame window, Camera camera) {
        this.window = window;
        window.getContentPane().add(new Canvas(renderItems, camera));
        window.setVisible(true);
        window.setSize(screen.getDisplayMode().getWidth()-50, screen.getDisplayMode().getHeight()-50);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Map<Integer, SwingRenderable> getRenderList() {
        return renderItems;
    }

    public void start() {
        renderThread.start();
    }

    public JFrame getWindow() {
        return window;
    }
}
