package com.pendulumparadox.state;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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

    @Override
    public T getFrom()
    {
        return from;
    }

    @Override
    public U getTo()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Boolean> future = executor.submit(callable);
            if (future.get()){

            }
        } catch (Exception e) {
            // TODO: Should be smarter ways to do a dynamic entitystate system, but would require a rework of how statemachine works
        }

        return to;
    }
}
