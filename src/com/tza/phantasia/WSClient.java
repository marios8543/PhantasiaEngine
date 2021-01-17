package com.tza.phantasia;

import com.tza.phantasia.Entities.MiniMap;
import com.tza.phantasia.Entities.Walker;
import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.VisibleEntity;
import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import com.tza.phantasia.Utilities.MovementListenerInterface;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WSClient extends WebSocketClient {
    public String selfId = "";
    public GameControlInterface mainControlInterface;

    class Player extends Walker {
        public final String username;
        private final VisibleEntity nameTag;

        public Player(String name) {
            super("charSprite");
            username = name;
            nameTag = Main.getRenderer().addVisibleEntity().setString(username);
            Main.getSoundMap().addHandler(Main.getMainPlayer(), this);
            MiniMap.MiniMapItem miniPlayer = Main.getMiniMap().addItem("point.png");
            super.setCollisionCheck(Main.getMap().getCollision());
            addMovementListener(new MovementListenerInterface() {
                @Override
                public void positionUpdate(int x, int y) {
                    miniPlayer.setX(x);
                    miniPlayer.setY(y);

                    nameTag.setX_pos(x);
                    nameTag.setY_pos(y - 30);
                }
                @Override
                public void movementUpdate(KeypressHelper.KeyAction action) {}
                @Override
                public void collision(World.Collision collision) {}
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
            setX(Main.STARTING_X);
            setY(Main.STARTING_Y);
        }
    }

    public Map<String, Player> playerList = new HashMap<>();
    public WSClient(URI uri, GameControlInterface mainControlInterface1) {
        super(uri);
        mainControlInterface = mainControlInterface1;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {}

    @Override
    public void onMessage(String s) {
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(s);
            if (selfId.equals("")) {
                selfId = (String) object.get("id");
                return;
            }
            JSONObject data = (JSONObject) object.get("d");
            switch ((String) object.get("t")) {
                case "p_add" -> {
                    if (data.get("id").equals(selfId)) return;
                    playerList.put((String) data.get("id"), new Player((String) data.get("name")));
                }
                case "p_rem" -> {
                    playerList.get(data.get("id")).remove();
                    playerList.remove(data.get("id"));
                }
                case "mo_ev" -> {
                    if (data.get("id").equals(selfId)) {
                        mainControlInterface.move(KeypressHelper.KeyAction.valueOf((String) data.get("a")));
                    }
                    else {
                        playerList.get(data.get("id")).startAction(KeypressHelper.KeyAction.valueOf((String) data.get("a")));
                    }
                }
                case "st_ev" -> {
                    if (data.get("id").equals(selfId)) {
                        mainControlInterface.stop(KeypressHelper.KeyAction.valueOf((String) data.get("a")));
                        mainControlInterface.action1(new int[]{(int) data.get("x"), (int) data.get("y")});
                    }
                    else {
                        playerList.get(data.get("id")).stopAction(KeypressHelper.KeyAction.valueOf((String) data.get("a")));
                        if (!playerList.get(data.get("id")).isWalking()) {
                            playerList.get(data.get("id")).setX((int) data.get("x"));
                            playerList.get(data.get("id")).setY((int) data.get("y"));
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        if (!b) {
            this.connect();
        }
    }

    @Override
    public void onError(Exception e) {}
}
