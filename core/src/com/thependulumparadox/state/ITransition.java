package com.thependulumparadox.state;

public interface ITransition
{
    IState getFrom();
    IState getTo();
}
