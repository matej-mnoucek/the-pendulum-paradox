package com.thependulumparadox.state;

public class TaggedState<T> implements IState
{
    public final T object;
    public final String tag;

    public TaggedState(String tag, T object)
    {
        this.object = object;
        this.tag = tag;
    }

    @Override
    public void execute()
    {
        // Not necessary
    }
}
