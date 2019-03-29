package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class AnimatedSpriteComponent implements Component
{
    public String atlasPath = "";
    public String region = "";
    public float frameDuration = 1.0f;

    public float width = 1;
    public float height = 1;
}
