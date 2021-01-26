package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.Renderer.VisibleEntity;
import com.tza.phantasia.Sound.SoundMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tza.phantasia.Phantasia.getRenderer;
import static com.tza.phantasia.Utilities.ResourceUtils.getResource;

public class World{
    public enum Collision {
        COLLTOP,
        COLLBOTTOM,
        COLLLEFT,
        COLLRIGHT
    }

    private VisibleEntity visibleEntity = getRenderer().addVisibleEntity();
    private BufferedImage collisionMap;
    private final SoundMap soundMap;
    private final List<MapEntity> entities = new ArrayList<>();
    public final String resourceName;

    public final int STARTING_X;
    public final int STARTING_Y;

    public World(String resourceName1, JSONObject object) throws IOException, ParseException {
        resourceName = resourceName1;
        if (Objects.isNull(object)) {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(
                    getResource(String.format("maps/%s/properties.json", resourceName))).openStream());
            object = (JSONObject) (new JSONParser()).parse(streamReader);
        }
        visibleEntity.setResourceName(String.format("maps/%s/map.png", resourceName)).setScale(1);
        try {
            collisionMap = ImageIO.read(Objects.requireNonNull(getResource(String.format("maps/%s/map.col", resourceName))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        soundMap = new SoundMap(resourceName, (JSONObject) object.get("sound_samples"));
        STARTING_X = Integer.parseInt(String.valueOf(object.get("starting_x")));
        STARTING_Y = Integer.parseInt(String.valueOf(object.get("starting_y")));

        JSONArray array = (JSONArray) object.get("entities");
        if (Objects.nonNull(array)) array.forEach((entity) -> entities.add(new MapEntity((JSONObject) entity)));
    }

    public World(String resourceName1) throws IOException, ParseException {
        this(resourceName1, null);
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

    public CollisionCheckInterface getCollision() {
        return this::getCollision;
    }

    public VisibleEntity getVisibleEntity() {
        return visibleEntity;
    }

    public SoundMap getSoundMap() {
        return soundMap;
    }

    public void setVisibleEntity(VisibleEntity visibleEntity) {
        this.visibleEntity = visibleEntity;
    }

}
