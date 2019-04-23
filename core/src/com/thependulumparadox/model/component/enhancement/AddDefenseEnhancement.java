package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;


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
        attributes.defense += bonusDefense;
        super.apply(attributes);
    }
}
