package com.pendulumparadox.view.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * This class represents an UI layer (HUD, menu, high score table...)
 */
public abstract class BaseScreen implements Screen
{
    protected Stage stage;

    public BaseScreen()
    {
        stage = new Stage(new ScreenViewport());
    }

    public Stage getStage()
    {
        return stage;
    }
}
