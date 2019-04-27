package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that stores interactions/collisions with other entities
 * in order to process them in a system and execute actions
 */
public class InteractionComponent implements Component
{
    public List<Entity> interactions;

    public InteractionComponent()
    {
        interactions = new ArrayList<>();
    }
}
