package com.thependulumparadox.multiplayer;



public interface ISynchronization
{

    public void startSignInIntent();

    public void startQuickGame();

    public void sendAction(String action);

    public void handleActions();
}
