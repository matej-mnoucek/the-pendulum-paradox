package com.pendulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Entity builder interface
 */
public interface IEntityBuilder
{
    Entity create(String name);
    IEntityBuilder add(Component component);
    IEntityBuilder clear();
}
