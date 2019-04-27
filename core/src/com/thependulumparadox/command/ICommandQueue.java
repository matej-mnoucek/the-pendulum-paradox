package com.thependulumparadox.command;

/**
 * Command queue that is used for storing and invoking commands
 * Mainly used for defining routines for enemy AI
 * @see ICommand
 */
public interface ICommandQueue
{
    void loop(boolean loop);
    boolean add(ICommand command);
    boolean remove(ICommand command);
    boolean update(float delta);
}
