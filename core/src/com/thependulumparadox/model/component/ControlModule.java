package com.thependulumparadox.model.component;

import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.IEvent;

public abstract class ControlModule
{
    public final IEvent<EventArgs> left;
    public final IEvent<EventArgs> right;
    public final IEvent<EventArgs> jump;
    public final IEvent<EventArgs> attack;

    public ControlModule()
    {
        left = new Event<EventArgs>();
        right = new Event<EventArgs>();
        jump = new Event<EventArgs>();
        attack = new Event<EventArgs>();
    }

    public abstract void update(float delta);
}
