package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.model.system.PhysicsSystem;

public class StaticBodyComponent implements Component
{
    /*
    public float width = 1.0f;
    public float height = 1.0f;
    public Vector2 center = new Vector2(0,0);

    public float density = 1.0f;
    public float friction = 0.0f;
    public float restitution = 0.0f;

    public short collisionGroup = 0;
    */

    public final Body body;
    public boolean initialized = false;

    public StaticBodyComponent(World world)
    {
        // Creating physics body representation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(0,0));

        // Add it to the world
        body = world.createBody(bodyDef);

        // Create a box (polygon) shape
        PolygonShape polygon = new PolygonShape();

        // Set the polygon shape as a box
        polygon.setAsBox(1, 1);

        // Create a fixture definition to apply the shape to it
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;

        // Create a fixture from the box and add it to the body
        body.createFixture(fixtureDef);
        body.setActive(false);
    }

    public StaticBodyComponent position(float x, float y)
    {
        body.setTransform(new Vector2(x,y), 0);
        return this;
    }

    public StaticBodyComponent dimension(float width, float height)
    {
        ((PolygonShape)body.getFixtureList().first()
                .getShape()).setAsBox(width/2.0f, height/2.0f);

        return this;
    }

    public StaticBodyComponent properties(float density, float friction, float restitution)
    {
        body.getFixtureList().first().setDensity(density);
        body.getFixtureList().first().setFriction(friction);
        body.getFixtureList().first().setRestitution(restitution);
        return this;
    }

    public StaticBodyComponent gravityScale(float scale)
    {
        body.setGravityScale(scale);
        return this;
    }

    public void activate(boolean activate)
    {
        body.setActive(activate);
    }
}
