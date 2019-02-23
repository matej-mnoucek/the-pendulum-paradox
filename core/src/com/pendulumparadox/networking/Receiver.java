package com.pendulumparadox.networking;

import com.pendulumparadox.observer.EventArgs;
import com.pendulumparadox.observer.IEvent;

public abstract class Receiver
{
    IEvent<EventArgs> receive;
}
