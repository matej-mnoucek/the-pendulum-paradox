package com.thependulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Abstract entity factory interface
 * Used for creating and tearing up most of the entities in the game
 */
public abstract class AbstractEntityFactory
{
    public abstract Entity create(String entity);
}
