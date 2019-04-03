package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
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

    private World world;

    public PhysicsSystem(World world) { this.world = world; }

    public void addedToEngine(Engine engine)
    {
        staticBodyEntities = engine.getEntitiesFor(Family.all(StaticBodyComponent.class,
                TransformComponent.class).get());
        dynamicBodyEntities = engine.getEntitiesFor(Family.all(DynamicBodyComponent.class,
                TransformComponent.class).get());

        for (int i = 0; i < staticBodyEntities.size(); i++)
        {
            Entity entity = staticBodyEntities.get(i);
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

            // Create a fixture from the box and add it to the body
            body.createFixture(fixtureDef);

            // Add it to the list
            staticBodies.add(body);
        }

        for (int i = 0; i < dynamicBodyEntities.size(); i++)
        {
            Entity entity = dynamicBodyEntities.get(i);
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

            // Create our fixture and attach it to the body
            body.createFixture(fixtureDef);

            // Add to the list
            dynamicBodies.add(body);
        }
    }

    public void removedFromEngine (Engine engine)
    {
        
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
    }

    public void update(float deltaTime)
    {
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

            // Process impulses
            body.applyLinearImpulse(dynamicBodyComponent.impulseHorizontal,
                    dynamicBodyComponent.impulseVertical,
                    transformComponent.position.x,
                    transformComponent.position.y,
                    true);

            dynamicBodyComponent.impulseHorizontal = 0.0f;
            dynamicBodyComponent.impulseVertical = 0.0f;
        }
    }
}
