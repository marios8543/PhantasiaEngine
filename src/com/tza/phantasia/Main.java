package com.tza.phantasia;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, InterruptedException, ParseException, IOException {
        JSONObject object = new JSONObject();

        JSONObject config = new JSONObject();
        config.put("walk_interval", 3);
        config.put("walk_offset", 1);
        config.put("scroll_interval", 3);
        config.put("scroll_offset", 1);

        JSONObject minimap = new JSONObject();
        minimap.put("scale", 0.07);

        object.put("map", "default");
        object.put("config", config);
        object.put("minimap", minimap);

        new Phantasia(object);
    }
}
