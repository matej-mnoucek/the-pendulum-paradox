package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.TransformComponent;

/**
 * Simple system for following the player with the game camera
 */
public class CameraFollowSystem extends EntitySystem {
    private Entity followedEntity = null;
    private ImmutableArray<Entity> followableEntities;

    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<PlayerComponent> playerComponentMapper
            = ComponentMapper.getFor(PlayerComponent.class);

    private OrthographicCamera camera;

    public CameraFollowSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void addedToEngine(Engine engine)
    {
        followableEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                TransformComponent.class).get());

        // Find player one
        for (int i = 0; i < followableEntities.size(); i++)
        {
            Entity entity = followableEntities.get(i);
            PlayerComponent player = playerComponentMapper.get(entity);

            if (player.id == 1)
            {
                followedEntity = entity;
                break;
            }
        }
    }

    public void update(float deltaTime)
    {
        if (followedEntity != null)
        {
            TransformComponent transformComponent
                = transformComponentMapper.get(followedEntity);

            // Follow transform of the entity
            camera.position.set(transformComponent.position,0);
            camera.update();
        }
    }
}
