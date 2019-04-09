package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class DynamicBodyComponent implements Component
{
    public float width = 1.0f;
    public float height = 1.0f;
    public Vector2 center = new Vector2(0,0);

    public float density = 40.0f;
    public float friction = 0.3f;
    public float restitution = 0.1f;

    public float impulseVertical = 0.0f;
    public float impulseHorizontal = 0.0f;
}
