package com.thependulumparadox.view;

import com.thependulumparadox.state.IState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.Screen;

/**
 * This class implements a View state (for MVP and from State pattern).
 * Each view state represents combination of UI and loaded Scene
 * Each view state will communicate with Presenter via events
 */
public class ViewState implements IState
{
    private Screen screen;
    private Scene scene;

    @Override
    public void execute()
    {

    }
}
