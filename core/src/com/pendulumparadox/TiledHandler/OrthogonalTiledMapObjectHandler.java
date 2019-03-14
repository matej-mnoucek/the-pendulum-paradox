package com.pendulumparadox.TiledHandler;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class OrthogonalTiledMapObjectHandler extends OrthogonalTiledMapRenderer {

    public OrthogonalTiledMapObjectHandler(TiledMap map) {
        super(map);
        MapLayer collisionLayer = map.getLayers().get("CollisionLayer");

        for (MapObject mapObject : collisionLayer.getObjects()) {
            RectangleMapObject colliderRectangle = (RectangleMapObject) mapObject;
            //To get rect properties:
            //colliderRectangle.getRectangle()...

            //0,0 = top left corner
        }
    }

    @Override
    public void renderObject(MapObject object) {
        //TODO: this can possibly be made to check for layer instead of "if name != null", but I couldn't figure out how. This is ok though
        if (object.getName() != null)
        {
            switch (object.getName())
            {
                case "Starting point":
                    //Create player character(s)
                    break;
                case "Goal":
                    //Create Goal
                    break;
                default:
                    break;
            }
        }


    }
}
