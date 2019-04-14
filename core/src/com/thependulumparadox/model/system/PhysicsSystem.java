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
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
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
    private static final float TIME_STEP = 1/60f;
    private static final float MAX_TIME_STEP = 1/4f;
    private static final int VELOCITY_ITERATIONS = 12;
    private static final int POSITION_ITERATIONS = 2;

    // Collision category (bit masks) - 15 bits
    // 000000000000001 = 1 = ground group
    // 000000000000010 = 2 = player group
    // 000000000000100 = 4 = enemy group
    // 000000000001000 = 8 = bullet group
    // 000000000010000 = 16 = power-up group
    // 000000000100000 = 32 = player trigger group
    // 000000001000000 = 64 = enemy trigger group
    // 000000010000000 = 128 = power-up trigger group
    public enum CollisionCategory
    {
        DEFAULT(1),
        PLAYER(2),
        ENEMY(4),
        BULLET(8),
        POWERUP(16),
        PLAYER_TRIGGER(32),
        ENEMY_TRIGGER(64),
        POWERUP_TRIGGER(128);

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
    // Player trigger group = collides with enemy and power-up group = 000000000010100
    // Enemy trigger group = collides with bullets and player group = 000000000001010
    // Power-up trigger group = collides with player group = 000000000000010
    public enum CollisionMask
    {
        DEFAULT(32767),
        PLAYER(21),
        ENEMY(11),
        BULLET(5),
        POWERUP(3),
        PLAYER_TRIGGER(20),
        ENEMY_TRIGGER(10),
        POWERUP_TRIGGER(2);

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

    // Bodies ready to be destroyed
    private HashSet<Body> bodiesToDestroy = new HashSet<>();

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
                        engine.removeEntity(entityA);
                        engine.removeEntity(entityB);

                        bodiesToDestroy.add(contact.getFixtureA().getBody());
                        bodiesToDestroy.add(contact.getFixtureB().getBody());

                        return;
                    }

                    if(entityB.flags == 4 && entityA.flags == 8)
                    {
                        engine.removeEntity(entityA);
                        engine.removeEntity(entityB);

                        bodiesToDestroy.add(contact.getFixtureA().getBody());
                        bodiesToDestroy.add(contact.getFixtureB().getBody());

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
                        bodiesToDestroy.add(contact.getFixtureA().getBody());
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
                        bodiesToDestroy.add(contact.getFixtureB().getBody());
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

        // Bodies to destroy
        Iterator<Body> iterator = bodiesToDestroy.iterator();
        while(iterator.hasNext())
        {
            Body body = iterator.next();
            world.destroyBody(body);
        }
        bodiesToDestroy.clear();


        // Update physics
        float frameTime = Math.min(deltaTime, MAX_TIME_STEP);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP)
        {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
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
                filter2.categoryBits = CollisionCategory.PLAYER_TRIGGER.bits;
                filter2.maskBits = CollisionMask.PLAYER_TRIGGER.bits;
                break;
            // Enemy
            case 4:
                filter1.categoryBits = CollisionCategory.ENEMY.bits;
                filter1.maskBits = CollisionMask.ENEMY.bits;
                filter2.categoryBits = CollisionCategory.ENEMY_TRIGGER.bits;
                filter2.maskBits = CollisionMask.ENEMY_TRIGGER.bits;
                break;
            // Bullet
            case 8:
                filter1.categoryBits = CollisionCategory.BULLET.bits;
                filter1.maskBits = CollisionMask.BULLET.bits;
                break;
            // Power up
            case 16:
                filter1.categoryBits = CollisionCategory.POWERUP.bits;
                filter1.maskBits = CollisionMask.POWERUP.bits;
                filter2.categoryBits = CollisionCategory.POWERUP_TRIGGER.bits;
                filter2.maskBits = CollisionMask.POWERUP_TRIGGER.bits;
                break;
        }
        body.getFixtureList().first().setFilterData(filter1);

        // If entity has trigger as well set collision rules
        if (body.getFixtureList().size > 1)
        {
            body.getFixtureList().get(1).setFilterData(filter2);
        }

        // Add collision argument = entity
        body.getFixtureList().first().setUserData(entity);
    }
}
