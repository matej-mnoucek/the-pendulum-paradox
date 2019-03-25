package com.pendulumparadox.state;

public class MenuToGameTansition<T extends IState,U extends IState> implements ITransition<T,U>{

    private final T from;
    private final U to;

    public MenuToGameTansition(T from, U to){
        this.from = from;
        this.to = to;
    }

    @Override
    public T getFrom() {
        return from;
    }

    @Override
    public U getTo() {
        return to;
    }
}