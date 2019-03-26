package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.pendulumparadox.state.EntityTransition;
import com.pendulumparadox.state.IState;
import com.pendulumparadox.state.StateMachine;

import java.util.ArrayList;
import java.util.List;

public class StateComponent implements Component
{
    class Idle implements IState
    {
        @Override
        public void execute(){

        }
    }

    class Walking implements IState
    {
        @Override
        public void execute(){

        }
    }
    class Shooting implements IState
    {
        @Override
        public void execute(){

        }
    }
    class WalkShooting implements IState
    {
        @Override
        public void execute(){

        }
    }

    class Ascending implements IState
    {
        @Override
        public void execute(){

        }
    }

    class Descending implements IState
    {
        @Override
        public void execute(){

        }
    }

    class AirShooting implements IState
    {
        @Override
        public void execute(){

        }
    }

    class Dying implements IState
    {
        @Override
        public void execute(){

        }
    }

    class Dead implements IState
    {
        @Override
        public void execute(){

        }
    }
    private Idle idle;
    private Walking walking;
    private Shooting shooting;
    private WalkShooting walkShooting;
    private Ascending ascending;
    private Descending descending;
    private AirShooting airShooting;
    private Dying dying;
    private Dead dead;

    private StateMachine stateMachine = new StateMachine();
    private List<EntityTransition> transitions;
    public StateComponent(){
        transitions = new ArrayList<>();
        idle = new Idle();
        walking = new Walking();
        shooting = new Shooting();
        walkShooting = new WalkShooting();
        ascending = new Ascending();
        descending = new Descending();
        airShooting = new AirShooting();
        dying = new Dying();
        dead = new Dead();
        stateMachine.addState(idle);
        stateMachine.addState(walking);
        stateMachine.addState(shooting);
        stateMachine.addState(walkShooting);
        stateMachine.addState(ascending);
        stateMachine.addState(descending);
        stateMachine.addState(airShooting);
        stateMachine.addState(dying);
        stateMachine.addState(dead);

        Vector2 velocity = new Vector2();

        stateMachine.addTransition(new EntityTransition(idle, walking, () -> {
            return Math.abs(velocity.x) > 0;
        }));
        stateMachine.addTransition(new EntityTransition(idle, ascending, () -> {
            return velocity.y > 0;
        }));
        stateMachine.addTransition(new EntityTransition(idle, descending, () -> {
            return velocity.y < 0;
        })); //for when the platform the character stands on suddenly disappears
        stateMachine.addTransition(new EntityTransition(idle, dying, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(idle, shooting, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(walking, idle, () -> {
            return Math.abs(velocity.x) == 0;
        }));
        stateMachine.addTransition(new EntityTransition(walking, walkShooting, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(walking, ascending, () -> {
            return velocity.y > 0;
        }));
        stateMachine.addTransition(new EntityTransition(walking, descending, () -> {
            return velocity.y < 0;
        }));
        stateMachine.addTransition(new EntityTransition(walking, dying, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(shooting, walkShooting, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(shooting, ascending, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(shooting, descending, () -> {
            return false;
        })); //for when the platform the character stands on suddenly disappears
        stateMachine.addTransition(new EntityTransition(shooting, dying, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(walkShooting, shooting, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(walkShooting, airShooting, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(walkShooting, dying, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(ascending, idle, () -> {
            return Math.abs(velocity.y) == 0;
        })); //shouldn't really happen, but I guess it probably can due to game physics
        stateMachine.addTransition(new EntityTransition(ascending, descending, () -> {
            return Math.abs(velocity.y) < 0;
        }));
        stateMachine.addTransition(new EntityTransition(ascending, airShooting,() -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(ascending, dying, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(descending, idle, () -> {
            return Math.abs(velocity.y) == 0;
        }));
        stateMachine.addTransition(new EntityTransition(descending, airShooting, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(descending, dying, () -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(airShooting, idle, () -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(airShooting, ascending,() -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(airShooting, descending,() -> {
            return false;
        }));
        stateMachine.addTransition(new EntityTransition(airShooting, dying,() -> {
            return false;
        }));

        stateMachine.addTransition(new EntityTransition(dying, dead, () -> {
            return false;
        }));

    }
    public StateMachine getStateMachine(){
        return stateMachine;
    }
}
