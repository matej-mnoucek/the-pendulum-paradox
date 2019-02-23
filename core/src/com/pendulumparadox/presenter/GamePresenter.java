package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GamePresenter extends Game
{
    // Component based system
    Engine engine = new Engine();

    // Assets
    AssetManager assets = new AssetManager();
    OrthographicCamera camera = new OrthographicCamera();

    @Override
    public void create()
    {

    }

    public void update(float delta)
    {
        engine.update(delta);
    }

    @Override
    public void render()
    {
        // Render
        super.render();
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }
}
