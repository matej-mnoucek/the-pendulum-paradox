package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Component that provides entity with physics.
 * It simulates static body == NOT able to move around.
 * Mainly used as collision zones for level geometry
 */
public class StaticBodyComponent implements Component
{
    public Body body;
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

        // Dispose shape
        polygon.dispose();
    }

    public StaticBodyComponent position(Vector2 position)
    {
        body.setTransform(position, 0);
        return this;
    }

    public StaticBodyComponent dimension(float width, float height)
    {
        ((PolygonShape)body.getFixtureList().first()
                .getShape()).setAsBox(width/2.0f, height/2.0f);

        return this;
    }

    public StaticBodyComponent properties(int fixtureIndex, float density,
                                           float friction, float restitution)
    {
        Array<Fixture> fixtures = body.getFixtureList();
        if (fixtureIndex < fixtures.size)
        {
            fixtures.get(fixtureIndex).setDensity(density);
            fixtures.get(fixtureIndex).setFriction(friction);
            fixtures.get(fixtureIndex).setRestitution(restitution);
        }
        return this;
    }

    public StaticBodyComponent gravityScale(float scale)
    {
        body.setGravityScale(scale);
        return this;
    }

    public StaticBodyComponent addTrigger(float radius)
    {
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(radius);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        circleShape.dispose();

        return this;
    }

    public StaticBodyComponent makeTrigger()
    {
        body.getFixtureList().first().setSensor(true);
        return this;
    }

    public void activate(boolean activate)
    {
        body.setActive(activate);
    }
}
