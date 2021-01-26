package com.tza.phantasia.Utilities;

import org.json.simple.JSONObject;

public class Config {
    private final JSONObject object;

    public final int WALK_OFFSET;
    public final int WALK_INTERVAL;
    public final int SCROLL_OFFSET;
    public final int SCROLL_INTERVAL;

    public Config(JSONObject object1) {
        object = object1;
        WALK_INTERVAL = (int) object.get("walk_interval");
        WALK_OFFSET = (int) object.get("walk_offset");
        SCROLL_INTERVAL = (int) object.get("scroll_interval");
        SCROLL_OFFSET = (int) object.get("scroll_offset");
    }

    public String get(String key) {
        return (String) object.get(key);
    }
}
