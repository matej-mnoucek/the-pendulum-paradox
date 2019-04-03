package com.thependulumparadox.model.system.enhancement;

import com.badlogic.ashley.core.Entity;

import com.thependulumparadox.model.component.AttributesComponent;

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
        AttributesComponent life = entity.getComponent(AttributesComponent.class);
        if (life != null)
        {
            life.lives += bonusLifes;
        }

        super.apply();
    }
}
