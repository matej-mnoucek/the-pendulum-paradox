package com.thependulumparadox.model.system.enhancement;

import com.badlogic.ashley.core.Entity;

/**
 * Base class for enhancements (e.g. power-ups, special ammo)
 * using chain of responsibility pattern
 */
public abstract class Enhancement
{
    protected Entity entity;
    protected Enhancement next;

    public Enhancement(Entity entity)
    {
        this.entity = entity;
    }

    public void chain(Enhancement enhancement)
    {
        if (!next.equals(null))
        {
            next.chain(enhancement);
        }
        else
        {
            next = enhancement;
        }
    }

    public void apply()
    {
        if (!next.equals(null))
        {
            next.apply();
        }
    }
}
