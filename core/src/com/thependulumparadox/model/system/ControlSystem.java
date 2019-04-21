package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;
import com.thependulumparadox.model.MoveCommands;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.ControlModule;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.IEventHandler;


/**
 * All the logic for handling input from the user (locally)
 */
public class ControlSystem extends EntitySystem implements MoveCommands
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

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean jump = false;
    private boolean shooting = false;

    public ControlSystem getInputSystem(){
        return this;
    }

    public void addedToEngine(Engine engine)
    {
        controlledEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                ControlComponent.class, PlayerComponent.class, TransformComponent.class).get());

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
                String current = stateComponent.currentState.tag;
                if (current == "runRight")
                {
                    stateComponent.transition("jumpRight");
                }
                else if(current == "runLeft")
                {
                    stateComponent.transition("jumpLeft");
                }
                else
                {
                    stateComponent.transition("jumpRight");
                }
            });

            controlComponent.controlModule.attackStart.addHandler((args)->{

                // Create new bullet
                Entity bullet = entityPool.createEntity();
                bullet.flags = 8;
                TransformComponent transform = new TransformComponent();
                transform.position = transformComponent.position;
                transform.position.x += 0.35f;
                SpriteComponent sprite = new SpriteComponent("sprites/bullets/circle_bullet_blue.png");
                sprite.height = 0.3f;
                sprite.width = 0.3f;
                BulletComponent bulletComponent = new BulletComponent(entity);
                DynamicBodyComponent dynamic = new DynamicBodyComponent(dynamicBodyComponent.body.getWorld());
                dynamic.position(transformComponent.position).dimension(sprite.width, sprite.height)
                        .gravityScale(0.0f).activate(true);
                dynamic.body.applyLinearImpulse(5,0,0,0,false);

                // Add all components
                bullet.add(transform);
                bullet.add(sprite);
                bullet.add(bulletComponent);
                bullet.add(dynamic);

                // Add to engine
                engine.addEntity(bullet);

                stateComponent.transition("shootRight");
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
                if (stateComponent.currentState.tag != "idle")
                {
                    // Delay idle transition a bit
                    Timer.Task task = new Timer.Task(){
                        @Override
                        public void run() {
                            stateComponent.transition("idle");
                            Timer.instance().clear();
                        }};
                    Timer.schedule(task, 0.1f);
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
