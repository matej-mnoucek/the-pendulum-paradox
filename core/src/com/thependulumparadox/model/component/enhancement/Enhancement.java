package com.thependulumparadox.model.component.enhancement;

import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.misc.StandardAttributes;

/**
 * Base class for enhancements (e.g. power-ups, special ammo)
 * Uses chain of responsibility pattern for chaining more of them and applying to a player
 */
public abstract class Enhancement
{
    protected Enhancement next;
    protected float duration;
    protected boolean permanent;

    public Enhancement()
    {
        this.permanent = true;
        this.duration = 0.0f;
    }

    public Enhancement(float duration)
    {
        this.permanent = false;
        this.duration = duration;
    }

    public void chain(Enhancement enhancement)
    {
        if (next != null)
        {
            next.chain(enhancement);
        }
        else
        {
            next = enhancement;
        }
    }

    public void apply(StandardAttributes attributes)
    {
        if (next != null)
        {
            next.apply(attributes);
        }
    }

    public void step(float delta)
    {
        // Subtract delta
        if (duration > 0)
            duration -= delta;

        if (next != null)
        {
            next.step(delta);
        }
    }

    public boolean isPermanent()
    {
        return permanent;
    }
}
