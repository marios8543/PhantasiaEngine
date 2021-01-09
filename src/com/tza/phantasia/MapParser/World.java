package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.Main;
import com.tza.phantasia.Renderer.VisibleEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class World{
    public enum Collision {
        COLLTOP,
        COLLBOTTOM,
        COLLLEFT,
        COLLRIGHT
    }

    private VisibleEntity visibleEntity = Main.getRenderer().addVisibleEntity();
    private BufferedImage collisionMap;

    public World(String resourceName) {
        visibleEntity.setResourceName(resourceName).setScale(Main.WORLD_SCALE);
        try {
            collisionMap = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResource(resourceName + ".col")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCollision(int x, int y){
        try {
            return collisionMap.getRGB(x, y) != -1;
        }
        catch (ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    public Collision getCollision(int x, int y, Walker player) {
        Collision collision = null;
        int height = player.getVisibleEntity().getRenderable().getHeight();
        int width = player.getVisibleEntity().getRenderable().getWidth();
        int COLCHECKOFFSET = 5;
        if (checkCollision(x, y) && checkCollision(x, y+ COLCHECKOFFSET)) collision =  Collision.COLLLEFT;
        if (checkCollision(x, y+height) && checkCollision(x, y+height- COLCHECKOFFSET)) collision = Collision.COLLLEFT;
        if (checkCollision(x, y) && checkCollision(x+ COLCHECKOFFSET, y)) collision = Collision.COLLTOP;
        if (checkCollision(x, y) && checkCollision(x+width- COLCHECKOFFSET, y)) collision = Collision.COLLTOP;
        if (checkCollision(x+width, y) && checkCollision(x+width, y+ COLCHECKOFFSET)) collision = Collision.COLLRIGHT;
        if (checkCollision(x+width, y+height) && checkCollision(x+width, y+height- COLCHECKOFFSET)) collision = Collision.COLLRIGHT;
        if (checkCollision(x, y+height) && checkCollision(x+ COLCHECKOFFSET, y+height)) collision = Collision.COLLBOTTOM;
        if (checkCollision(x+width, y+height) && checkCollision(x+width- COLCHECKOFFSET, y+height)) collision =  Collision.COLLBOTTOM;

        return collision;
    }

    public CollisionCheck getCollision() {
        return this::getCollision;
    }

    public VisibleEntity getVisibleEntity() {
        return visibleEntity;
    }

    public void setVisibleEntity(VisibleEntity visibleEntity) {
        this.visibleEntity = visibleEntity;
    }
}
