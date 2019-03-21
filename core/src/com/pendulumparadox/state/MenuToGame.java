package com.pendulumparadox.state;

public class MenuToGame implements ITransition{

    private IState FromState;
    private IState ToState;

    @Override
    public IState getFrom() {
        return FromState;
    }

    @Override
    public IState getTo() {
        return ToState;
    }
}
