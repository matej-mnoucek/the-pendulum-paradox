package com.thependulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Abstract factory interface
 */
public abstract class AbstractEntityFactory
{
    public abstract Entity create(String entity);
}
