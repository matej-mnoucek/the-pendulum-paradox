package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;

/**
 * Factory for all components (makes component creation easier)
 */
public class ComponentFactory extends AbstractComponentFactory
{
    @Override
    public <T extends Component> T create(Class<T> component)
    {
        if (component == TransformComponent.class)
        {

        }



        return null;
    }
}
