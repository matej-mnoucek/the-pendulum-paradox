package com.thependulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.AbstractEntityFactory;

/**
 * Factory for all components (makes component creation easier)
 */
public class EntityFactory extends AbstractEntityFactory
{
    @Override
    public Entity create(String entity)
    {
        switch(entity)
        {
            case "XXX":
                System.out.println("XXX");
        }

        return null;
    }
}
