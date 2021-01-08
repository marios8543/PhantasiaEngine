package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.World;

public interface CollisionCheck {
    World.Collision canMoveTo(int x, int y, Walker walker);
}
