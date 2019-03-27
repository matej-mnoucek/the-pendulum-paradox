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
    protected OrthographicCamera camera;

    public Scene(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    @Override
    public void dispose() {
        this.level.dispose();
    }
}
