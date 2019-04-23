package com.thependulumparadox.control;

import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.IEvent;

public abstract class ControlModule
{
    public final IEvent<EventArgs> leftStart;
    public final IEvent<EventArgs> rightStart;
    public final IEvent<EventArgs> jumpStart;
    public final IEvent<EventArgs> attackStart;

    public final IEvent<EventArgs> left;
    public final IEvent<EventArgs> right;
    public final IEvent<EventArgs> jump;
    public final IEvent<EventArgs> attack;

    public final IEvent<EventArgs> leftEnd;
    public final IEvent<EventArgs> rightEnd;
    public final IEvent<EventArgs> jumpEnd;
    public final IEvent<EventArgs> attackEnd;

    public ControlModule()
    {
        leftStart = new Event<EventArgs>();
        rightStart = new Event<EventArgs>();
        jumpStart = new Event<EventArgs>();
        attackStart = new Event<EventArgs>();

        left = new Event<EventArgs>();
        right = new Event<EventArgs>();
        jump = new Event<EventArgs>();
        attack = new Event<EventArgs>();

        leftEnd = new Event<EventArgs>();
        rightEnd = new Event<EventArgs>();
        jumpEnd = new Event<EventArgs>();
        attackEnd = new Event<EventArgs>();
    }

    public abstract void update(float delta);
}