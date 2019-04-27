package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.StateComponent;

/**
 * System that processes animated sprite components, update frames to render
 * and switches animations based on the current entity state
 * @see AnimatedSpriteComponent
 */
public class AnimationControlSystem extends EntitySystem
{
    private ImmutableArray<Entity> animatedEntities;

    private ComponentMapper<StateComponent> stateComponentMapper
            = ComponentMapper.getFor(StateComponent.class);
    private ComponentMapper<AnimatedSpriteComponent> animatedSpriteComponentMapper
            = ComponentMapper.getFor(AnimatedSpriteComponent.class);


    @Override
    public void addedToEngine(Engine engine)
    {
        animatedEntities = engine.getEntitiesFor(Family.all(AnimatedSpriteComponent.class,
                StateComponent.class).get());
    }

    @Override
    public void update(float delta)
    {

        // Change animations based on states
        for (int i = 0; i < animatedEntities.size(); i++)
        {
            Entity entity = animatedEntities.get(i);
            StateComponent stateComponent = stateComponentMapper.get(entity);
            AnimatedSpriteComponent animatedSpriteComponent
                    = animatedSpriteComponentMapper.get(entity);

            // Change animation based on current state name
            if (stateComponent.currentState != null)
            {
                String tag = stateComponent.currentState.tag;
                if (animatedSpriteComponent.animations.containsKey(tag))
                {
                    animatedSpriteComponent.currentAnimation(tag);
                }
            }
        }
    }
}
