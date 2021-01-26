package com.tza.phantasia.Entities;

import com.tza.phantasia.MapParser.CollisionCheckInterface;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import com.tza.phantasia.Utilities.KeypressHelper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static com.tza.phantasia.Phantasia.getConfig;

public class Walker extends GenericEntity{
    protected final List<KeypressHelper.KeyAction> actionList = new ArrayList<>();
    private final List<MovementListenerInterface> externalMovementListeners = new ArrayList<>();
    private CollisionCheckInterface collisionCheckInterface;
    private final String resourceName;
    private KeypressHelper.KeyAction lastAction;
    private final Timer timer = new Timer(getConfig().WALK_INTERVAL, e -> {
        try {
            actionList.forEach((walkAction -> {
                int tmpY = getY(), tmpX = getX();
                switch (walkAction) {
                    case MOVEUP -> tmpY -= getConfig().WALK_OFFSET;
                    case MOVEDOWN -> tmpY += getConfig().WALK_OFFSET;
                    case MOVELEFT -> tmpX -= getConfig().WALK_OFFSET;
                    case MOVERIGHT -> tmpX += getConfig().WALK_OFFSET;
                }
                World.Collision collision = collisionCheckInterface.canMoveTo(tmpX, tmpY, this);
                if (collision == null) {
                    setX(tmpX);
                    setY(tmpY);
                    update();
                    externalMovementListeners.forEach((listener) -> {
                        try {
                            listener.positionUpdate(getX(), getY());
                            listener.movementUpdate(walkAction);
                        }
                        catch (Exception exc) { exc.printStackTrace(); }
                    });
                }
                else {
                    externalMovementListeners.forEach((listener) -> listener.collision(collision));
                }
            }));
        }
        catch (ConcurrentModificationException ignored){}
    });

    public Walker(String resourceName1) {
        resourceName = String.format("charSprites/%s", resourceName1);
        getVisibleEntity().setResourceName(String.format("%s/stand-left.gif", resourceName));
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

    public boolean isWalking(KeypressHelper.KeyAction action) {
        return actionList.contains(action);
    }

    public boolean isWalking() {
        return !actionList.isEmpty();
    }

    private void applyMovementSprite(KeypressHelper.KeyAction action, boolean isStopped) {
        String prefix = isStopped ? "stand" : "walk";
        switch (action) {
            case MOVELEFT -> getVisibleEntity().setResourceName(String.format("%s/%s-left.gif", resourceName, prefix));
            case MOVEUP -> getVisibleEntity().setResourceName(String.format("%s/%s-up.gif", resourceName, prefix));
            case MOVEDOWN -> getVisibleEntity().setResourceName(String.format("%s/%s-down.gif", resourceName, prefix));
            case MOVERIGHT -> getVisibleEntity().setResourceName(String.format("%s/%s-right.gif", resourceName, prefix));
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

    public void setCollisionCheck(CollisionCheckInterface collisionCheckInterface1) {
        collisionCheckInterface = collisionCheckInterface1;
    }
}
