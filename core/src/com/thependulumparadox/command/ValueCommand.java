package com.thependulumparadox.command;

public class ValueCommand<T> implements ICommand
{
    private T variable;
    private T value;

    public ValueCommand(T variable, T value)
    {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public boolean execute(float delta)
    {
        variable = value;
        return true;
    }
}
