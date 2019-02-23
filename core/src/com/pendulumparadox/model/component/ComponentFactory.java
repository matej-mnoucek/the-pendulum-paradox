package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Factory for all components (makes component creation easier)
 */
public class ComponentFactory extends AbstractComponentFactory
{
    @Override
    public <T extends Component> T create(Class<T> component)
    {
        return null;
    }
}
