package com.thependulumparadox.observer;

/**
 * Implementation of EventArgs that actually carries some data
 * @see IEvent
 * @see EventArgs
 * @param <T>
 */
public class ValueEventArgs<T> extends EventArgs
{
    public final T value;

    public ValueEventArgs(T value)
    {
        this.value = value;
    }
}
