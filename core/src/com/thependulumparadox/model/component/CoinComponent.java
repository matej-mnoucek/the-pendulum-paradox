package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Component that identifies coin and defines its value
 */
public class CoinComponent implements Component
{
    public float value = 10.0f;
}
