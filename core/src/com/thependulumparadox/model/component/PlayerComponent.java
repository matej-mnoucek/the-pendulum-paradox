package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.misc.StandardAttributes;

public class PlayerComponent implements Component
{
    public StandardAttributes base;
    public StandardAttributes current;

    public PlayerComponent()
    {
        base = new StandardAttributes();
        current = new StandardAttributes();
    }
}