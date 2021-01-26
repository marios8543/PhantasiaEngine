package com.tza.phantasia.Netcode;

import com.tza.phantasia.MapParser.MiniMap;
import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.VisibleEntity;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;

import static com.tza.phantasia.Phantasia.*;

public class Player extends Walker {
    public final String username;
    private final VisibleEntity nameTag;

    public Player(String name) {
        super("default");
        username = name;
        nameTag = getRenderer().addVisibleEntity().setString(username);
        getWorld().getSoundMap().addHandler(getMainPlayer(), this);
        MiniMap.MiniMapItem miniPlayer = getMiniMap().addItem("entities/point.png");
        setCollisionCheck(getWorld().getCollision());
        addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                miniPlayer.setX(x);
                miniPlayer.setY(y);

                nameTag.setX_pos(x);
                nameTag.setY_pos(y - 30);
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
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        });
        setX(getWorld().STARTING_X);
        setY(getWorld().STARTING_Y);
    }
}