package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;
import com.thependulumparadox.model.component.PlayerComponent;

/**
 * Implementation of enhancement that multiplies damage by arbitrary number
 * This enhancement is just temporal == time limited
 * @see Enhancement
 */
public class MultiplyDamageEnhancement extends Enhancement
{
    private float multiplyFactor = 0;

    public MultiplyDamageEnhancement(float multiplyFactor, float duration)
    {
        super(duration);
        this.multiplyFactor = multiplyFactor;
    }

    @Override
    public void apply(StandardAttributes attributes)
    {
        if (duration > 0.0f)
            attributes.damage *= multiplyFactor;

        super.apply(attributes);
    }
}
