package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple component that allows entity to have rotation, position and scale in 2D world
 */
public class TransformComponent implements Component
{
    public Vector2 position;
    public float rotation;
    public Vector2 scale;

    public TransformComponent()
    {
        this.position = new Vector2(0,0);
        this.scale = new Vector2(0,0);
        this.rotation = 0;
    }
}
