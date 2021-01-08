package com.tza.phantasia.Entities;

import com.tza.phantasia.Main;
import com.tza.phantasia.MapParser.CollisionCheck;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.VisibleEntity;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import com.tza.phantasia.Utilities.KeypressHelper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Walker extends GenericEntity{
    protected final List<KeypressHelper.KeyAction> actionList = new ArrayList<>();
    private boolean autoUpdate = true;
    private final List<MovementListenerInterface> externalMovementListeners = new ArrayList<>();
    private CollisionCheck collisionCheck;
    private final Timer timer = new Timer(Main.WALK_INTERVAL, e -> {
        actionList.forEach((walkAction -> {
            int tmpY = y_pos, tmpX = x_pos;
            switch (walkAction) {
                case MOVEUP :
                    tmpY -= Main.WALK_OFFSET;
                    break;
                case MOVEDOWN:
                    tmpY += Main.WALK_OFFSET;
                    break;
                case MOVELEFT:
                    tmpX -= Main.WALK_OFFSET;
                    break;
                case MOVERIGHT:
                    tmpX += Main.WALK_OFFSET;
                    break;
            }
            World.Collision collision = collisionCheck.canMoveTo(tmpX, tmpY, this);
            if (collision == null) {
                x_pos = tmpX;
                y_pos = tmpY;
                externalMovementListeners.forEach((listener) -> {
                    listener.positionUpdate(x_pos, y_pos);
                    listener.movementUpdate(walkAction);
                });
            }
            else {
                externalMovementListeners.forEach((listener) -> listener.collision(collision));
            }
        }));
        if (autoUpdate) update();
    });

    public Walker(String resourceName) {
        x_pos = Main.MIN_PLAYER_X;
        y_pos = Main.MIN_PLAYER_Y;
        visibleEntity = Main.getRenderer().addVisibleEntity();
        visibleEntity.setResourceName(resourceName);
        visibleEntity.setCoords(x_pos, y_pos);
    }

    public void startAction(KeypressHelper.KeyAction action) {
        if(!actionList.contains(action)) actionList.add(action);
        if(actionList.size() >= 1) timer.start();
        externalMovementListeners.forEach((listener) -> {
            listener.move(action);
        });
    }

    public void stopAction(KeypressHelper.KeyAction action) {
        actionList.remove(action);
        if(actionList.size() == 0) timer.stop();
        externalMovementListeners.forEach((listener) -> {
            listener.stop(action);
        });
    }

    public void stopAll() {
        actionList.clear();
        timer.stop();
    }

    public void addMovementListener(MovementListenerInterface controlInterface) {
        externalMovementListeners.add(controlInterface);
    }

    public void setAutoUpdate(boolean autoUpdate1) {
        autoUpdate = autoUpdate1;
    }

    public boolean isWalking() {
        return !actionList.isEmpty();
    }

    public boolean isWalking(KeypressHelper.KeyAction action) {
        return actionList.contains(action);
    }

    public VisibleEntity getVisibleEntity() { return  visibleEntity; }

    public void update() {
        visibleEntity.setCoords(x_pos, y_pos);
    }

    public void setCollisionCheck(CollisionCheck collisionCheck1) {
        collisionCheck = collisionCheck1;
    }
}
