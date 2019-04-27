package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thependulumparadox.model.system.PhysicsSystem;

/**
 * Component that provides entity with physics.
 * It simulates dynamic body == able to move around and interact.
 */
public class DynamicBodyComponent implements Component
{
    public Body body;
    public boolean initialized = false;
    public boolean toDestroy = false;

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

        // Dispose shape
        polygon.dispose();
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

    public DynamicBodyComponent properties(int fixtureIndex, float density,
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

    public DynamicBodyComponent gravityScale(float scale)
    {
        body.setGravityScale(scale);
        return this;
    }

    public DynamicBodyComponent addTrigger(float radius)
    {
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(radius);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;

        Fixture fixture = body.createFixture(fixtureDef);
        circleShape.dispose();

        return this;
    }

    public DynamicBodyComponent makeTrigger()
    {
        body.getFixtureList().first().setSensor(true);
        return this;
    }

    public void activate(boolean activate)
    {
        body.setActive(activate);
    }

    public void wakeup()
    {
        body.setAwake(true);
    }
}
