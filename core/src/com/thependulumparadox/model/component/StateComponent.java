package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.thependulumparadox.state.TaggedState;

import java.util.ArrayList;
import java.util.List;

public class StateComponent<T> implements Component
{
    public List<TaggedState<T>> states = new ArrayList<>();
    public TaggedState<T> initialState = null;

    public TaggedState<T> currentState = null;
    public TaggedState<T> requestedState = null;
    public boolean transitionRequested = false;
}
