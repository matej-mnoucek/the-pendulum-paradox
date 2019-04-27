package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Component that provides sprite based animation visual to the corresponding entity
 */
public class AnimatedSpriteComponent implements Component
{
    public final HashMap<String, Animation<TextureRegion>> animations;
    public String currentAnimation;

    public float width = 1.0f;
    public float height = 1.0f;

    public AnimatedSpriteComponent(String spriteAtlasPath)
    {
        // Initialize dictionary
        animations = new HashMap<String, Animation<TextureRegion>>();

        // Create atlas and extract regions
        TextureAtlas atlas = new TextureAtlas(spriteAtlasPath);
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        for (int i = 0; i < regions.size; i++) {
            TextureAtlas.AtlasRegion region = regions.get(i);
            Animation<TextureRegion> animation = new Animation<TextureRegion>(1.0f,
                    atlas.findRegions(region.name), Animation.PlayMode.LOOP);

            // Add animation to hash map
            animations.put(region.name, animation);
        }

        // Set current animation
        if (regions.size > 0)
        {
            currentAnimation = regions.first().name;
        }
    }

    public AnimatedSpriteComponent dimension(float width, float height)
    {
        this.width = width;
        this.height = height;
        return this;
    }

    public AnimatedSpriteComponent frameDuration(String animation, float duration)
    {
        if (animations.containsKey(animation))
        {
            animations.get(animation).setFrameDuration(duration);
        }
        return this;
    }

    public AnimatedSpriteComponent frameDuration(float duration)
    {
        for (Animation<TextureRegion> animation : animations.values())
        {
            animation.setFrameDuration(duration);
        }
        return this;
    }

    public AnimatedSpriteComponent playMode(String animation, Animation.PlayMode mode)
    {
        if (animations.containsKey(animation))
        {
            animations.get(animation).setPlayMode(mode);
        }
        return this;
    }

    public AnimatedSpriteComponent playMode(Animation.PlayMode mode)
    {
        for (Animation<TextureRegion> animation : animations.values())
        {
            animation.setPlayMode(mode);
        }
        return this;
    }

    public boolean currentAnimation(String animation)
    {
        if (animations.containsKey(animation))
        {
            currentAnimation = animation;
            return true;
        }
        else
        {
            return false;
        }
    }
}
