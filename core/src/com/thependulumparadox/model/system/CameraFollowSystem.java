package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.thependulumparadox.model.component.CameraTargetComponent;
import com.thependulumparadox.model.component.TransformComponent;

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
        followedEntity = engine.getEntitiesFor(Family.all(CameraTargetComponent.class,
                TransformComponent.class).get()).first();
    }

    public void update(float deltaTime)
    {
        TransformComponent transformComponent
                = transformComponentMapper.get(followedEntity);

        // Follow transform of the entity
        camera.position.set(transformComponent.position, 0);
        camera.update();
    }
}
