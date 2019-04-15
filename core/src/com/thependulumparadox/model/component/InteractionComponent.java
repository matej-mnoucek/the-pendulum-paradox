package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class InteractionComponent implements Component
{
    public List<Entity> interactions;

    public InteractionComponent()
    {
        interactions = new ArrayList<>();
    }
}
