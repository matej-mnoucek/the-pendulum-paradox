package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.misc.StandardAttributes;

/**
 * Component that identifies enemy and defines its basic attributes
 * and score you get for killing it.
 */
public class EnemyComponent implements Component
{
    public StandardAttributes base;
    public StandardAttributes current;

    public int score;

    public EnemyComponent()
    {
        base = new StandardAttributes();
        current = new StandardAttributes();
        score = 5;
    }
}
