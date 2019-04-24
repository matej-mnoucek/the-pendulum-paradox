package com.thependulumparadox.multiplayer;


import com.thependulumparadox.control.IMoveCommands;

public interface ISynchronization
{
    void startSignInIntent();
    void startQuickGame();
    boolean isRoomFull();
    void sendAction(String action);
    void synchronize();
    void setInputHandler(IMoveCommands inputHandler);
    String getHighscore();
    void submitScore(int score);
}
