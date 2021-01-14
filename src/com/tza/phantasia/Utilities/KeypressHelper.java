package com.tza.phantasia.Utilities;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class KeypressHelper {
    public enum KeyAction {
        MOVEUP,
        MOVEDOWN,
        MOVELEFT,
        MOVERIGHT,
        ACTION1,
        ACTION2
    }

    private static final Map<Character, KeyAction> keyBinds = new HashMap<>();
    private static final boolean[] debounceArray = new boolean[6];

    public KeypressHelper(JFrame frame, GameControlInterface controlInterface, boolean debounce) {
        new KeypressHelper(frame, controlInterface, debounce, null);
    }

    public KeypressHelper(JFrame frame, GameControlInterface controlInterface, boolean debounce, List<KeyAction> keys) {
        Arrays.fill(debounceArray, false);
        List<KeyAction> keyActionList = Arrays.asList(KeyAction.values());

        if (Objects.isNull(keys)) {
            keyBinds.put('w', KeyAction.MOVEUP);
            keyBinds.put('s', KeyAction.MOVEDOWN);
            keyBinds.put('a', KeyAction.MOVELEFT);
            keyBinds.put('d', KeyAction.MOVERIGHT);
        }

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                KeyAction action = keyBinds.get(c);
                if(action != null) {
                    int index = keyActionList.indexOf(action);
                    if (debounceArray[index]) return;
                    debounceArray[index] = true;
                    if(action == KeyAction.MOVEUP ||
                            action == KeyAction.MOVEDOWN ||
                            action == KeyAction.MOVELEFT ||
                            action == KeyAction.MOVERIGHT)
                        controlInterface.move(action);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeyAction action = keyBinds.get(e.getKeyChar());
                try {
                    debounceArray[keyActionList.indexOf(action)] = false;
                }
                catch (ArrayIndexOutOfBoundsException ignored) {}

                controlInterface.stop(action);
                controlInterface.genericAction(e.getKeyChar());
            }
        });
    }
}
