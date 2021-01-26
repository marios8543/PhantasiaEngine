package com.tza.phantasia;

import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.MiniMap;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Netcode.WSClient;
import com.tza.phantasia.Renderer.Renderer;
import com.tza.phantasia.Utilities.Config;
import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import kuusisto.tinysound.TinySound;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Phantasia {
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    private static final JFrame window = new JFrame();
    private static Renderer renderer;
    private static Walker mainPlayer;
    private static MiniMap miniMap;
    private static World world;
    private static Config config;

    public Phantasia(JSONObject object) throws IOException, ParseException, URISyntaxException, InterruptedException {
        config = new Config((JSONObject) object.get("config"));
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        window.setResizable(false);
        WINDOW_HEIGHT = window.getHeight();
        WINDOW_WIDTH = window.getWidth();
        renderer = new Renderer(window);
        TinySound.init();

        loadWorld((String) object.get("map"));
        if(object.containsKey("online")) setupOnline((JSONObject) object.get("online"));
        else setupOffline();
        if (object.containsKey("mini_map")) {
            JSONObject minimap = (JSONObject) object.get("mini_map");
            miniMap = new MiniMap(world, (int) minimap.get("scale"));
        }
        setupMainPlayer();
    }

    public void loadWorld(String mapName) throws IOException, ParseException {
        world = new World(mapName);
    }

    private void setupMainPlayer() {
        mainPlayer = new Walker("default");
        getRenderer().getCamera().hookToWalker(mainPlayer);
        mainPlayer.setCollisionCheck(world.getCollision());
        world.getSoundMap().addHandler(mainPlayer);
        mainPlayer.setX(world.STARTING_X);
        mainPlayer.setY(world.STARTING_Y);
        mainPlayer.update();
        getRenderer().getCamera().setX_pos(mainPlayer.getX() + 550);
        getRenderer().getCamera().setY_pos(mainPlayer.getY() + 30);
    }

    private void setupOffline() {
        new KeypressHelper(renderer.getInternalRenderer().getWindow(), new GameControlInterface() {
            @Override
            public void move(KeypressHelper.KeyAction action) {
                mainPlayer.startAction(action);
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                mainPlayer.stopAction(action);
            }
            @Override
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        }, false);
    }

    private void setupOnline(JSONObject settings) throws URISyntaxException, InterruptedException {
        WSClient client = new WSClient(new URI("ws://" + settings.get("server_url") + "/socket"),
                new GameControlInterface() {
                    @Override
                    public void move(KeypressHelper.KeyAction action) {
                        mainPlayer.startAction(action);
                    }

                    @Override
                    public void stop(KeypressHelper.KeyAction action) {
                        mainPlayer.stopAction(action);
                    }

                    @Override
                    public void action1(Object v) {
                        int[] coords = (int[]) v;
                        mainPlayer.setX(coords[0]);
                        mainPlayer.setY(coords[1]);
                        getRenderer().getCamera().setX_pos(mainPlayer.getX() + 450);
                        getRenderer().getCamera().setY_pos(mainPlayer.getY() - 250);
                    }

                    @Override
                    public void action2(Object v) {}
                    @Override
                    public void genericAction(char c) {}
                });
        client.connectBlocking();
    }

    public static JFrame getWindow() {
        return window;
    }

    public static Renderer getRenderer() {
        return renderer;
    }

    public static World getWorld() {
        return world;
    }

    public static Walker getMainPlayer() {
        return mainPlayer;
    }

    public static MiniMap getMiniMap() {
        return miniMap;
    }

    public static Config getConfig() {
        return config;
    }
}
