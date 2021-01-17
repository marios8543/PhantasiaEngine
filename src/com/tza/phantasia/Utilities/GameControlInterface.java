package com.tza.phantasia.Utilities;

public interface GameControlInterface {
    void move(KeypressHelper.KeyAction action);

    void stop(KeypressHelper.KeyAction action);

    void action1(Object v);

    void action2(Object v);

    void genericAction(char c);

}
