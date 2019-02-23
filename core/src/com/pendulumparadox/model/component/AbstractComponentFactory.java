package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;

/**
 * Abstract factory interface
 */
public abstract class AbstractComponentFactory
{
    abstract <T extends Component> T create(Class<T> component);
}
