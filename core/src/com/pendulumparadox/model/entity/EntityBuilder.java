package com.pendulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.pendulumparadox.model.component.NameComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity builder for easy creation of named entities from a list of components
 */
public class EntityBuilder implements IEntityBuilder
{
    List<Component> components = new ArrayList<Component>();

    @Override
    public Entity create(String name)
    {
        NameComponent nameComponent = new NameComponent();
        nameComponent.name = name;
        components.add(0, nameComponent);

        Entity entity = new Entity();
        for (Component component : components)
        {
            entity.add(component);
        }
        components.clear();

        return entity;
    }

    @Override
    public EntityBuilder add(Component component)
    {
        components.add(component);
        return this;
    }

    @Override
    public EntityBuilder clear()
    {
        components.clear();
        return this;
    }
}
