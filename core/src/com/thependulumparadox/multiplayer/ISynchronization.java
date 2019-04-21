package com.thependulumparadox.multiplayer;


import com.thependulumparadox.control.MoveCommands;

public interface ISynchronization
{

    public void startSignInIntent();

    public void startQuickGame();

    public boolean isRoomFull();

    public void sendAction(String action);

    public void handleActions();

    public void setInputHandler(MoveCommands inputHandler);
}
