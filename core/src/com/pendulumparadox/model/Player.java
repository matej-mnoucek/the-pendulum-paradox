package com.pendulumparadox.model;

import com.badlogic.gdx.math.Vector2;
import com.pendulumparadox.controll.IControlled;
import com.pendulumparadox.interfaces.IUpdatable;

public class Player extends Entity implements IPlayer, IUpdatable
{
    private IControlled controlled;
    private Vector2 position;

    public Player(IControlled controlled)
    {
        this.controlled = controlled;
    }

    @Override
    public void update(float delta)
    {

    }

    public Vector2 getPosition()
    {
        return position;
    }
}
