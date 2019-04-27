package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Component that identifies bullet and also contains the entity who shot the bullet
 */
public class BulletComponent implements Component
{
    public Entity shotBy;

    public BulletComponent(Entity shotBy)
    {
        this.shotBy = shotBy;
    }
}
