package com.pendulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pendulumparadox.model.component.AnimationComponent;
import com.pendulumparadox.model.component.GraphicsComponent;
import com.pendulumparadox.model.component.TransformComponent;

/**
 * All the logic for animated entities
 */
public class AnimationSystem extends EntitySystem {
    //TODO: move all rendering to same system so we only use one spritebatch
    SpriteBatch batch;
    private ImmutableArray<Entity> entities;
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    //TODO: find place for a global timer
    float elapsedTime = 0;

    public AnimationSystem() {
        batch = new SpriteBatch();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(GraphicsComponent.class).get());

    }

    @Override
    public void update(float dt) {
        batch.begin();

        elapsedTime+=dt;
        for (Entity e : entities) {
            TransformComponent transformComponent = tm.get(e);
            batch.draw(am.get(e).getCurrentFrame(elapsedTime), transformComponent.position.x, transformComponent.position.y);
        }
        batch.end();
    }
}
