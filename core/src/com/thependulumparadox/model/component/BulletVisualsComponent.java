package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.thependulumparadox.misc.Pair;
import com.thependulumparadox.misc.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that changes appearance of bullets based on current player stats
 * (Stats can be changed by enhancements, so that would lead to different projectile sprites)
 */
public class BulletVisualsComponent implements Component
{
    public final List<Pair<Range<Float>, Sprite>> bulletSprites;
    public final Sprite defaultSprite;
    public Sprite currentSprite;

    public BulletVisualsComponent(String defaultSpritePath)
    {
        this.bulletSprites = new ArrayList<>();
        this.defaultSprite = new Sprite(new Texture(defaultSpritePath));
        this.currentSprite = defaultSprite;
    }

    public BulletVisualsComponent add(Range<Float> damageRange, String spritePath)
    {
        Sprite sprite = new Sprite(new Texture(spritePath));
        bulletSprites.add(new Pair<>(damageRange, sprite));

        return this;
    }
}
