package com.tza.phantasia.Utilities;

import com.tza.phantasia.MapParser.World;

public interface MovementListenerInterface extends GameControlInterface{
    void positionUpdate(int x, int y);
    void movementUpdate(KeypressHelper.KeyAction action);
    void collision(World.Collision collision);
}
