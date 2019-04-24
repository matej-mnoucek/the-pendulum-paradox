package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;

import java.util.HashSet;
import java.util.Iterator;

/**
 * The laws of physics live here (mainly Box2D library in the future)
 */
public class PhysicsSystem extends EntitySystem
{
    // Constants
    private static final float TIME_STEP = 1/120f;
    private static final float MAX_TIME_STEP = 1/16f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 4;

    // Collision category (bit masks) - 15 bits
    // 000000000000001 = 1 = ground group
    // 000000000000010 = 2 = player group
    // 000000000000100 = 4 = enemy group
    // 000000000001000 = 8 = bullet group
    // 000000000010000 = 16 = enhancement group
    // 000000000100000 = 32 = coin group
    // 000000001000000 = 64 = player trigger group
    // 000000010000000 = 128 = enemy trigger group
    public enum CollisionCategory
    {
        DEFAULT(1),
        PLAYER(2),
        ENEMY(4),
        BULLET(8),
        ENHANCEMENT(16),
        COIN(32);
        //PLAYER_TRIGGER(64),
        //ENEMY_TRIGGER(128);

        public final short bits;
        private CollisionCategory(int bits)
        {
            this.bits = (short)bits;
        }
    }

    // Collision masks (bit masks) - 15 bits
    // Default group = collides with everything = 111111111111111
    // Player group = collides with default, enemy and enhancement group = 000000000010101
    // Enemy group = collides with default, bullets and player group = 000000000001011
    // Bullet group = collides with default and enemy group = 000000000000101
    // Enhancement group = collides with default and player group = 000000000000011
    // Coin group = collides with default and player group = 000000000000011
    // Player trigger group = collides with enemy and enhancement group = 000000000010100
    // Enemy trigger group = collides with bullets and player group = 000000000001010
    public enum CollisionMask
    {
        DEFAULT(32767),
        PLAYER(21),
        ENEMY(11),
        BULLET(5),
        ENHANCEMENT(3),
        COIN(3),
        PLAYER_TRIGGER(20),
        ENEMY_TRIGGER(10);

        public final short bits;
        private CollisionMask(int bits)
        {
            this.bits = (short)bits;
        }
    }

    // Cached entities
    private ImmutableArray<Entity> staticBodyEntities;
    private ImmutableArray<Entity> dynamicBodyEntities;

    private ComponentMapper<StaticBodyComponent> staticBodyComponentComponentMapper
            = ComponentMapper.getFor(StaticBodyComponent.class);
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);


    // Physics world
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
        }

        // Initialize dynamic bodies
        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            DynamicBodyComponent component = dynamicBodyComponentComponentMapper.get(entity);

            initializeBody(entity, component.body);
            component.initialized = true;
        }
    }

    private float accumulator = 0;
    public void update(float deltaTime)
    {
        // Check new entities and initialize them
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


        // Update physics
        float frameTime = Math.min(deltaTime, MAX_TIME_STEP);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP)
        {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }


        // Update dynamic bodies
        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            TransformComponent transformComponent
                    = transformComponentMapper.get(entity);
            DynamicBodyComponent dynamicBodyComponent
                    = dynamicBodyComponentComponentMapper.get(entity);

            // Update position
            transformComponent.position = dynamicBodyComponent.body.getPosition();

            // Destroy
            if(dynamicBodyComponent.toDestroy)
            {
                world.destroyBody(dynamicBodyComponent.body);
                getEngine().removeEntity(entity);
            }
        }
    }

    // Further initialization of bodies in the system
    private void initializeBody(Entity entity, Body body)
    {
        // Setup collisions and triggers
        Filter filter1 = new Filter();
        Filter filter2 = new Filter();
        switch (entity.flags)
        {
            // Default
            case 1:
                filter1.categoryBits = CollisionCategory.DEFAULT.bits;
                filter1.maskBits = CollisionMask.DEFAULT.bits;
                break;
            // Player
            case 2:
                filter1.categoryBits = CollisionCategory.PLAYER.bits;
                filter1.maskBits = CollisionMask.PLAYER.bits;
                //filter2.categoryBits = CollisionCategory.PLAYER_TRIGGER.bits;
                filter2.maskBits = CollisionMask.PLAYER_TRIGGER.bits;
                break;
            // Enemy
            case 4:
                filter1.categoryBits = CollisionCategory.ENEMY.bits;
                filter1.maskBits = CollisionMask.ENEMY.bits;
                //filter2.categoryBits = CollisionCategory.ENEMY_TRIGGER.bits;
                filter2.maskBits = CollisionMask.ENEMY_TRIGGER.bits;
                break;
            // Bullet
            case 8:
                filter1.categoryBits = CollisionCategory.BULLET.bits;
                filter1.maskBits = CollisionMask.BULLET.bits;
                break;
            // Enhancement
            case 16:
                filter1.categoryBits = CollisionCategory.ENHANCEMENT.bits;
                filter1.maskBits = CollisionMask.ENHANCEMENT.bits;
                break;
            // Coin
            case 32:
                //filter1.categoryBits = CollisionCategory.COIN.bits;
                //filter1.maskBits = CollisionMask.COIN.bits;
                break;
        }

        // Set fixture 1 filter
        body.getFixtureList().first().setFilterData(filter1);
        // Add fixture 1 collision argument = entity
        body.getFixtureList().first().setUserData(entity);

        // If entity has trigger as well set collision rules and argument
        if (body.getFixtureList().size > 1)
        {
            body.getFixtureList().get(1).setFilterData(filter2);
            body.getFixtureList().get(1).setUserData(entity);
        }
    }
}
