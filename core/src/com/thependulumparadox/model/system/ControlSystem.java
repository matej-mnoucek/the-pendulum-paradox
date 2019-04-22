package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.thependulumparadox.model.MoveCommands;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.BulletVisualsComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.AbstractEntityFactory;
import com.thependulumparadox.model.entity.EntityFactory;


/**
 * All the logic for handling input from the user (locally)
 */
public class ControlSystem extends EntitySystem implements MoveCommands
{
    private ImmutableArray<Entity> controlledEntities;
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<ControlComponent> controlComponentMapper
            = ComponentMapper.getFor(ControlComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<StateComponent> stateComponentMapper
            = ComponentMapper.getFor(StateComponent.class);
    private ComponentMapper<BulletComponent> bulletComponentMapper
            = ComponentMapper.getFor(BulletComponent.class);
    private ComponentMapper<BulletVisualsComponent> bulletVisualsComponentMapper
            = ComponentMapper.getFor(BulletVisualsComponent.class);
    private ComponentMapper<SpriteComponent> spriteComponentMapper
            = ComponentMapper.getFor(SpriteComponent.class);

    // Timer for state transitions delays
    Timer timer = new Timer();

    // Entity factory
    AbstractEntityFactory factory;


    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean jump = false;
    private boolean shooting = false;

    public ControlSystem getInputSystem(){
        return this;
    }


    public ControlSystem(AbstractEntityFactory factory)
    {
        this.factory = factory;
    }

    public void addedToEngine(Engine engine)
    {
        controlledEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                ControlComponent.class, StateComponent.class, TransformComponent.class).get());

        for (int i = 0; i < controlledEntities.size(); i++)
        {
            Entity entity = controlledEntities.get(i);
            ControlComponent controlComponent = controlComponentMapper.get(entity);
            DynamicBodyComponent dynamicBodyComponent = dynamicBodyComponentMapper.get(entity);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            StateComponent stateComponent = stateComponentMapper.get(entity);
            BulletVisualsComponent bulletVisualsComponent = bulletVisualsComponentMapper.get(entity);

            controlComponent.controlModule.right.addHandler((args)->
            {
                // Limit max speed right
                if(dynamicBodyComponent.body.getLinearVelocity().x
                        > controlComponent.maxMoveRightSpeed)
                {
                    return;
                }

                // Apply impulse
                dynamicBodyComponent.body.applyLinearImpulse(controlComponent.moveRightImpulse,
                        0,0,0,true);

                // Change state
                stateComponent.transition("runRight");

                // Facing direction
                controlComponent.facingRight = true;
            });

            controlComponent.controlModule.left.addHandler((args)->
            {
                // Limit max speed left
                if(dynamicBodyComponent.body.getLinearVelocity().x
                        < -controlComponent.maxMoveLeftSpeed)
                {
                    return;
                }

                // Apply impulse
                dynamicBodyComponent.body.applyLinearImpulse(-controlComponent.moveLeftImpulse,
                        0,0,0,true);
                // Change state
                stateComponent.transition("runLeft");

                // Facing direction
                controlComponent.facingRight = false;
            });

            controlComponent.controlModule.jumpStart.addHandler((args)->
            {
                // Limit jump in the air
                if (Math.abs(dynamicBodyComponent.body.getLinearVelocity().y)
                        > controlComponent.jumpLimitSpeedThreshold)
                {
                    return;
                }

                dynamicBodyComponent.body.applyLinearImpulse(0,
                        controlComponent.jumpImpulse,0,0,true);


                // Jump to the right direction
                if (controlComponent.facingRight)
                {
                    stateComponent.transition("jumpRight");
                }
                else
                {
                    stateComponent.transition("jumpLeft");
                }
            });

            controlComponent.controlModule.attackStart.addHandler((args)->{

                Entity bullet = factory.create("bullet");
                dynamicBodyComponentMapper.get(bullet).position(transformComponent.position);
                spriteComponentMapper.get(bullet).sprite = bulletVisualsComponent.currentSprite;
                bulletComponentMapper.get(bullet).shotBy = entity;
                engine.addEntity(bullet);


                // Shoot to the right direction
                DynamicBodyComponent dynamic = dynamicBodyComponentMapper.get(bullet);
                if (controlComponent.facingRight)
                {
                    dynamic.body.applyLinearImpulse(controlComponent.shootImpulse,
                            0, 0, 0, true);
                    stateComponent.transition("shootRight");
                }
                else
                {
                    dynamic.body.applyLinearImpulse(-controlComponent.shootImpulse,
                            0, 0, 0, true);
                    stateComponent.transition("shootLeft");
                }
            });
        }
    }

    public void update(float deltaTime)
    {
        // Update control modules
        for (int i = 0; i < controlledEntities.size(); i++)
        {
            Entity entity = controlledEntities.get(i);
            ControlComponent controlComponent = controlComponentMapper.get(entity);
            controlComponent.controlModule.update(deltaTime);

            // Handle idle transition
            DynamicBodyComponent bodyComponent = dynamicBodyComponentMapper.get(entity);
            if (Math.abs(bodyComponent.body.getLinearVelocity().x) < 0.1f
                && Math.abs(bodyComponent.body.getLinearVelocity().y) < 0.1f)
            {
                StateComponent stateComponent = stateComponentMapper.get(entity);
                if (stateComponent.currentState.tag != "idleRight"
                        && stateComponent.currentState.tag != "idleLeft")
                {
                    // Delay idle transition a bit
                    Timer.Task task = new Timer.Task()
                    {
                        @Override
                        public void run() {
                            if (controlComponent.facingRight)
                            {
                                stateComponent.transition("idleRight");
                            }
                            else
                            {
                                stateComponent.transition("idleLeft");
                            }
                            timer.clear();
                        }
                    };
                    timer.schedule(task, 0.1f);
                }
            }

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
