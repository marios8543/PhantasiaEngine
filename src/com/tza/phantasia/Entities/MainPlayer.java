package com.tza.phantasia.Entities;

import com.tza.phantasia.Main;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.Camera;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;

public class MainPlayer extends Walker {
    public MainPlayer(String resourceName, World world, int winWidth, int winHeight) {
        super(resourceName);
        setAutoUpdate(false);
        getVisibleEntity().setScale(Main.WORLD_SCALE);
        setCollisionCheck(world.getCollision());

        super.addMovementListener(new MovementListenerInterface() {
            @Override
            public void move(KeypressHelper.KeyAction action) {
                switch (action) {
                    case MOVELEFT -> Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLRIGHT);
                    case MOVERIGHT -> Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLLEFT);
                    case MOVEUP -> Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLDOWN);
                    case MOVEDOWN -> Main.getRenderer().getCamera().scroll(Camera.ScrollAction.SCROLLUP);
                }
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                switch (action) {
                    case MOVELEFT -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLRIGHT);
                    case MOVERIGHT -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLLEFT);
                    case MOVEUP -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLDOWN);
                    case MOVEDOWN -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLUP);
                }
            }

            @Override
            public void collision(World.Collision collision) {
                switch (collision) {
                    //case COLLLEFT -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLRIGHT);
                    //case COLLRIGHT -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLLEFT);
                    //case COLLTOP -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLDOWN);
                    //case COLLBOTTOM -> Main.getRenderer().getCamera().stop(Camera.ScrollAction.SCROLLUP);
                }
            }

            @Override
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {}
            @Override
            public void positionUpdate(int x, int y) {}
        });
    }
}