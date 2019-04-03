package com.thependulumparadox.observer;

@FunctionalInterface
public interface IEventHandler<T>
{
    void handle(T args);
}
