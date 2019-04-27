package com.thependulumparadox.state;

/**
 * Implementation of a simple state that stores object
 * @param <T> Generic parameter for defining which object is stored in the state
 * @see IState
 */
public class State<T> implements IState
{
    public final T object;

    public State(T object)
    {
        this.object = object;
    }

    @Override
    public void execute()
    {
        // Not necessary
    }
}
