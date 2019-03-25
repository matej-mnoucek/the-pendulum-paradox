package com.pendulumparadox.observer;

import com.pendulumparadox.state.InGameState;
import com.pendulumparadox.state.StateMachine;

public class NewGameHandler implements IEventHandler{

    private StateMachine stateMachine;

    public NewGameHandler(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handle(Object args) {
        stateMachine.nextState(new InGameState());
    }
}
