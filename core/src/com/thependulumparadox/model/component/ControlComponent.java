package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.control.ControlModule;

public class ControlComponent implements Component
{
    public final ControlModule controlModule;
    public boolean facingRight = true;

    public ControlComponent(ControlModule controlModule)
    {
        this.controlModule = controlModule;
    }
}
