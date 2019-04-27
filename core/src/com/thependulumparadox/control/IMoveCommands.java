package com.thependulumparadox.control;

public interface IMoveCommands
{
    public void moveLeft();

    public void stopMoveLeft();

    public void moveRight();

    public void stopMoveRight();

    public void jump();

    public void stopJump();

    public void startShooting();

    public void stopShooting();
}
