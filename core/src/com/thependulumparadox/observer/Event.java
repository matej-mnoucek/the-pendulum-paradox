package com.thependulumparadox.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Event implementation
 * @see IEvent
 * @param <T> Event arguments class
 */
public class Event<T extends EventArgs> implements IEvent<T>
{
    private List<IEventHandler> handlers = new ArrayList<>();

    @Override
    public void invoke(T args)
    {

        IEventHandler<T> hand = (T ar)->{};

        for (IEventHandler<T> handler : handlers)
        {
            handler.handle(args);
        }
    }

    @Override
    public void addHandler(IEventHandler<T> handler)
    {
        handlers.add(handler);
    }

    @Override
    public void removeHandler(IEventHandler<T> handler)
    {
        handlers.remove(handler);
    }

    @Override
    public void removeAllHandlers()
    {
        handlers.clear();
    }
}
