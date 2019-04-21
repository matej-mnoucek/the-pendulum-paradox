package com.thependulumparadox.command;

public interface ICommandQueue
{
    boolean add(ICommand command);
    boolean remove(ICommand command);
    boolean update(float delta);
}
