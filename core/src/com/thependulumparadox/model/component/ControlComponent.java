package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.control.ControlModule;

/**
 * Component that links control module to entity and provides various settings
 * to fine tune sensitivity, speed limits etc.
 */
public class ControlComponent implements Component
{
    public final ControlModule controlModule;
    public boolean facingRight = true;

    public float maxMoveLeftSpeed = 6.0f;
    public float moveLeftImpulse = 1.0f;
    public float maxMoveRightSpeed = 6.0f;
    public float moveRightImpulse = 1.0f;

    public float jumpImpulse = 10.0f;
    public float jumpLimitSpeedThreshold = 0.1f;

    public float shootImpulse = 8f;

    public float backToIdleSpeedThreshold = 0.1f;
    public float backToIdleTime = 0.1f;

    public ControlComponent(ControlModule controlModule)
    {
        this.controlModule = controlModule;
    }
}
