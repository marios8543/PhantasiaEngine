package com.tza.phantasia;

import com.tza.phantasia.Entities.MainPlayer;
import com.tza.phantasia.Entities.MiniMap;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.Renderer;
import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;

public class Main {
    public static final int MIN_PLAYER_X = 100;
    public static final int MIN_PLAYER_Y = 50;
    public static final int MAX_PLAYER_X = 1550;
    public static final int MAX_PLAYER_Y = 700;
    public static final int WALK_INTERVAL = 2;
    public static final int WALK_OFFSET = 1;
    public static final int SCROLL_INTERVAL = 2;
    public static final int SCROLL_OFFSET = 1;
    public static final int WORLD_SCALE = 1;
    public static final double MINIMAP_SCALE = 0.07;

    private static JFrame window = new JFrame();
    private static final Renderer renderer = new Renderer(window);
    private static final World map = new World("map.png");
    private static MainPlayer player;
    private static MiniMap miniMap = new MiniMap(map);


    public synchronized static void main(String[] args) {
        renderer.addVisibleEntity().setResourceName("start.png").setCoords(0, 0);
        player = new MainPlayer("test.png", map);

        player.setX(MIN_PLAYER_X);
        player.setY(MIN_PLAYER_Y+300);
        player.update();

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
        });
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}
