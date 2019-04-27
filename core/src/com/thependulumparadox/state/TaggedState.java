package com.thependulumparadox.state;

/**
 * Simple state implementation that stores String
 * @see IState
 */
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
