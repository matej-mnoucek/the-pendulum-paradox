package com.thependulumparadox.observer;

/**
 * Interface for simple events (observer pattern)
 * @param <T>
 */
public interface IEvent<T>
{
    void invoke(T args);
    void addHandler(IEventHandler<T> handler);
    void removeHandler(IEventHandler<T> handler);
    void removeAllHandlers();
}
