package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component
{
    public Vector2 position;
    public float rotation;
    public Vector2 scale;

    public TransformComponent(Vector2 position){
        this.position = position;
    }

    public TransformComponent(Vector2 position, float rotation){
        this.position = position;
        this.rotation = rotation;
    }

    public TransformComponent(Vector2 position, float rotation, Vector2 scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
}
