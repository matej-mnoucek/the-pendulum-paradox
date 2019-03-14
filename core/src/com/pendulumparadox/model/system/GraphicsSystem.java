package com.pendulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pendulumparadox.model.component.GraphicsComponent;
import com.pendulumparadox.model.component.TransformComponent;

/**
 * All the logic for graphics components
 */
public class GraphicsSystem extends EntitySystem {

    SpriteBatch batch;
    private ImmutableArray<Entity> entities;
    private ComponentMapper<GraphicsComponent> gm = ComponentMapper.getFor(GraphicsComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    public GraphicsSystem(){
        batch = new SpriteBatch();
    }


    @Override
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(GraphicsComponent.class).get());

    }

    @Override
    public void update(float dt){
        batch.begin();
        for (Entity e : entities){
            TransformComponent transformComponent = tm.get(e);
            batch.draw(gm.get(e).texture, transformComponent.position.x, transformComponent.position.y);
        }
        batch.end();
    }
}
