package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.model.component.enhancement.Enhancement;

public class EnhancementComponent implements Component
{
    public final Enhancement enhancement;

    public EnhancementComponent(Enhancement enhancement)
    {
        this.enhancement = enhancement;
    }
}
