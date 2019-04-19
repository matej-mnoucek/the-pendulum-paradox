package com.thependulumparadox.model.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardControlModule extends ControlModule
{
    // Key mapping
    private final static int rightKey = Input.Keys.RIGHT;
    private final static int leftKey = Input.Keys.LEFT;
    private final static int attackKey = Input.Keys.SPACE;
    private final static int jumpKey = Input.Keys.UP;

    @Override
    public void update(float delta)
    {
        if (Gdx.input.isKeyPressed(rightKey))
        {
            right.invoke(null);
        }

        if (Gdx.input.isKeyPressed(leftKey))
        {
            left.invoke(null);
        }

        if (Gdx.input.isKeyJustPressed(attackKey))
        {
            attack.invoke(null);
        }

        if (Gdx.input.isKeyJustPressed(jumpKey))
        {
            jump.invoke(null);
        }
    }
}
