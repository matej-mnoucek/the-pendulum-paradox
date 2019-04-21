package com.thependulumparadox.command;

import com.thependulumparadox.observer.IEvent;

public class InvokeCommand implements ICommand
{
    private IEvent event;
    private float repeatDuration;
    private float currentDuration;

    public InvokeCommand(IEvent event)
    {
        this.event = event;
        this.repeatDuration = 0.0f;
        this.currentDuration = 0.0f;
    }

    public InvokeCommand(IEvent event, float repeatDuration)
    {
        this.event = event;
        this.repeatDuration = repeatDuration;
        this.currentDuration = repeatDuration;
    }

    @Override
    public boolean execute(float delta)
    {
        event.invoke(null);
        currentDuration -= delta;

        if (currentDuration <= 0.0f)
        {
            currentDuration = repeatDuration;
            return true;
        }
        else
        {
            return false;
        }
    }
}
