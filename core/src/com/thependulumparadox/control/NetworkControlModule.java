package com.thependulumparadox.control;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.thependulumparadox.model.component.DynamicBodyComponent;

/**
 * Implementation of a control module that receives input over network
 * @see ControlModule
 */
public class NetworkControlModule extends EventControlModule implements IMoveCommands
{
    private Body body;

    public NetworkControlModule(Body body)
    {
        this.body = body;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
    }

    @Override
    public void moveLeft() { leftStart(); }
    @Override
    public void stopMoveLeft(Vector2 position)
    {
        leftEnd();
        body.setTransform(position,0);
    }

    @Override
    public void moveRight() { rightStart(); }
    @Override
    public void stopMoveRight(Vector2 position)
    {
        rightEnd();
        body.setTransform(position,0);
    }

    @Override
    public void jump() { jumpStart(); }
    @Override
    public void stopJump() { jumpEnd(); }
    @Override
    public void startShooting(Vector2 position )
    {
        body.setTransform(position,0);
        attackStart();
    }

    @Override
    public void stopShooting() { attackEnd(); }
}
