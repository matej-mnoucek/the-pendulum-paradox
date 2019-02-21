package com.pendulumparadox.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.pendulumparadox.controller.GameController;
import com.pendulumparadox.interfaces.IGame;

public class PendulumParadoxGame extends ApplicationAdapter
{
	IGame game = new GameController();

	@Override
	public void create ()
	{
		game.create();
	}

	@Override
	public void render ()
	{
		game.render();
		game.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose ()
	{
		game.dispose();
	}
}
