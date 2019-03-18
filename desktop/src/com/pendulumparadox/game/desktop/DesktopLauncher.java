package com.pendulumparadox.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pendulumparadox.game.PendulumParadoxGame;
import com.pendulumparadox.presenter.GamePresenter;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		//config.fullscreen = true;
		new LwjglApplication(new PendulumParadoxGame(), config);

		config.width = GamePresenter.V_WIDTH;
		config.height = GamePresenter.V_HEIGHT;
	}
}
