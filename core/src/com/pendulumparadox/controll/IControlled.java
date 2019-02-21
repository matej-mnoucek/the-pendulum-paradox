package com.pendulumparadox.controll;

import com.pendulumparadox.interfaces.IUpdatable;
import com.pendulumparadox.observer.IEvent;

public interface IControlled extends IUpdatable
{
    IEvent getLeft();
    IEvent getRight();
    IEvent getJump();
    IEvent getCrouch();
    IEvent getAttack();
}
