package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component
{
    public Vector2 position;
    public float rotation;
    public Vector2 scale;
}