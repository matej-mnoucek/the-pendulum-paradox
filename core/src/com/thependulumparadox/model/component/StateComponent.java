package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.thependulumparadox.state.TaggedState;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that allows entity to have its internal state
 * (Used a lot for determining which animation to play based on current state)
 */
public class StateComponent implements Component
{
    public final List<TaggedState> states;
    public TaggedState initialState = null;

    public TaggedState currentState = null;
    public TaggedState requestedState = null;
    public boolean transitionRequested = false;

    public StateComponent()
    {
        states = new ArrayList<>();
    }

    public StateComponent add(TaggedState state)
    {
        states.add(state);
        return this;
    }

    public String currentStateName()
    {
        return currentState.tag;
    }

    public boolean initial(String stateTag)
    {
        for (int i = 0; i < states.size(); i++)
        {
            TaggedState state = states.get(i);

            if (state.tag.equals(stateTag))
            {
                initialState = state;
                currentState = state;
                return true;
            }
        }

        return false;
    }

    public boolean transition(String stateTag)
    {
        for (int i = 0; i < states.size(); i++)
        {
            TaggedState state = states.get(i);

            if (state.tag.equals(stateTag))
            {
                requestedState = state;
                transitionRequested = true;
                return true;
            }
        }

        return false;
    }
}
