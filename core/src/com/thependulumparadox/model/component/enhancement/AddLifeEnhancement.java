package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;

/**
 * Implementation of enhancement that adds some amount of lives
 * This enhancement is permanent (not limited by time)
 * @see Enhancement
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
