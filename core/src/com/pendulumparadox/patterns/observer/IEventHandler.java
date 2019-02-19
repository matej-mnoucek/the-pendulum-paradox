package com.pendulumparadox.patterns.observer;

public interface IEventHandler<T>
{
    void handle(T args);
}
