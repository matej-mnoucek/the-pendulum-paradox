package com.thependulumparadox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.presenter.GamePresenter;

public class PendulumParadoxGame extends ApplicationAdapter
{
	private Game game;

	// Standard single player game

    // Multi player game with  synchronization proxy
    public PendulumParadoxGame(ISynchronization proxy)
    {
        game = new GamePresenter(proxy);
    }

    @Override
    public void create()
    {
        super.create();
        game.create();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        game.resize(width, height);
    }

    @Override
    public void render()
    {
        super.render();
        game.render();
    }

    @Override
    public void pause()
    {
        super.pause();
        game.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
        game.resume();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        game.dispose();
    }
}
