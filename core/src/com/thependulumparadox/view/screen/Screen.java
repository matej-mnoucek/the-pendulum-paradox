package com.thependulumparadox.view.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * This class represents an UI layer (HUD, menu, high score table...)
 */
public abstract class Screen implements ApplicationListener
{
    protected Stage stage;




    @Override
    public void create()
    {
        stage = new Stage(new ScreenViewport());
    }

    public Stage getStage()
    {
        return stage;
    }
}
