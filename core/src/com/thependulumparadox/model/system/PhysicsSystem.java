package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The laws of physics live here (mainly Box2D library in the future)
 */
public class PhysicsSystem extends EntitySystem
{
    // Collision category (bit masks) - 15 bits
    // 000000000000001 = 1 = ground group
    // 000000000000010 = 2 = player group
    // 000000000000100 = 4 = enemy group
    // 000000000001000 = 8 = bullet group
    // 000000000010000 = 16 = power-up group
    public enum CollisionCategory
    {
        DEFAULT(1),
        PLAYER(2),
        ENEMY(4),
        BULLET(8),
        POWERUP(16);

        public final short bits;
        private CollisionCategory(int bits)
        {
            this.bits = (short)bits;
        }
    }

    // Collision masks (bit masks) - 15 bits
    // Default group = collides with everything = 111111111111111
    // Player group = collides with default, enemy and power-up group = 000000000010101
    // Enemy group = collides with default, bullets and player group = 000000000001011
    // Bullet group = collides with default and enemy group = 000000000000101
    // Power-up group = collides with default and player group = 000000000000011
    public enum CollisionMask
    {
        DEFAULT(32767),
        PLAYER(21),
        ENEMY(11),
        BULLET(5),
        POWERUP(3);

        public final short bits;
        private CollisionMask(int bits)
        {
            this.bits = (short)bits;
        }
    }


    private ImmutableArray<Entity> staticBodyEntities;
    private ImmutableArray<Entity> dynamicBodyEntities;

    private ComponentMapper<StaticBodyComponent> staticBodyComponentComponentMapper
            = ComponentMapper.getFor(StaticBodyComponent.class);
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);

    private World world;

    public PhysicsSystem(World world) { this.world = world; }

    public void addedToEngine(Engine engine)
    {
        staticBodyEntities = engine.getEntitiesFor(Family.all(StaticBodyComponent.class,
                TransformComponent.class).get());
        dynamicBodyEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                TransformComponent.class).get());

        // Initialize static bodies
        for (int i = 0; i < staticBodyEntities.size(); i++)
        {
            Entity entity = staticBodyEntities.get(i);
            StaticBodyComponent component = staticBodyComponentComponentMapper.get(entity);

            initializeBody(entity, component.body);
            component.initialized = true;
            component.activate(true);
        }

        // Initialize dynamic bodies
        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            DynamicBodyComponent component = dynamicBodyComponentComponentMapper.get(entity);

            initializeBody(entity, component.body);
            component.initialized = true;
            component.activate(true);
        }

        // Define collision handling
        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                if(contact.getFixtureA().getUserData() instanceof Entity
                        && contact.getFixtureB().getUserData() instanceof Entity)
                {
                    Entity entityA = (Entity) contact.getFixtureA().getUserData();
                    Entity entityB = (Entity) contact.getFixtureB().getUserData();

                    // If bullet and enemy kill it
                    if(entityA.flags == 4 && entityB.flags == 8)
                    {
                        world.step(0,0,0);
                        world.destroyBody(contact.getFixtureA().getBody());
                        world.destroyBody(contact.getFixtureB().getBody());

                        engine.removeEntity(entityA);
                        engine.removeEntity(entityB);

                        return;
                    }

                    if(entityB.flags == 4 && entityA.flags == 8)
                    {
                        world.step(0,0,0);
                        world.destroyBody(contact.getFixtureA().getBody());
                        world.destroyBody(contact.getFixtureB().getBody());

                        engine.removeEntity(entityA);
                        engine.removeEntity(entityB);

                        return;
                    }
                }

                if(contact.getFixtureA().getUserData() instanceof Entity)
                {
                    Entity entity = (Entity) contact.getFixtureA().getUserData();

                    //System.out.println("FLAG_A:" + entity.flags);

                    // If bullet then remove it
                    if (entity.flags == 8)
                    {
                        engine.removeEntity(entity);
                        return;
                    }
                }

                if(contact.getFixtureB().getUserData() instanceof Entity)
                {
                    Entity entity = (Entity) contact.getFixtureB().getUserData();

                    //System.out.println("FLAG_B:" + entity.flags);

                    // If bullet then remove it
                    if (entity.flags == 8)
                    {
                        engine.removeEntity(entity);
                        return;
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private float accumulator = 0;
    private float timeStep = 1/60.0f;
    public void update(float deltaTime)
    {
        // Check new entities
        for (int i = 0; i < staticBodyEntities.size(); i++)
        {
            Entity entity = staticBodyEntities.get(i);
            StaticBodyComponent component = staticBodyComponentComponentMapper.get(entity);

            if (!component.initialized)
            {
                initializeBody(entity, component.body);
                component.initialized = true;
            }
        }

        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            DynamicBodyComponent component = dynamicBodyComponentComponentMapper.get(entity);

            if (!component.initialized)
            {
                initializeBody(entity, component.body);
                component.initialized = true;
            }
        }


        // Update positions of dynamic bodies
        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            TransformComponent transformComponent
                    = transformComponentMapper.get(entity);
            DynamicBodyComponent dynamicBodyComponent
                    = dynamicBodyComponentComponentMapper.get(entity);

            // Update position
            transformComponent.position = dynamicBodyComponent.body.getPosition();
        }

        // Update physics
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep)
        {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }
    }

    private void initializeBody(Entity entity, Body body)
    {
        // Setup collisions
        Filter filter = new Filter();
        switch (entity.flags)
        {
            // Default
            case 1:
                filter.categoryBits = CollisionCategory.DEFAULT.bits;
                filter.maskBits = CollisionMask.DEFAULT.bits;
                break;
            // Player
            case 2:
                filter.categoryBits = CollisionCategory.PLAYER.bits;
                filter.maskBits = CollisionMask.PLAYER.bits;
                break;
            // Enemy
            case 4:
                filter.categoryBits = CollisionCategory.ENEMY.bits;
                filter.maskBits = CollisionMask.ENEMY.bits;
                break;
            // Bullet
            case 8:
                filter.categoryBits = CollisionCategory.BULLET.bits;
                filter.maskBits = CollisionMask.BULLET.bits;
                break;
            // Power up
            case 16:
                filter.categoryBits = CollisionCategory.POWERUP.bits;
                filter.maskBits = CollisionMask.POWERUP.bits;
                break;
        }
        body.getFixtureList().first().setFilterData(filter);

        // Add collision argument
        body.getFixtureList().first().setUserData(entity);
    }
}
