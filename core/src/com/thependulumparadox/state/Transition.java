package com.thependulumparadox.state;

/**
 * Basic implementation of transition
 * @see ITransition
 */
public class Transition implements ITransition
{
    private final IState from;
    private final IState to;

    public Transition(IState from, IState to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public IState getFrom()
    {
        return from;
    }

    @Override
    public IState getTo()
    {
        return to;
    }
}
