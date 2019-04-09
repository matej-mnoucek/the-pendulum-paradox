package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.thependulumparadox.model.MoveCommands;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;

/**
 * All the logic for handling input from the user (locally)
 */
public class InputSystem extends EntitySystem implements MoveCommands
{
    private Entity controlledEntity;
    private DynamicBodyComponent dynamicBodyComponent;
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);


    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean jump = false;
    private boolean shooting = false;

    public InputSystem getInputSystem(){
        return this;
    }

    public void addedToEngine(Engine engine)
    {
        controlledEntity = engine.getEntitiesFor(Family.all(ControlComponent.class,
                TransformComponent.class).get()).first();
        dynamicBodyComponent = dynamicBodyComponentMapper.get(controlledEntity);
    }

    public void update(float deltaTime)
    {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || moveRight )
        {

            //System.out.println(dynamicBodyComponent.impulseHorizontal);
            dynamicBodyComponent.impulseHorizontal = 15f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || moveLeft)
        {
            dynamicBodyComponent.impulseHorizontal = -15f;
        }
        else
        {
            dynamicBodyComponent.impulseHorizontal = 0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || jump)
        {
            dynamicBodyComponent.impulseVertical = 150f;
            jump = false;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            dynamicBodyComponent.impulseVertical = -5f;
        }
        else
        {
            dynamicBodyComponent.impulseVertical = 0f;
        }
        if (shooting){

        }
    }

    public void moveLeft(){
        moveLeft = true;
    }

    public void moveRight(){
        moveRight = true;
    }

    public void stopMoveLeft(){
        moveLeft = false;
    }

    public void stopMoveRight(){
        moveRight = false;
    }

    public void jump(){jump = true;}

    public void startShooting() {shooting = true;}

    public void stopShooting() {shooting = false;}
}
