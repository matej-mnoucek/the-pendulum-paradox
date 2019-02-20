package com.pendulumparadox.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameLevel implements ILevel
{
    TiledMap map;
    MapRenderer mapRenderer;

    public GameLevel(TiledMap level, OrthographicCamera camera)
    {
        map = level;
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(camera);
    }

    @Override
    public void render()
    {
        mapRenderer.render();
    }
}
