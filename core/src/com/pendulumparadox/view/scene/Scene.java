package com.pendulumparadox.view.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.pendulumparadox.TiledHandler.OrthogonalTiledMapObjectHandler;

/**
 *  This class represents a scene/map loaded from external Tiled level editor
 */
public abstract class Scene implements Screen
{
    TiledMap level;
    MapRenderer renderer;

    public Scene(TiledMap level, OrthographicCamera camera)
    {
        this.level = level;
        renderer = new OrthogonalTiledMapObjectHandler(level);
        //renderer = new OrthogonalTiledMapRenderer(level);
        renderer.setView(camera);
    }

    @Override
    public void dispose() {
        this.level.dispose();
    }
}
