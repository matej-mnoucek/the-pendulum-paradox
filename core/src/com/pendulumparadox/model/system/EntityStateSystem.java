package com.pendulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pendulumparadox.model.component.StateComponent;

/**
 * All the logic for animated entities
 */
public class EntityStateSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private ComponentMapper<StateComponent> sm = ComponentMapper.getFor(StateComponent.class);

    public EntityStateSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(StateComponent.class).get());

    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            //sm.get(e).getStateMachine().
        }
    }
}
