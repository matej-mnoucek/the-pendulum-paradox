package com.thependulumparadox.state;

import com.thependulumparadox.state.IState;

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
