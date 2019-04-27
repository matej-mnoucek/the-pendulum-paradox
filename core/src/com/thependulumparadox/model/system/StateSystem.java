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

/**
 * System which handles switching between different entity states
 * @see StateComponent
 */
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
    }

    private void initializeStateMachines()
    {
        // Get rid of old state machines
        stateMachines.clear();

        // Create corresponding state machines
        for (int i = 0; i < stateEntities.size(); i++)
        {
            Entity entity = stateEntities.get(i);
            StateComponent stateComponent = stateComponentMapper.get(entity);

            IStateMachine machine = new StateMachine(true);
            // Add all states
            for (int j = 0; j < stateComponent.states.size(); j++)
            {
                machine.addState((IState) stateComponent.states.get(j));
            }
            machine.setInitialState(stateComponent.initialState);
            stateComponent.currentState = stateComponent.initialState;

            // Add state machine
            stateMachines.add(machine);
        }
    }

    public void update(float deltaTime)
    {
        // If the amount of entities changed reinit machines
        if (stateMachines.size() != stateEntities.size())
        {
            initializeStateMachines();
        }

        // Process requested state transitions
        for (int i = 0; i < stateEntities.size(); i++)
        {
            Entity entity = stateEntities.get(i);
            IStateMachine machine = stateMachines.get(i);
            StateComponent stateComponent = stateComponentMapper.get(entity);

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
