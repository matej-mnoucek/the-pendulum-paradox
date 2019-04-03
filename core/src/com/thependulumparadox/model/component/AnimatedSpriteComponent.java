package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;

public class AnimatedSpriteComponent implements Component
{
    public String atlasPath = "";
    public String region = "";
    public float frameDuration = 1.0f;

    public float width = 1;
    public float height = 1;
}
