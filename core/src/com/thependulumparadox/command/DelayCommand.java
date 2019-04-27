package com.thependulumparadox.command;

/**
 * Implementation of delay command, simply waits for given amount of seconds
 */
public class DelayCommand implements ICommand
{
    // Delay in seconds
    private final float delay;
    private float current;

    public DelayCommand(float delay)
    {
        this.delay = delay;
        this.current = delay;
    }

    @Override
    public boolean execute(float delta)
    {
        current -= delta;

        if (current <= 0.0f)
        {
            current = delay;
            return true;
        }
        else
        {
            return false;
        }
    }
}
