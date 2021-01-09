package com.tza.phantasia.Utilities;

public interface GameControlInterface {
    void move(KeypressHelper.KeyAction action);

    void stop(KeypressHelper.KeyAction action);

    void action1();

    void action2();

    void genericAction(char c);

}
