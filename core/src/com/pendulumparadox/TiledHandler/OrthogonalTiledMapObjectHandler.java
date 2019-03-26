package com.pendulumparadox.TiledHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pendulumparadox.model.component.AnimationComponent;
import com.pendulumparadox.model.component.StateComponent;
import com.pendulumparadox.model.component.TransformComponent;

public class OrthogonalTiledMapObjectHandler extends OrthogonalTiledMapRenderer {

    Engine ecs;

    public OrthogonalTiledMapObjectHandler(TiledMap map, Engine ecs) {
        super(map);
        this.ecs = ecs;
        MapLayer collisionLayer = map.getLayers().get("CollisionLayer");
        MapLayer pointLayer = map.getLayers().get("PointLayer");

        for (MapObject mapObject : collisionLayer.getObjects()) {
            RectangleMapObject colliderRectangle = (RectangleMapObject) mapObject;
            //To get rect properties:
            //colliderRectangle.getRectangle()...

            //0,0 = top left corner
        }
        loadEntities(pointLayer);

    }
    private void loadEntities(MapLayer layer){
        //TODO: this can possibly be made to check for layer instead of "if name != null", but I couldn't figure out how. This is ok though
        for (MapObject object : layer.getObjects()){
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    case "Starting point":
                        System.out.print("here");
                        Entity player = new Entity();

                        Vector2 position = new Vector2();
                        ((RectangleMapObject)object).getRectangle().getPosition(position);
                        player.add(new TransformComponent(position));
                        player.add(new AnimationComponent());
                        player.add(new StateComponent());
                        ecs.addEntity(player);
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
    @Override
    public void renderObject(MapObject object) {
        //For debugging purposes. (drawing points/collision Areas etc)
    }
}
