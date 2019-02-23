package com.pendulumparadox.view;

import com.pendulumparadox.state.IState;
import com.pendulumparadox.view.scene.Scene;
import com.pendulumparadox.view.screen.Screen;

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
