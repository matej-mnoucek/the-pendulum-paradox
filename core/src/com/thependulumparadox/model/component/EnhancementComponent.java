package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.model.component.enhancement.Enhancement;

/**
 * Component that links corresponding entity with enhancement implementation
 */
public class EnhancementComponent implements Component
{
    public final Enhancement enhancement;

    public EnhancementComponent(Enhancement enhancement)
    {
        this.enhancement = enhancement;
    }
}
