package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.thependulumparadox.model.MoveCommands;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.PlayerComponent;
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
        controlledEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                TransformComponent.class).get()).first();
        dynamicBodyComponent = dynamicBodyComponentMapper.get(controlledEntity);
    }

    public void update(float deltaTime)
    {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || moveRight )
        {

            //System.out.println(dynamicBodyComponent.impulseHorizontal);
            dynamicBodyComponent.body.applyLinearImpulse(1f, 0,0,0,true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || moveLeft)
        {
            dynamicBodyComponent.body.applyLinearImpulse(-1f, 0,0,0,true);
        }
        else
        {
            //dynamicBodyComponent.impulseHorizontal = 0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || jump)
        {
            //dynamicBodyComponent.impulseVertical = 15f;
            dynamicBodyComponent.body.applyLinearImpulse(0, 1f,0,0,true);
            jump = false;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            //dynamicBodyComponent.impulseVertical = -5f;
            dynamicBodyComponent.body.applyLinearImpulse(0, -1f,0,0,true);
        }
        else
        {
            //dynamicBodyComponent.impulseVertical = 0f;
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
