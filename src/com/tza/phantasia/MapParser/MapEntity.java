package com.tza.phantasia.MapParser;

import com.tza.phantasia.Entities.GenericEntity;
import com.tza.phantasia.Utilities.ResourceUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static com.tza.phantasia.Phantasia.getMainPlayer;
import static com.tza.phantasia.Phantasia.getWorld;

public class MapEntity extends GenericEntity {
    public MapEntity(String resourceName) throws IOException, ParseException {
        getVisibleEntity().setResourceName(String.format("entities/%s/texture.png", resourceName));
        JSONObject object = (JSONObject)
                new JSONParser().parse(ResourceUtils.fileReader("entities/%s/properties.json"));
        initializeFromJson(object);
    }

    public MapEntity(JSONObject object) {
        getVisibleEntity().setResourceName((String) object.get("resource"));
        initializeFromJson(object);
    }

    private void setupSoundHandler(String sample) {
        getWorld().getSoundMap().addHandler(getMainPlayer(), this).setPersistentSample(sample);
    }

    private void initializeFromJson(JSONObject object) {
        setX((int) object.get("x"));
        setY((int) object.get("y"));
        if (object.containsKey("sound")) {
            JSONObject sound = (JSONObject) object.get("sound");
            setupSoundHandler((String) sound.get("sample"));
        }
    }
}
