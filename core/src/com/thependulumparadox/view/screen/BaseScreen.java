package com.thependulumparadox.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thependulumparadox.misc.Constants;

/**
 * This class represents base UI layer (HUD, menu, high score table...)
 */
public abstract class BaseScreen implements Screen
{
    protected final Stage stage;
    protected static OrthographicCamera camera;
    protected static Viewport viewport;

    public BaseScreen()
    {
        if(this.camera == null)
        {
            this.camera = new OrthographicCamera();
        }

        if(viewport == null)
        {
            int width = Gdx.graphics.getWidth();
            int height = Gdx.graphics.getHeight();
            int proportionalWidth = (int)((Constants.VIRTUAL_HEIGHT * width) / (float)height);
            int proportionalHeight = (int)Constants.VIRTUAL_HEIGHT;

            viewport = new FillViewport(proportionalWidth, proportionalHeight);
        }

        stage = new Stage(viewport);
    }

    @Override
    public void render(float delta)
    {
        // Process stage inputs and draw it
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    public Stage getStage()
    {
        return stage;
    }
}
