package com.pendulumparadox.observer;

@FunctionalInterface
public interface IEventHandler<T>
{
    void handle(T args);
}
