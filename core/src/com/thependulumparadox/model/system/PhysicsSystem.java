package com.thependulumparadox.model.system;

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
    // Collision category (bit masks)
    // 0000000000000001 = 1 = ground group
    // 0000000000000010 = 2 = player group
    // 0000000000000100 = 4 = enemy group
    // 0000000000001000 = 8 = bullet group
    // 0000000000010000 = 16 = power-up group
    public enum CollisionCategory
    {
        DEFAULT(0x0001),
        PLAYER(0x0002),
        ENEMY(0x0004),
        BULLET(0x0008),
        POWERUP(0x0010);

        public final short bits;
        private CollisionCategory(int bits)
        {
            this.bits = (short)bits;
        }
    }

    // Collision masks (bit masks)
    // Default group = collides with everything = 1111111111111111
    // Player group = collides with default, enemy and power-up group = 0000000000010101
    // Enemy group = collides with default, bullets and player group = 0000000000001011
    // Bullet group = collides with default and enemy group = 0000000000000101
    // Power-up group = collides with default and player group = 0000000000000011
    public enum CollisionMask
    {
        DEFAULT(0xFFFF),
        PLAYER(0x0015),
        ENEMY(0x000B),
        BULLET(0x0005),
        POWERUP(0x0003);

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

    private List<Body> staticBodies = new ArrayList<>();
    private List<Body> dynamicBodies = new ArrayList<>();
    private List<Entity> cachedStaticBodyEntities = new ArrayList<>();
    private List<Entity> cachedDynamicBodyEntities = new ArrayList<>();

    private World world;

    public PhysicsSystem(World world) { this.world = world; }

    public void addedToEngine(Engine engine)
    {
        staticBodyEntities = engine.getEntitiesFor(Family.all(StaticBodyComponent.class,
                TransformComponent.class).get());
        dynamicBodyEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                TransformComponent.class).get());

        // Create static bodies
        for (int i = 0; i < staticBodyEntities.size(); i++)
        {
            Entity entity = staticBodyEntities.get(i);
            staticBodies.add(tearUpStaticBody(entity));

            // Cache corresponding entity
            cachedStaticBodyEntities.add(entity);
        }

        // Create dynamic bodies
        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
            Body dynamicBody = tearUpDynamicBody(entity);
            dynamicBodies.add(dynamicBody);

            // Cache corresponding entity
            cachedDynamicBodyEntities.add(entity);
        }

        // Define collision handling
        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {

                if(contact.getFixtureB().getUserData() instanceof Entity)
                {
                    Entity entity = (Entity) contact.getFixtureB().getUserData();

                    // If bullet then remove it
                    if (entity.flags == 0x0008)
                    {
                        engine.removeEntity(entity);
                    }
                }

                if(contact.getFixtureA().getUserData() instanceof Entity)
                {
                    Entity entity = (Entity) contact.getFixtureA().getUserData();

                    // If bullet then remove it
                    if (entity.flags == 0x0008)
                    {
                        engine.removeEntity(entity);
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

    public void removedFromEngine (Engine engine)
    {
        
        // Dispose shapes
        for (Body staticBody : staticBodies)
        {
            for (Fixture fixture : staticBody.getFixtureList())
            {
                fixture.getShape().dispose();
            }
        }

        for (Body dynamicBody : dynamicBodies)
        {
            for (Fixture fixture : dynamicBody.getFixtureList())
            {
                fixture.getShape().dispose();
            }
        }

        // Delete bodies and empty caches
        staticBodies.clear();
        cachedStaticBodyEntities.clear();
        dynamicBodies.clear();
        cachedDynamicBodyEntities.clear();
    }

    public void update(float deltaTime)
    {
        // Check if some dynamic bodies were added to the engine
        if (dynamicBodyEntities.size() != cachedDynamicBodyEntities.size())
        {
            // If there is new entity
            for (int i = 0; i < dynamicBodyEntities.size(); i++)
            {
                // Get entity
                Entity entity = dynamicBodyEntities.get(i);

                if (!cachedDynamicBodyEntities.contains(entity))
                {
                    dynamicBodies.add(tearUpDynamicBody(entity));
                    cachedDynamicBodyEntities.add(entity);
                }
            }

            // If an entity was removed
            for (int i = 0; i < cachedDynamicBodyEntities.size(); i++)
            {
                // Get entity
                Entity entity = cachedDynamicBodyEntities.get(i);

                if (!dynamicBodyEntities.contains(entity, true))
                {
                    int index = cachedDynamicBodyEntities.indexOf(entity);
                    cachedDynamicBodyEntities.remove(entity);
                    Body body = dynamicBodies.remove(index);
                    world.destroyBody(body);
                }
            }
        }

        // Check if some static bodies were added to the engine
        if (staticBodyEntities.size() != cachedStaticBodyEntities.size())
        {
            // If there is a new entity
            for (int i = 0; i < staticBodyEntities.size(); i++)
            {
                // Get entity
                Entity entity = staticBodyEntities.get(i);

                if (!cachedStaticBodyEntities.contains(entity))
                {
                    staticBodies.add(tearUpStaticBody(entity));
                    cachedStaticBodyEntities.add(entity);
                }
            }

            // If an entity was removed
            for (int i = 0; i < cachedStaticBodyEntities.size(); i++)
            {
                // Get entity
                Entity entity = cachedStaticBodyEntities.get(i);

                if (!staticBodyEntities.contains(entity, true))
                {
                    int index = cachedStaticBodyEntities.indexOf(entity);
                    cachedStaticBodyEntities.remove(entity);
                    Body body = staticBodies.remove(index);
                    world.destroyBody(body);
                }
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

            Body body = dynamicBodies.get(i);
            transformComponent.position = body.getPosition();
            body.setGravityScale(dynamicBodyComponent.gravityScale);

            // Process impulses
            body.applyLinearImpulse(dynamicBodyComponent.impulseHorizontal,
                    dynamicBodyComponent.impulseVertical,
                    transformComponent.position.x,
                    transformComponent.position.y,
                    true);

            //dynamicBodyComponent.impulseHorizontal = 0.0f;
            //dynamicBodyComponent.impulseVertical = 0.0f;
        }
    }


    private Body tearUpDynamicBody(Entity entity)
    {
        DynamicBodyComponent dynamicBodyComponent
                = dynamicBodyComponentComponentMapper.get(entity);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(dynamicBodyComponent.center);

        // Create body
        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        // Create a box (polygon) shape
        PolygonShape polygon = new PolygonShape();

        // Set the polygon shape as a box
        polygon.setAsBox(dynamicBodyComponent.width/2, dynamicBodyComponent.height/2);

        // Create a fixture definition to apply the shape to it
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = dynamicBodyComponent.density;
        fixtureDef.friction = dynamicBodyComponent.friction;
        fixtureDef.restitution = dynamicBodyComponent.restitution;

        // Setup collision category and mask based on entity flags
        switch (entity.flags)
        {
            // Default
            case 1:
                fixtureDef.filter.categoryBits = CollisionCategory.DEFAULT.bits;
                fixtureDef.filter.maskBits = CollisionMask.DEFAULT.bits;
            // Player
            case 2:
                fixtureDef.filter.categoryBits = CollisionCategory.PLAYER.bits;
                fixtureDef.filter.maskBits = CollisionMask.PLAYER.bits;
            // Enemy
            case 4:
                fixtureDef.filter.categoryBits = CollisionCategory.ENEMY.bits;
                fixtureDef.filter.maskBits = CollisionMask.ENEMY.bits;
            // Bullet
            case 8:
                fixtureDef.filter.categoryBits = CollisionCategory.BULLET.bits;
                fixtureDef.filter.maskBits = CollisionMask.BULLET.bits;
            // Power up
            case 16:
                fixtureDef.filter.categoryBits = CollisionCategory.POWERUP.bits;
                fixtureDef.filter.maskBits = CollisionMask.POWERUP.bits;

        }

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);


        // Pass entity for collision detection
        fixture.setUserData(entity);


        // Return body
        return body;
    }

    private Body tearUpStaticBody(Entity entity)
    {
        StaticBodyComponent staticBodyComponent
                = staticBodyComponentComponentMapper.get(entity);

        // Creating physics body representation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(staticBodyComponent.center);

        // Add it to the world
        Body body = world.createBody(bodyDef);

        // Create a box (polygon) shape
        PolygonShape polygon = new PolygonShape();

        // Set the polygon shape as a box
        polygon.setAsBox(staticBodyComponent.width, staticBodyComponent.height);

        // Create a fixture definition to apply the shape to it
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = staticBodyComponent.density;
        fixtureDef.friction = staticBodyComponent.friction;
        fixtureDef.restitution = staticBodyComponent.restitution;

        // Setup collision category and mask based on entity flags
        switch (entity.flags)
        {
            // Default
            case 1:
                fixtureDef.filter.categoryBits = CollisionCategory.DEFAULT.bits;
                fixtureDef.filter.maskBits = CollisionMask.DEFAULT.bits;
                // Player
            case 2:
                fixtureDef.filter.categoryBits = CollisionCategory.PLAYER.bits;
                fixtureDef.filter.maskBits = CollisionMask.PLAYER.bits;
                // Enemy
            case 4:
                fixtureDef.filter.categoryBits = CollisionCategory.ENEMY.bits;
                fixtureDef.filter.maskBits = CollisionMask.ENEMY.bits;
                // Bullet
            case 8:
                fixtureDef.filter.categoryBits = CollisionCategory.BULLET.bits;
                fixtureDef.filter.maskBits = CollisionMask.BULLET.bits;
                // Power up
            case 16:
                fixtureDef.filter.categoryBits = CollisionCategory.POWERUP.bits;
                fixtureDef.filter.maskBits = CollisionMask.POWERUP.bits;

        }

        // Create a fixture from the box and add it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Pass entity for collision detection
        fixture.setUserData(entity);

        // Return body
        return body;
    }
}
