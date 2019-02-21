package com.pendulumparadox.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pendulumparadox.interfaces.IGame;

public class GameController extends Game implements IGame
{
    AssetManager assets = new AssetManager();
    OrthographicCamera camera = new OrthographicCamera();

    @Override
    public void create()
    {

    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void render()
    {
        super.render();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
