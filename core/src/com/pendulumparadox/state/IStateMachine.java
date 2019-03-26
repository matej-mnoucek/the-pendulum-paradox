package com.pendulumparadox.state;

import java.util.List;

public interface IStateMachine
{
    void addTransition(ITransition transition);
    void removeTransition(ITransition transition);

    void addState(IState state);
    void removeState(IState state);

    void setInitialState(IState state) throws EStateNotAvailable;
    void nextState(IState nextState) throws EInvalidTransition, EStateNotAvailable;
    IState getCurrentState();

    List<ITransition> getTransitions();
}
