package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.Walker;

public interface CollisionCheckInterface {
    World.Collision canMoveTo(int x, int y, Walker walker);
}
