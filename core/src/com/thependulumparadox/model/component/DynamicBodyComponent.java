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

public class DynamicBodyComponent implements Component
{
    public final Body body;
    public boolean initialized = false;

    public DynamicBodyComponent(World world)
    {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(0,0));

        // Create body
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        // Create a box (polygon) shape
        PolygonShape polygon = new PolygonShape();

        // Set the polygon shape as a box
        polygon.setAsBox(1,1);

        // Create a fixture definition to apply the shape to it
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);
        body.setActive(false);
    }

    public DynamicBodyComponent position(Vector2 position)
    {
        body.setTransform(position, 0);
        return this;
    }

    public DynamicBodyComponent dimension(float width, float height)
    {
        ((PolygonShape)body.getFixtureList().first()
                .getShape()).setAsBox(width/2.0f, height/2.0f);

        return this;
    }

    public DynamicBodyComponent properties(float density, float friction, float restitution)
    {
        body.getFixtureList().first().setDensity(density);
        body.getFixtureList().first().setFriction(friction);
        body.getFixtureList().first().setRestitution(restitution);
        return this;
    }

    public DynamicBodyComponent gravityScale(float scale)
    {
        body.setGravityScale(scale);
        return this;
    }

    public void activate(boolean activate)
    {
        body.setActive(activate);
    }
}
