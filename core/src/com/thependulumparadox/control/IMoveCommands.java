package com.thependulumparadox.control;

import com.badlogic.gdx.math.Vector2;

public interface IMoveCommands
{
    public void moveLeft();

    public void stopMoveLeft(Vector2 position);

    public void moveRight();

    public void stopMoveRight(Vector2 position);

    public void jump();

    public void stopJump();

    public void startShooting(Vector2 position);

    public void stopShooting();
}
