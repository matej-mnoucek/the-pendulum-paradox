package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Component that provides sprite visual to the corresponding entity
 */
public class SpriteComponent implements Component
{
    public Sprite sprite;
    public float width = 1.0f;
    public float height = 1.0f;

    public float rotationSpeed = 0.0f;
    public float rotationAngle = 1.0f;

    public SpriteComponent(String spritePath)
    {
        // Possibility to create empty sprite component
        if (spritePath == null)
        {
            sprite = null;
            return;
        }

        sprite = new Sprite(new Texture(spritePath));
    }
}
