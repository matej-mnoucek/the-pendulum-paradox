package com.pendulumparadox.controll;

import com.pendulumparadox.observer.Event;
import com.pendulumparadox.observer.IEvent;

public class Controlled implements IControlled
{
    private IEvent left;
    private IEvent right;
    private IEvent jump;
    private IEvent crouch;
    private IEvent attack;

    public Controlled()
    {
        left = new Event();
        right = new Event();
        jump = new Event();
        crouch = new Event();
        attack = new Event();
    }

    @Override
    public IEvent getLeft()
    {
        return left;
    }

    @Override
    public IEvent getRight()
    {
        return right;
    }

    @Override
    public IEvent getJump()
    {
        return jump;
    }

    @Override
    public IEvent getCrouch()
    {
        return crouch;
    }

    @Override
    public IEvent getAttack()
    {
        return crouch;
    }

    @Override
    public void update(float delta)
    {

    }
}
