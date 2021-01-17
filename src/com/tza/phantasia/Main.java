package com.tza.phantasia;

import com.tza.phantasia.Entities.MainPlayer;
import com.tza.phantasia.Entities.MiniMap;
import com.tza.phantasia.MapParser.SoundMap;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.Camera;
import com.tza.phantasia.Renderer.Renderer;
import com.tza.phantasia.Renderer.ScrollInterface;
import com.tza.phantasia.Renderer.VisibleEntity;
import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import kuusisto.tinysound.TinySound;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class Main {
    public static final String SERVER_URL = System.getenv().getOrDefault("SERVER_URL", "127.0.0.1");
    public static int WALK_INTERVAL;
    public static int WALK_OFFSET;
    public static int SCROLL_INTERVAL;
    public static int SCROLL_OFFSET;
    public static int WORLD_SCALE;
    public static double MINIMAP_SCALE;
    public static int SOUND_FADE_DISTANCE;
    public static int STARTING_X;
    public static int STARTING_Y;

    private static final JFrame window = new JFrame();
    private static Renderer renderer;
    private static World map;
    private static MainPlayer player;
    private static MiniMap miniMap;
    private static SoundMap soundMap;

    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder(new URI("http://" + SERVER_URL + "/config")).build();
        String res = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        JSONObject config = (JSONObject) new JSONParser().parse(res);

        WALK_INTERVAL = Integer.parseInt(String.valueOf(config.get("WALK_INTERVAL")));
        WALK_OFFSET = Integer.parseInt(String.valueOf(config.get("WALK_OFFSET")));
        SCROLL_INTERVAL = Integer.parseInt(String.valueOf(config.get("SCROLL_INTERVAL")));
        SCROLL_OFFSET = Integer.parseInt(String.valueOf(config.get("SCROLL_OFFSET")));
        WORLD_SCALE = Integer.parseInt(String.valueOf(config.get("WORLD_SCALE")));
        MINIMAP_SCALE = (double) config.get("MINIMAP_SCALE");
        SOUND_FADE_DISTANCE = Integer.parseInt(String.valueOf(config.get("SOUND_FADE_DISTANCE")));
        STARTING_X = Integer.parseInt(String.valueOf(config.get("STARTING_X")));
        STARTING_Y = Integer.parseInt(String.valueOf(config.get("STARTING_Y")));

        String username = JOptionPane.showInputDialog("Enter username");
        if (Objects.isNull(username) || username.equals("")) System.exit(1);

        //GRAPHICS AND SOUND SETUP
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //window.setUndecorated(true);
        window.setResizable(false);
        WINDOW_HEIGHT = window.getHeight();
        WINDOW_WIDTH = window.getWidth();
        renderer = new Renderer(window);
        TinySound.init();

        //WORLD AND MAIN PLAYER SETUP
        map = new World("map.png");
        soundMap = new SoundMap("map.png");
        miniMap = new MiniMap(map);

        player = new MainPlayer("charSprite", map, window.getWidth(), window.getHeight());
        soundMap.addHandler(player);
        player.setX(STARTING_X);
        player.setY(STARTING_Y);
        player.update();
        getRenderer().getCamera().setX_pos(player.getX() + 450);
        getRenderer().getCamera().setY_pos(player.getY() - 250);
        VisibleEntity mainPlayerLabel = getRenderer().addVisibleEntity().setString(username);

        miniMap.visibleEntity.setResourceName("map.png.col").setCamerable(false);
        MiniMap.MiniMapItem miniPlayer = miniMap.addItem("point.png");

        WSClient client = new WSClient(new URI("ws://" + SERVER_URL + "/socket"),
                new GameControlInterface() {
            @Override
            public void move(KeypressHelper.KeyAction action) {
                player.startAction(action);
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                player.stopAction(action);
            }

            @Override
            public void action1(Object v) {
                System.out.println("CORRECTING POSITION");
                int[] coords = (int[]) v;
                player.setX(coords[0]);
                player.setY(coords[1]);

                getRenderer().getCamera().setX_pos(player.getX() + 450);
                getRenderer().getCamera().setY_pos(player.getY() - 250);
            }

            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        });
        client.connectBlocking();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        client.send(jsonObject.toJSONString());

        new KeypressHelper(renderer.getInternalRenderer().getWindow(), new GameControlInterface() {
            @Override
            public void move(KeypressHelper.KeyAction action) {
                JSONObject object = new JSONObject();
                object.put("START_ACTION", action.toString());
                client.send(object.toJSONString());
            }

            @Override
            public void stop(KeypressHelper.KeyAction action) {
                JSONObject object = new JSONObject();
                object.put("STOP_ACTION", action.toString());
                client.send(object.toJSONString());
            }

            @Override
            public void action1(Object v) { }

            @Override
            public void action2(Object v) { }

            @Override
            public void genericAction(char c) {
                switch (c) {
                    case 'i': renderer.getCamera().setY_pos(renderer.getCamera().getY_pos() + 10);
                    case 'k': renderer.getCamera().setY_pos(renderer.getCamera().getY_pos() - 10);
                    case 'j': renderer.getCamera().setX_pos(renderer.getCamera().getX_pos() - 10);
                    case 'l': renderer.getCamera().setX_pos(renderer.getCamera().getX_pos() + 10);
                }
            }
        }, false);

        player.addMovementListener(new MovementListenerInterface() {
            @Override
            public void positionUpdate(int x, int y) {
                miniPlayer.setX(x);
                miniPlayer.setY(y);

                mainPlayerLabel.setX_pos(x);
                mainPlayerLabel.setY_pos(y - 30);
            }

            @Override
            public void movementUpdate(KeypressHelper.KeyAction action) {}
            @Override
            public void collision(World.Collision collision) {
                switch (collision) {
                    case COLLTOP -> {
                        JSONObject object = new JSONObject();
                        object.put("STOP_ACTION", KeypressHelper.KeyAction.MOVEUP.toString());
                        client.send(object.toJSONString());
                    }
                    case COLLBOTTOM -> {
                        JSONObject object = new JSONObject();
                        object.put("STOP_ACTION", KeypressHelper.KeyAction.MOVEDOWN.toString());
                        client.send(object.toJSONString());
                    }
                    case COLLRIGHT -> {
                        JSONObject object = new JSONObject();
                        object.put("STOP_ACTION", KeypressHelper.KeyAction.MOVERIGHT.toString());
                        client.send(object.toJSONString());
                    }
                    case COLLLEFT -> {
                        JSONObject object = new JSONObject();
                        object.put("STOP_ACTION", KeypressHelper.KeyAction.MOVELEFT.toString());
                        client.send(object.toJSONString());
                    }
                }
            }
            @Override
            public void move(KeypressHelper.KeyAction action) {}
            @Override
            public void stop(KeypressHelper.KeyAction action) {}
            @Override
            public void action1(Object v) {}
            @Override
            public void action2(Object v) {}
            @Override
            public void genericAction(char c) {}
        });
    }

    public static Renderer getRenderer() {
        return renderer;
    }
    public static World getMap() {
        return map;
    }
    public static SoundMap getSoundMap() {
        return soundMap;
    }
    public static MainPlayer getMainPlayer() {
        return player;
    }
    public static MiniMap getMiniMap() {
        return miniMap;
    }
}
