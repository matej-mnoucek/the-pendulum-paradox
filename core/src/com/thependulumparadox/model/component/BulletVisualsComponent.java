package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.thependulumparadox.misc.Pair;
import com.thependulumparadox.misc.Range;

import java.util.ArrayList;
import java.util.List;

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
