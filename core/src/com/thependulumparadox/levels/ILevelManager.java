package com.thependulumparadox.levels;

import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.BaseScreen;

/**
 * Level manager handles switching between different levels (basically just list of them)
 * After last level is completed the manager switches to the first level again (endless play)
 */
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
