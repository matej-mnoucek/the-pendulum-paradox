package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component
{
    public final Texture sprite;
    public float width = 1.0f;
    public float height = 1.0f;

    public SpriteComponent(String spritePath)
    {
        sprite = new Texture(spritePath);
    }
}
