package com.thependulumparadox.command;

/**
 * Base interface for Command design pattern
 * @see <a href="https://www.geeksforgeeks.org/command-pattern/">Command</a>
 */
public interface ICommand
{
    boolean execute(float delta);
}
