package com.thependulumparadox.state;

/**
 * Interface for defining state machine that handles states and transitions between them
 * (state pattern)
 */
public interface IStateMachine
{
    boolean addTransition(ITransition transition);
    boolean removeTransition(ITransition transition);

    boolean addState(IState state);
    boolean removeState(IState state);

    boolean setInitialState(IState state);
    boolean nextState(IState nextState);
    IState getCurrentState();
}
