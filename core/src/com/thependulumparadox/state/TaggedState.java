package com.thependulumparadox.state;

public class TaggedState<T> implements IState
{
    public final T object;
    public final String tag;

    public TaggedState(T object, String tag)
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
