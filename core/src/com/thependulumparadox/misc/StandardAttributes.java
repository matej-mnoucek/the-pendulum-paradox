package com.thependulumparadox.misc;

public class StandardAttributes implements Cloneable
{
    public int lives = 2;
    public float health = 100;
    public float defense = 100;
    public float damage = 50;
    public float speed = 10;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
