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
    private final String resourceName;
    private KeypressHelper.KeyAction lastAction;
    private final Timer timer = new Timer(Main.WALK_INTERVAL, e -> {
        actionList.forEach((walkAction -> {
            int tmpY = y_pos, tmpX = x_pos;
            switch (walkAction) {
                case MOVEUP -> tmpY -= Main.WALK_OFFSET;
                case MOVEDOWN -> tmpY += Main.WALK_OFFSET;
                case MOVELEFT -> tmpX -= Main.WALK_OFFSET;
                case MOVERIGHT -> tmpX += Main.WALK_OFFSET;
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

    public Walker(String resourceName1) {
        x_pos = Main.MIN_PLAYER_X;
        y_pos = Main.MIN_PLAYER_Y;
        resourceName = resourceName1;
        visibleEntity = Main.getRenderer().addVisibleEntity();
        visibleEntity.setResourceName(String.format("%s/stand-left.gif", resourceName1));
        visibleEntity.setCoords(x_pos, y_pos);
    }

    public void startAction(KeypressHelper.KeyAction action) {
        if(!actionList.contains(action)) actionList.add(action);
        setMovementSprite();
        lastAction = action;
        if(actionList.size() >= 1) timer.start();
        externalMovementListeners.forEach((listener) -> listener.move(action));
    }

    public void stopAction(KeypressHelper.KeyAction action) {
        actionList.remove(action);
        if(actionList.size() == 0) {
            timer.stop();
            setMovementSprite();
        }
        externalMovementListeners.forEach((listener) -> listener.stop(action));
    }

    public void addMovementListener(MovementListenerInterface controlInterface) {
        externalMovementListeners.add(controlInterface);
    }

    public void setAutoUpdate(boolean autoUpdate1) {
        autoUpdate = autoUpdate1;
    }

    public boolean isWalking(KeypressHelper.KeyAction action) {
        return actionList.contains(action);
    }

    public VisibleEntity getVisibleEntity() { return  visibleEntity; }

    private void applyMovementSprite(KeypressHelper.KeyAction action, boolean isStopped) {
        System.out.println("Applying gif");
        String prefix = isStopped ? "stand" : "walk";
        switch (action) {
            case MOVELEFT -> visibleEntity.setResourceName(String.format("%s/%s-left.gif", resourceName, prefix));
            case MOVEUP -> visibleEntity.setResourceName(String.format("%s/%s-up.gif", resourceName, prefix));
            case MOVEDOWN -> visibleEntity.setResourceName(String.format("%s/%s-down.gif", resourceName, prefix));
            case MOVERIGHT -> visibleEntity.setResourceName(String.format("%s/%s-right.gif", resourceName, prefix));
        }
    }

    public void setMovementSprite() {
        if (actionList.isEmpty()) {
            applyMovementSprite(lastAction, true);
        }
        else {
            actionList.iterator().forEachRemaining((action -> applyMovementSprite(action, false)));
            //applyMovementSprite(actionList.get(0), false);
        }
    }

    public void update() {
        visibleEntity.setCoords(x_pos, y_pos);
    }

    public void setCollisionCheck(CollisionCheck collisionCheck1) {
        collisionCheck = collisionCheck1;
    }
}
