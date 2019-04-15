package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;
import com.thependulumparadox.model.component.PlayerComponent;


public class MultiplyDamageEnhancement extends Enhancement
{
    private float multiplyFactor = 0;

    public MultiplyDamageEnhancement(Entity entity, float multiplyFactor, float duration)
    {
        super(duration);
        this.multiplyFactor = multiplyFactor;
    }

    @Override
    public void apply(StandardAttributes attributes)
    {
        attributes.damage *= multiplyFactor;
        super.apply(attributes);
    }
}
