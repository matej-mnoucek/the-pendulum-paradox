package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.BulletVisualsComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.SoundComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.AbstractEntityFactory;
import com.thependulumparadox.model.entity.EntityFactory;


/**
 * System that converts events from control modules into real actions executed on an entity
 * e.g. run left, right, attack, jump
 * @see ControlComponent
 */
public class ControlSystem extends EntitySystem
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
    private ComponentMapper<SoundComponent> soundComponentMapper
            = ComponentMapper.getFor(SoundComponent.class);

    // Timer for state transitions delays
    Timer timer = new Timer();

    // Entity factory
    AbstractEntityFactory factory;

    private int lastIterationEntityCount = 0;


    public ControlSystem(World world)
    {
        this.factory = new EntityFactory(world);
    }

    public void addedToEngine(Engine engine)
    {
        controlledEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                ControlComponent.class, StateComponent.class, TransformComponent.class).get());

        initializeEventHandlers();
        lastIterationEntityCount = controlledEntities.size();
    }

    private void initializeEventHandlers()
    {
        // Remove old handlers
        for (int i = 0; i < controlledEntities.size(); i++)
        {
            Entity entity = controlledEntities.get(i);
            ControlComponent controlComponent = controlComponentMapper.get(entity);

            controlComponent.controlModule.right.removeAllHandlers();
            controlComponent.controlModule.left.removeAllHandlers();
            controlComponent.controlModule.jumpStart.removeAllHandlers();
            controlComponent.controlModule.attackStart.removeAllHandlers();
            controlComponent.controlModule.meleeStart.removeAllHandlers();
        }

        // Create new handlers
        for (int i = 0; i < controlledEntities.size(); i++)
        {
            Entity entity = controlledEntities.get(i);
            ControlComponent controlComponent = controlComponentMapper.get(entity);
            DynamicBodyComponent dynamicBodyComponent = dynamicBodyComponentMapper.get(entity);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            StateComponent stateComponent = stateComponentMapper.get(entity);
            BulletVisualsComponent bulletVisualsComponent = bulletVisualsComponentMapper.get(entity);
            SoundComponent soundComponent = soundComponentMapper.get(entity);


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

                // Play sound
                if (soundComponent != null)
                    soundComponent.enqueuePlay("jump");
            });

            controlComponent.controlModule.attackStart.addHandler((args)->{

                Entity bullet = factory.create("bullet");
                dynamicBodyComponentMapper.get(bullet).position(transformComponent.position);
                spriteComponentMapper.get(bullet).sprite = bulletVisualsComponent.currentSprite;
                bulletComponentMapper.get(bullet).shotBy = entity;
                getEngine().addEntity(bullet);


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

                // Play sound
                if (soundComponent != null)
                    soundComponent.enqueuePlay("shoot");
            });

            controlComponent.controlModule.meleeStart.addHandler((args)->{

                // Attack to the right direction
                if (controlComponent.facingRight)
                {
                    stateComponent.transition("attackRight");
                }
                else
                {
                    stateComponent.transition("attackLeft");
                }
            });
        }
    }

    public void update(float deltaTime)
    {
        if (controlledEntities.size() != lastIterationEntityCount)
        {
            initializeEventHandlers();
            lastIterationEntityCount = controlledEntities.size();
        }

        // Update control modules
        for (int i = 0; i < controlledEntities.size(); i++)
        {
            Entity entity = controlledEntities.get(i);
            ControlComponent controlComponent = controlComponentMapper.get(entity);
            controlComponent.controlModule.update(deltaTime);

            // Handle idle transition
            DynamicBodyComponent bodyComponent = dynamicBodyComponentMapper.get(entity);
            if (Math.abs(bodyComponent.body.getLinearVelocity().x) < controlComponent.backToIdleSpeedThreshold
                && Math.abs(bodyComponent.body.getLinearVelocity().y) < controlComponent.backToIdleSpeedThreshold)
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
                    timer.schedule(task, controlComponent.backToIdleTime);
                }
            }

        }
    }

}
