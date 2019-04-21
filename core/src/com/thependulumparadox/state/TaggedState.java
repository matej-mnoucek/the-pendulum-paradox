package com.thependulumparadox.state;

public class TaggedState implements IState
{
    public final String tag;

    public TaggedState(String tag)
    {
        this.tag = tag;
    }

    @Override
    public void execute()
    {
        // Not necessary
    }
}
