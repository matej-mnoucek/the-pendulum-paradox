package com.thependulumparadox.model;

public interface MoveCommands {

    public MoveCommands getInputSystem();

    public void moveLeft();

    public void stopMoveLeft();

    public void moveRight();

    public void stopMoveRight();

    public void jump();

    public void startShooting();

    public void stopShooting();

}
