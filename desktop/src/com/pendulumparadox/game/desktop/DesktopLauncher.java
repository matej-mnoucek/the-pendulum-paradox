package com.pendulumparadox.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.pendulumparadox.game.PendulumParadoxGame;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		//config.fullscreen = true;
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = (int)Math.pow(2,15);
		settings.maxHeight = (int)Math.pow(2,15);
		TexturePacker.process(settings, "Characters/png", "../game-android/assets", "robot");

		new LwjglApplication(new PendulumParadoxGame(), config);
	}
}
