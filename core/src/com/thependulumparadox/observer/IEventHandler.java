package com.thependulumparadox.observer;

/**
 * Functional interface for defining a block of code that runs when the event is invoked
 * @param <T>
 */
@FunctionalInterface
public interface IEventHandler<T>
{
    void handle(T args);
}
