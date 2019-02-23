package com.pendulumparadox.model.system.enhancement;

import com.badlogic.ashley.core.Entity;
import com.pendulumparadox.model.component.LifeComponent;

/**
 * Example of modifier that adds some extra lifes
 */
public class LifeEnhancement extends Enhancement
{
    float bonusLifes = 0;

    public LifeEnhancement(Entity entity, int bonusLifes)
    {
        super(entity);
        this.bonusLifes = bonusLifes;
    }

    @Override
    public void apply()
    {
        LifeComponent life = entity.getComponent(LifeComponent.class);
        if (life != null)
        {
            life.lifes += bonusLifes;
        }

        super.apply();
    }
}
