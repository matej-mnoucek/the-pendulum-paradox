package com.thependulumparadox.levels;

import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.BaseScreen;

public interface ILevelManager
{
    void addLevel(Scene scene, BaseScreen inGameScreen, BaseScreen gameOverScreen);
    boolean isCurrentLevelLast();
    Scene currentLevelScene();
    ViewState currentInGameViewState();
    ViewState currentGameOverViewState();
    boolean nextLevel();
    void reset();
}
