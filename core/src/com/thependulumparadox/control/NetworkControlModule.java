package com.thependulumparadox.control;

public class NetworkControlModule extends EventControlModule implements MoveCommands
{

    @Override
    public void update(float delta)
    {

        super.update(delta);
    }

    @Override
    public MoveCommands getInputSystem() {
        return this;
    }

    @Override
    public void moveLeft() {leftStart();}
    @Override
    public void stopMoveLeft() {leftEnd();}
    @Override
    public void moveRight() {rightStart();}
    @Override
    public void stopMoveRight() {rightEnd();}
    @Override
    public void jump() {jumpStart();}
    @Override
    public void stopJump() {jumpEnd();}
    @Override
    public void startShooting() {attackStart();}
    @Override
    public void stopShooting() {attackEnd();}
}
