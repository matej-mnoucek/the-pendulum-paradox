package com.thependulumparadox.state;

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
