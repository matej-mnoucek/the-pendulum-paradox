package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;


/**
 * All the logic for handling input from the user (locally)
 */
public class ControlSystem extends EntitySystem
{
    private PooledEngine entityPool = new PooledEngine();

    private ImmutableArray<Entity> controlledEntities;
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<ControlComponent> controlComponentMapper
            = ComponentMapper.getFor(ControlComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<StateComponent> stateComponentMapper
            = ComponentMapper.getFor(StateComponent.class);

    // Timer for state transitions delays
    Timer timer = new Timer();

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


            controlComponent.controlModule.right.addHandler((args)->
            {
                // Limit max speed right
                if(dynamicBodyComponent.body.getLinearVelocity().x > 6.0f)
                {
                    return;
                }

                // Apply impulse
                dynamicBodyComponent.body.applyLinearImpulse(1f, 0,0,0,true);
                // Change state
                stateComponent.transition("runRight");

                // Facing direction
                controlComponent.facingRight = true;
            });

            controlComponent.controlModule.left.addHandler((args)->
            {
                // Limit max speed left
                if(dynamicBodyComponent.body.getLinearVelocity().x < -6.0f)
                {
                    return;
                }

                // Apply impulse
                dynamicBodyComponent.body.applyLinearImpulse(-1f, 0,0,0,true);
                // Change state
                stateComponent.transition("runLeft");

                // Facing direction
                controlComponent.facingRight = false;
            });

            controlComponent.controlModule.jumpStart.addHandler((args)->
            {
                // Limit jump in the air
                //TODO: Allows for double jumping when attempting to jump at peak
                if (Math.abs(dynamicBodyComponent.body.getLinearVelocity().y) > 0.1f)
                {
                    return;
                }

                dynamicBodyComponent.body.applyLinearImpulse(0, 10f,0,0,true);


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

                // Create new bullet
                Entity bullet = entityPool.createEntity();
                bullet.flags = 8;
                TransformComponent transform = new TransformComponent();
                transform.position = new Vector2(transformComponent.position);
                //transform.position.x += 0.35f;
                SpriteComponent sprite = new SpriteComponent("sprites/bullets/circle_bullet_blue.png");
                sprite.height = 0.3f;
                sprite.width = 0.3f;
                BulletComponent bulletComponent = new BulletComponent(entity);
                DynamicBodyComponent dynamic = new DynamicBodyComponent(dynamicBodyComponent.body.getWorld());
                dynamic.position(transformComponent.position).dimension(sprite.width, sprite.height)
                        .gravityScale(0.0f).activate(true);

                // Add all components
                bullet.add(transform);
                bullet.add(sprite);
                bullet.add(bulletComponent);
                bullet.add(dynamic);

                // Add to engine
                engine.addEntity(bullet);


                // Shoot to the right direction
                if (controlComponent.facingRight)
                {
                    dynamic.body.applyLinearImpulse(5, 0, 0, 0, true);
                    stateComponent.transition("shootRight");
                }
                else
                {
                    dynamic.body.applyLinearImpulse(-5, 0, 0, 0, true);
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

}
