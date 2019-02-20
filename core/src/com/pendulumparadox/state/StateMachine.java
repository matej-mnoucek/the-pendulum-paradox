package com.pendulumparadox.state;

import java.util.ArrayList;
import java.util.List;

public class StateMachine implements IStateMachine
{
    private List<IState> states = new ArrayList<>();
    private List<ITransition> transitions = new ArrayList<>();
    private IState currentState = null;

    @Override
    public void addTransition(ITransition transition)
    {
        if (!transitions.contains(transition))
        {
            transitions.add(transition);
        }
    }

    @Override
    public void removeTransition(ITransition transition)
    {
        transitions.remove(transition);
    }

    @Override
    public void addState(IState state)
    {
        if (!states.contains(state))
        {
            states.add(state);
        }
    }

    @Override
    public void removeState(IState state)
    {
        if (currentState.equals(state))
        {
            currentState = null;
        }

        states.remove(state);
    }

    @Override
    public void setInitialState(IState state) throws EStateNotAvailable
    {
        if (states.contains(state))
        {
            currentState = state;
            currentState.execute();
        }
        else
        {
            throw new EStateNotAvailable();
        }
    }

    @Override
    public void nextState(IState nextState) throws EInvalidTransition, EStateNotAvailable
    {
        if (currentState.equals(null))
        {
            throw new EStateNotAvailable();
        }

        for (ITransition transition : transitions)
        {
            if (transition.getFrom().equals(currentState) && transition.getTo().equals(nextState))
            {
                currentState = nextState;
                currentState.execute();
                return;
            }
        }

        throw new EInvalidTransition();
    }

    @Override
    public IState getCurrentState()
    {
        return currentState;
    }
}
