package com.pendulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.TiledHandler.OrthogonalTiledMapObjectHandler;
import com.pendulumparadox.model.component.CameraComponent;
import com.pendulumparadox.model.component.DynamicBodyComponent;
import com.pendulumparadox.model.component.StaticBodyComponent;
import com.pendulumparadox.model.component.TransformComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The laws of physics live here (mainly Box2D library in the future)
 */
public class CameraFollowSystem extends EntitySystem
{
    private Entity followedEntity;
    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);

    private OrthographicCamera camera;

    public CameraFollowSystem(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    public void addedToEngine(Engine engine)
    {
        followedEntity = engine.getEntitiesFor(Family.all(CameraComponent.class,
                TransformComponent.class).get()).first();
    }

    public void update(float deltaTime)
    {
        TransformComponent transformComponent
                = transformComponentMapper.get(followedEntity);

        // Follow transform of the entity
        camera.position.set(transformComponent.position, 0);
        camera.update(true);
    }
}
