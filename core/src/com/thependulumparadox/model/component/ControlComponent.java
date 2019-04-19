package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;

public class ControlComponent implements Component
{
    public final ControlModule controlModule;

    public ControlComponent(ControlModule controlModule)
    {
        this.controlModule = controlModule;
    }
}
