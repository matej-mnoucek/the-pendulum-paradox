package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;

/**
 * All the logic for handling input from the user (locally)
 */
public class InputSystem extends EntitySystem
{
    private Entity controlledEntity;
    private DynamicBodyComponent dynamicBodyComponent;
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);

    public void addedToEngine(Engine engine)
    {
        controlledEntity = engine.getEntitiesFor(Family.all(ControlComponent.class,
                TransformComponent.class).get()).first();
        dynamicBodyComponent = dynamicBodyComponentMapper.get(controlledEntity);
    }

    public void update(float deltaTime)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            //System.out.println(dynamicBodyComponent.impulseHorizontal);
            dynamicBodyComponent.impulseHorizontal = 5f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            dynamicBodyComponent.impulseHorizontal = -5f;
        }
        else
        {
            dynamicBodyComponent.impulseHorizontal = 0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            dynamicBodyComponent.impulseVertical = 15f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            dynamicBodyComponent.impulseVertical = -5f;
        }
        else
        {
            dynamicBodyComponent.impulseVertical = 0f;
        }
    }
}
