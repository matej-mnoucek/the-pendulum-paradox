package com.thependulumparadox.multiplayer;


import com.badlogic.gdx.math.Vector2;
import com.thependulumparadox.control.IMoveCommands;
import com.thependulumparadox.observer.Event;

/**
 * Interface for Android network synchronization module
 */
public interface ISynchronization
{
    void startSignInIntent();
    void startQuickGame();
    boolean isRoomFull();
    boolean isSignedIn();
    void sendAction(String action, Vector2 position);
    void synchronize();
    void setInputHandler(IMoveCommands inputHandler);
    String getHighscore();
    void submitScore(int score);
    Event getStartMultiplayerEvent();
}
