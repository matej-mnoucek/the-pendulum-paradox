package com.pendulumparadox.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Event<T extends EventArgs> implements IEvent<T>
{
    private List<IEventHandler> handlers = new ArrayList<>();

    @Override
    public void invoke(T args)
    {
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
