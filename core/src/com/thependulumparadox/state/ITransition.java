package com.thependulumparadox.state;

/**
 * Interface which defines legal transitions from one state to another
 * and therefore defines the state diagram
 */
public interface ITransition
{
    IState getFrom();
    IState getTo();
}
