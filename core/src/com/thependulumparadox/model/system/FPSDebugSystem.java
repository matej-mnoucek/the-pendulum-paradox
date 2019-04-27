package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Simple debug system for logging FPS every frame
 */
public class FPSDebugSystem extends EntitySystem
{
    private FPSLogger fpsLogger = new FPSLogger();

    @Override
    public void update(float deltaTime)
    {
        fpsLogger.log();
    }
}
