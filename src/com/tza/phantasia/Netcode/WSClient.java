package com.tza.phantasia.Netcode;

import com.tza.phantasia.Utilities.GameControlInterface;
import com.tza.phantasia.Utilities.KeypressHelper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WSClient extends WebSocketClient {
    public String selfId = "";
    public final GameControlInterface mainControlInterface;

    public final Map<String, Player> playerList = new HashMap<>();
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
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
