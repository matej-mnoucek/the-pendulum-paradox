package com.pendulumparadox.patterns.state;

public interface ITransition<T extends IState, U extends IState>
{
    T getFrom();
    U getTo();
}
