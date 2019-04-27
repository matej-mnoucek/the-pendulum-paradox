package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.misc.StandardAttributes;

/**
 * Component that identifies player and defines its basic attributes, his score
 * and id (used for distinguishing between different players in multi player mode)
 */
public class PlayerComponent implements Component
{
    public StandardAttributes base;
    public StandardAttributes current;

    public final int id;
    public int score;

    public PlayerComponent(int id)
    {
        this.id = id;
        defaults();
    }

    public void defaults()
    {
        base = new StandardAttributes();
        current = new StandardAttributes();
        score = 0;
    }
}
