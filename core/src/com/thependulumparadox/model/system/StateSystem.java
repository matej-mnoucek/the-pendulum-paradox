package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.state.IState;
import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.state.StateMachine;
import com.thependulumparadox.state.TaggedState;

import java.util.ArrayList;
import java.util.List;

public class StateSystem extends EntitySystem
{
    private ImmutableArray<Entity> stateEntities;
    private List<IStateMachine> stateMachines = new ArrayList<>();

    private ComponentMapper<StateComponent> stateComponentMapper
            = ComponentMapper.getFor(StateComponent.class);

    public void addedToEngine(Engine engine)
    {
        // Get all entities
        stateEntities = engine.getEntitiesFor(Family.all(StateComponent.class).get());

        // Create corresponding state machines
        for (int i = 0; i < stateEntities.size(); i++)
        {
            Entity entity = stateEntities.get(i);
            StateComponent stateComponent = stateComponentMapper.get(entity);

            IStateMachine machine = new StateMachine(true);
            // Add all states
            for (int j = 0; j < stateComponent.states.size(); j++)
            {
                machine.addState((IState) stateComponent.states.get(i));
            }
            machine.setInitialState(stateComponent.initialState);
            stateComponent.currentState = stateComponent.initialState;

            // Add state machine
            stateMachines.add(machine);
        }
    }

    public void update(float deltaTime)
    {
        // Process requested state transitions
        for (int i = 0; i < stateEntities.size(); i++)
        {
            Entity entity = stateEntities.get(i);
            IStateMachine machine = stateMachines.get(i);
            StateComponent stateComponent = stateComponentMapper.get(entity);

            //System.out.println("Player State: " + stateComponent.currentState.tag);

            if (stateComponent.transitionRequested)
            {
                if (stateComponent.states.contains(stateComponent.requestedState))
                {
                    machine.nextState(stateComponent.requestedState);
                    stateComponent.transitionRequested = false;

                    // Set current state
                    stateComponent.currentState = (TaggedState) machine.getCurrentState();
                }
                else
                {
                    stateComponent.transitionRequested = false;
                }
            }
        }
    }
}
