package com.pendulumparadox.observer;

public interface IEventHandler<T>
{
    void handle(T args);
}
