package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.thependulumparadox.misc.Pair;
import com.thependulumparadox.misc.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that changes appearance of player based on current player stats (add holo shield)
 * @see BulletVisualsComponent
 */
public class EnhancementVisualsComponent implements Component
{
    public final List<Pair<Range<Float>, Sprite>> defenseSprites;

    public EnhancementVisualsComponent()
    {
        this.defenseSprites = new ArrayList<>();
    }

    public EnhancementVisualsComponent addDefense(Range<Float> defenseRange, String spritePath)
    {
        Sprite sprite = new Sprite(new Texture(spritePath));
        defenseSprites.add(new Pair<>(defenseRange, sprite));

        return this;
    }
}
