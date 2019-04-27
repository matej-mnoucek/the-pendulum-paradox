package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Simple debug system that visualizes physics bodies
 */
public class PhysicsDebugSystem extends EntitySystem
{
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private World world;

    public PhysicsDebugSystem(World world, OrthographicCamera camera)
    {
        this.debugRenderer = new Box2DDebugRenderer();
        this.world = world;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime)
    {
        debugRenderer.render(world, camera.combined);
    }
}
