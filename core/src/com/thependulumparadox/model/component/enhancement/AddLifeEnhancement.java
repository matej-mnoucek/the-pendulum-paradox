package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;

/**
 * Example of modifier that adds some extra lifes
 */
public class AddLifeEnhancement extends Enhancement
{
    private float bonusLifes = 0;

    public AddLifeEnhancement(int bonusLifes)
    {
        this.bonusLifes = bonusLifes;
    }

    @Override
    public void apply(StandardAttributes attributes)
    {
        attributes.lives += bonusLifes;
        super.apply(attributes);
    }
}
