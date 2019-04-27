package com.thependulumparadox.state;

import com.thependulumparadox.view.ViewState;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of basic state machine
 * @see IStateMachine
 */
public class StateMachine implements IStateMachine
{
    private List<IState> states = new ArrayList<>();
    private List<ITransition> transitions = new ArrayList<>();
    private IState currentState = null;
    private boolean ignoreTransitions = false;

    public StateMachine()
    {
        this.ignoreTransitions = false;
    }

    public StateMachine(boolean ignoreTransitions)
    {
        this.ignoreTransitions = ignoreTransitions;
    }

    @Override
    public boolean addTransition(ITransition transition)
    {
        if (!transitions.contains(transition))
        {
            return transitions.add(transition);
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean removeTransition(ITransition transition)
    {
        return transitions.remove(transition);
    }

    @Override
    public boolean addState(IState state)
    {
        if (!states.contains(state))
        {
            states.add(state);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean removeState(IState state)
    {
        if (currentState.equals(state))
        {
            currentState = null;
        }

        return states.remove(state);
    }

    @Override
    public boolean setInitialState(IState state)
    {
        if (states.contains(state))
        {
            currentState = state;
            currentState.execute();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean nextState(IState nextState)
    {
        if (currentState == null)
        {
            return false;
        }

        if (ignoreTransitions)
        {
            currentState = nextState;
            currentState.execute();
            return true;
        }
        else
        {
            for (ITransition transition : transitions)
            {
                if (transition.getFrom().equals(currentState)
                        && transition.getTo().equals(nextState))
                {
                    currentState = nextState;
                    currentState.execute();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IState getCurrentState()
    {
        return currentState;
    }
}
