package com.pendulumparadox.view.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.pendulumparadox.TiledHandler.OrthogonalTiledMapObjectHandler;

public class GameScene extends Scene
{
    private MapRenderer renderer;

    public GameScene(TiledMap level, OrthographicCamera camera)
    {
        super(camera);

        // I dont know what is the difference between these two renderers
        //renderer = new OrthogonalTiledMapObjectHandler(level);
        renderer = new OrthogonalTiledMapRenderer(level);
        renderer.setView(camera);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        // Set view and render scene
        renderer.setView(camera);
        renderer.render();
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
        super.dispose();
    }
}
