package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class StaticBodyComponent implements Component
{
    public float width = 1.0f;
    public float height = 1.0f;
    public Vector2 center = new Vector2(0,0);

    public float density = 1.0f;
    public float friction = 0.0f;
    public float restitution = 0.0f;
}
