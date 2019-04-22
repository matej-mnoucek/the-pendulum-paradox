package com.thependulumparadox.command;

public interface ICommandQueue
{
    void loop(boolean loop);
    boolean add(ICommand command);
    boolean remove(ICommand command);
    boolean update(float delta);
}
