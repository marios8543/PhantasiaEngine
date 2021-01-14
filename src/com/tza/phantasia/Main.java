package com.tza.phantasia;

import com.tza.phantasia.Entities.MainPlayer;
import com.tza.phantasia.Entities.MiniMap;
import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.SoundMap;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.Renderer;
import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import kuusisto.tinysound.TinySound;

import javax.swing.*;

public class Main {
    public static final int WALK_INTERVAL = 3;
    public static final int WALK_OFFSET = 1;
    public static final int SCROLL_INTERVAL = WALK_INTERVAL;
    public static final int SCROLL_OFFSET = WALK_OFFSET;
    public static final int WORLD_SCALE = 1;
    public static final double MINIMAP_SCALE = 0.07;
    public static final int SOUND_FADE_DISTANCE = 1500;

    public static final int STARTING_X = 180;
    public static final int STARTING_Y = 310;

    private static final JFrame window = new JFrame();
    private static final Renderer renderer = new Renderer(window);
    private static World map;
    private static MainPlayer player;
    private static MiniMap miniMap;
    private static SoundMap soundMap;

    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    public static int MAX_VIEW_X;
    public static int MAX_VIEW_Y;
    public static int MIN_VIEW_X;
    public static int MIN_VIEW_Y;

    public synchronized static void main(String[] args) {
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setResizable(false);
        WINDOW_HEIGHT = window.getHeight();
        WINDOW_WIDTH = window.getWidth();

        MIN_VIEW_X = (int)(WINDOW_WIDTH * 0.05);
        MIN_VIEW_Y = (int)(WINDOW_HEIGHT * 0.05);
        MAX_VIEW_X = (int)(WINDOW_WIDTH * 0.85);
        MAX_VIEW_Y = (int)(WINDOW_HEIGHT * 0.85);

        TinySound.init();
        map = new World("map.png");
        soundMap = new SoundMap("map.png");
        miniMap = new MiniMap(map);
        player = new MainPlayer("charSprite", map, window.getWidth(), window.getHeight());

        soundMap.addHandler(player);
        player.setX(STARTING_X);
        player.setY(STARTING_Y);
        player.update();
        getRenderer().getCamera().setX_pos((WINDOW_WIDTH - player.getVisibleEntity().getRenderable().getWidth()) / 3);
        getRenderer().getCamera().setY_pos((WINDOW_HEIGHT - player.getVisibleEntity().getRenderable().getHeight()) / 3);

        Walker player2 = new Walker("charSprite");
        player2.setAutoUpdate(true);
        player2.setX(850);
        player2.setY(1900);
        player2.setCollisionCheck(map.getCollision());
        MiniMap.MiniMapItem item = miniMap.addItem("point.png");
        player2.addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                item.setX(x);
                item.setY(y);
            }
            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {}
            @Override
            public void collision(World.Collision collision) {}
            @Override
            public void move(KeypressHelper.KeyAction action) {}
            @Override
            public void stop(KeypressHelper.KeyAction action) {}
            @Override
            public void action1() {}
            @Override
            public void action2() {}
            @Override
            public void genericAction(char c) {}
        });
        soundMap.addHandler(player2, player);

        new Thread(() -> {
            while (true) {
                player2.startAction(KeypressHelper.KeyAction.MOVEUP);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player2.stopAction(KeypressHelper.KeyAction.MOVEUP);
                player2.startAction(KeypressHelper.KeyAction.MOVERIGHT);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player2.stopAction(KeypressHelper.KeyAction.MOVERIGHT);
                player2.startAction(KeypressHelper.KeyAction.MOVEDOWN);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player2.stopAction(KeypressHelper.KeyAction.MOVEDOWN);
                player2.startAction(KeypressHelper.KeyAction.MOVELEFT);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player2.stopAction(KeypressHelper.KeyAction.MOVELEFT);
            }
        }).start();

        miniMap.visibleEntity.setResourceName("map.png.col").setCamerable(false);
        MiniMap.MiniMapItem miniPlayer = miniMap.addItem("point.png");
        player.addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                miniPlayer.setX(x);
                miniPlayer.setY(y);
            }

            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {}
            @Override
            public void collision(World.Collision collision) {}
            @Override
            public void move(KeypressHelper.KeyAction action) {}
            @Override
            public void stop(KeypressHelper.KeyAction action) {}
            @Override
            public void action1() {}
            @Override
            public void action2() {}
            @Override
            public void genericAction(char c) {}
        });

        new KeypressHelper(renderer.getInternalRenderer().getWindow(), new GameControlInterface() {
            @Override
            public void move(KeypressHelper.KeyAction action) {
                player.startAction(action);
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                player.stopAction(action);
            }

            @Override
            public void action1() { }

            @Override
            public void action2() { }

            @Override
            public void genericAction(char c) {
                switch (c) {
                    case 'i': renderer.getCamera().setY_pos(renderer.getCamera().getY_pos() + 10);
                    case 'k': renderer.getCamera().setY_pos(renderer.getCamera().getY_pos() - 10);
                    case 'j': renderer.getCamera().setX_pos(renderer.getCamera().getX_pos() - 10);
                    case 'l': renderer.getCamera().setX_pos(renderer.getCamera().getX_pos() + 10);
                }
            }
        }, false);
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}
