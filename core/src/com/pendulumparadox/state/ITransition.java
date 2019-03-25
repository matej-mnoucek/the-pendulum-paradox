package com.pendulumparadox.state;

public interface ITransition
{
    IState getFrom();
    IState getTo();
}
