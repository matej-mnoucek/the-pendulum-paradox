package com.thependulumparadox.misc;

public class StandardAttributes implements Cloneable
{
    public int lives = 3;
    public float health = 100;
    public float defense = 100;
    public float damage = 50;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
