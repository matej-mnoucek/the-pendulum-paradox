package com.pendulumparadox.view.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class GameScene extends Scene
{
    public GameScene(TiledMap level, OrthographicCamera camera)
    {
        super(level, camera);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        super.renderer.render();
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

    @Override
    public void dispose()
    {

    }
}
