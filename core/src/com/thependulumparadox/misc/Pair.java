package com.thependulumparadox.misc;

/**
 * Simple class that encapsulates two arbitrary values (e.g. useful for storing key/value pairs)
 * @param <X>
 * @param <Y>
 */
public class Pair<X, Y>
{
    public final X key;
    public final Y value;

    public Pair(X key, Y value)
    {
        this.key = key;
        this.value = value;
    }
}
