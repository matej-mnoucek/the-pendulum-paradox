package com.thependulumparadox.observer;

public class ValueEventArgs<T> extends EventArgs
{
    public final T value;

    public ValueEventArgs(T value)
    {
        this.value = value;
    }
}
