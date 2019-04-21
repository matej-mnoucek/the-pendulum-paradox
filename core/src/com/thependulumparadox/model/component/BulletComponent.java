package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class BulletComponent implements Component
{
    public final Entity shotBy;

    public BulletComponent(Entity shotBy)
    {
        this.shotBy = shotBy;
    }
}
