package com.thependulumparadox.view.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 *  This class represents a scene/map loaded from external Tiled level editor
 */
public abstract class Scene implements Screen
{
    protected OrthographicCamera camera;

    public Scene(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }
}
