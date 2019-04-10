package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component
{
    public int index = 0;
    public int lives = 3;
    public int defense = 100;
    public int damage = 100;
    public int speed = 10;
}
