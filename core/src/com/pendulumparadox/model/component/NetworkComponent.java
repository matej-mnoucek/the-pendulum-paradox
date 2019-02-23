package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.pendulumparadox.networking.Receiver;
import com.pendulumparadox.networking.Sender;

public class NetworkComponent implements Component
{
    public Receiver receiver;
    public Sender sender;
}
