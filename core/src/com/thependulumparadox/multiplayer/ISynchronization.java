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
    boolean isUserSignedIn();
    void startQuickGame();
    boolean isGameInProgress();
    Event getStartMultiplayerEvent();
    Event getStopMultiplayerEvent();
    Event getPlayerDeathEvent();
    Event getUserLoggedInEvent();
    void setInputHandler(IMoveCommands inputHandler);
    void sendAction(String action, Vector2 position);
    void synchronize();
    String getHighscore();
    void submitScore(int score);
}
