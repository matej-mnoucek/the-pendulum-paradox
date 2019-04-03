package com.thependulumparadox.observer;

public interface IEvent<T>
{
    void invoke(T args);
    void addHandler(IEventHandler<T> handler);
    void removeHandler(IEventHandler<T> handler);
    void removeAllHandlers();
}
