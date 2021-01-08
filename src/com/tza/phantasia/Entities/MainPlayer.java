package com.tza.phantasia.Entities;

import com.tza.phantasia.Main;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.Camera;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;

public class MainPlayer extends Walker {
    private int viewXpos = 0;
    private int viewYpos = 0;

    public MainPlayer(String resourceName, World world) {
        super(resourceName);
        setAutoUpdate(false);
        visibleEntity.setScale(Main.WORLD_SCALE);
        setCollisionCheck(world.getCollision());

        super.addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                if (viewXpos <= Main.MIN_PLAYER_X && isWalking(KeypressHelper.KeyAction.MOVELEFT))
                    Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLRIGHT);
                else if (viewXpos >= Main.MAX_PLAYER_X && isWalking(KeypressHelper.KeyAction.MOVERIGHT))
                    Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLLEFT);
                if (viewYpos <= Main.MIN_PLAYER_Y && isWalking(KeypressHelper.KeyAction.MOVEUP))
                    Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLDOWN);
                else if(viewYpos >= Main.MAX_PLAYER_Y && isWalking(KeypressHelper.KeyAction.MOVEDOWN))
                    Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLUP);
                visibleEntity.setCoords(x, y);
            }

            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {
                switch (action) {
                    case MOVEUP -> viewYpos = viewYpos - Main.WALK_OFFSET >= Main.MIN_PLAYER_Y ? viewYpos -= Main.WALK_OFFSET : Main.MIN_PLAYER_Y;
                    case MOVEDOWN -> viewYpos = viewYpos + Main.WALK_OFFSET <= Main.MAX_PLAYER_Y ? viewYpos += Main.WALK_OFFSET : Main.MAX_PLAYER_Y;
                    case MOVELEFT -> viewXpos = viewXpos - Main.WALK_OFFSET >= Main.MIN_PLAYER_X ? viewXpos -= Main.WALK_OFFSET : Main.MIN_PLAYER_X;
                    case MOVERIGHT -> viewXpos = viewXpos + Main.WALK_OFFSET <= Main.MAX_PLAYER_X ? viewXpos += Main.WALK_OFFSET : Main.MAX_PLAYER_X;
                }
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                Main.getRenderer().getCamera().stopAll();
            }

            @Override
            public void collision(World.Collision collision) {
                Main.getRenderer().getCamera().stopAll();
            }

            @Override
            public void action1() {}
            @Override
            public void action2() {}
            @Override
            public void genericAction(char c) {}
            @Override
            public void move(KeypressHelper.KeyAction action) {}
        });
    }

    public void setX(int x) {
        x_pos = x;
        viewXpos = Math.min(x, Main.MAX_PLAYER_X);
    }

    public void setY(int y) {
        y_pos = y;
        viewYpos = Math.min(y, Main.MAX_PLAYER_Y);
    }
}
