package com.pendulumparadox.state;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Callable;

public class EntityTransition<T extends IState,U extends IState, I extends Callable<Boolean>> implements ITransition<T,U>
{
    private final T from;
    private final U to;
    private final Callable<Boolean> callable;
    public EntityTransition(T from, U to, Callable<Boolean> callable)
    {
        this.from = from;
        this.to = to;
        this.callable = callable;
    }

    private void attemptTransition() {

    }
    @Override
    public T getFrom()
    {
        return from;
    }

    @Override
    public U getTo()
    {
        return to;
    }
}
