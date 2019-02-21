package com.pendulumparadox.model;

import com.pendulumparadox.controll.IControlled;
import com.pendulumparadox.interfaces.IUpdatable;

public abstract class Enemy extends Entity implements IEnemy, IUpdatable
{
    private IControlled controlled;

    public Enemy(IControlled controlled)
    {
        this.controlled = controlled;
    }

    @Override
    public void update(float delta)
    {

    }
}
