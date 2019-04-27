package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;

/**
 * Implementation of enhancement that add some bonus defense
 * This enhancement is just temporal == time limited
 * @see Enhancement
 */
public class AddDefenseEnhancement extends Enhancement
{
    private float bonusDefense = 0;

    public AddDefenseEnhancement(float bonusDefense, float duration)
    {
        super(duration);
        this.bonusDefense = bonusDefense;
    }

    @Override
    public void apply(StandardAttributes attributes)
    {
        if (duration > 0.0f)
        {
            attributes.defense += bonusDefense;
        }

        super.apply(attributes);
    }
}
