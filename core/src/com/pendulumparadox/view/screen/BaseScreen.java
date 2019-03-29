package com.pendulumparadox.view.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * This class represents an UI layer (HUD, menu, high score table...)
 */
public abstract class BaseScreen implements Screen
{
    protected Stage stage;
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
            viewport = new ScreenViewport(camera);
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

    public Stage getStage()
    {
        return stage;
    }
}
