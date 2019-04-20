package com.thependulumparadox.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.IEvent;

public class EventControlModule extends ControlModule
{
    private boolean rightButtonPressed = false;
    private boolean leftButtonPressed = false;
    private boolean attackButtonPressed = false;
    private boolean jumpButtonPressed = false;


    // Button pressed
    public void leftStart()
    {
        leftButtonPressed = true;
        leftStart.invoke(null);
    }

    public void rightStart()
    {
        rightButtonPressed = true;
        rightStart.invoke(null);
    }

    public void jumpStart()
    {
        jumpButtonPressed = true;
        jumpStart.invoke(null);
    }

    public void attackStart()
    {
        attackButtonPressed = true;
        attackStart.invoke(null);
    }

    // Button released
    public void leftEnd()
    {
        leftButtonPressed = false;
        leftEnd.invoke(null);
    }

    public void rightEnd()
    {
        rightButtonPressed = false;
        rightEnd.invoke(null);
    }

    public void jumpEnd()
    {
        jumpButtonPressed = false;
        jumpEnd.invoke(null);
    }

    public void attackEnd()
    {
        attackButtonPressed = false;
        attackEnd.invoke(null);
    }

    @Override
    public void update(float delta)
    {
        if (leftButtonPressed)
        {
            left.invoke(null);
        }

        if (rightButtonPressed)
        {
            right.invoke(null);
        }

        if (attackButtonPressed)
        {
            attack.invoke(null);
        }

        if (jumpButtonPressed)
        {
            jump.invoke(null);
        }
    }
}
