package com.thependulumparadox.levels;

import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.BaseScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of level manager
 * @see ILevelManager
 */
public class LevelManager implements ILevelManager
{
    private List<ViewState> levels;
    private IStateMachine viewMachine;
    private int currentLevelIndex;

    public LevelManager(IStateMachine viewMachine)
    {
        levels = new ArrayList<>();
        currentLevelIndex = 0;
        this.viewMachine = viewMachine;
    }

    public void addLevel(Scene scene, BaseScreen inGameScreen, BaseScreen gameOverScreen)
    {
        ViewState inGame = new ViewState(scene, inGameScreen);
        ViewState gameOver = new ViewState(scene, gameOverScreen);

        viewMachine.addState(inGame);
        viewMachine.addState(gameOver);

        levels.add(inGame);
        levels.add(gameOver);
    }

    public boolean isCurrentLevelLast()
    {
        return levels.size() == (currentLevelIndex + 2);
    }

    public void reset() { currentLevelIndex = 0; }

    public Scene currentLevelScene()
    {
        return levels.get(currentLevelIndex).getScene();
    }

    public ViewState currentInGameViewState()
    {
        return levels.get(currentLevelIndex);
    }

    public ViewState currentGameOverViewState()
    {
        return levels.get(currentLevelIndex + 1);
    }

    public boolean nextLevel()
    {
        if (levels.size() == (currentLevelIndex + 2))
        {
            return false;
        }
        else
        {
            currentLevelIndex += 2;
            return true;
        }
    }
}
