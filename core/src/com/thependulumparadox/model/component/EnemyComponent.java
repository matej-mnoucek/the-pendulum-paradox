package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.misc.StandardAttributes;

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
