package com.thependulumparadox.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardControlModule extends ControlModule
{
    // Key mapping
    private final static int rightKey = Input.Keys.RIGHT;
    private final static int leftKey = Input.Keys.LEFT;
    private final static int attackKey = Input.Keys.SPACE;
    private final static int jumpKey = Input.Keys.UP;

    private boolean rightKeyPressed = false;
    private boolean leftKeyPressed = false;
    private boolean attackKeyPressed = false;
    private boolean jumpKeyPressed = false;

    @Override
    public void update(float delta)
    {
        // Right key
        if (Gdx.input.isKeyJustPressed(rightKey))
        {
            rightStart.invoke(null);
            rightKeyPressed = true;
        }

        if (Gdx.input.isKeyPressed(rightKey))
        {
            right.invoke(null);
        }

        if (!Gdx.input.isKeyPressed(rightKey) && rightKeyPressed)
        {
            rightEnd.invoke(null);
            rightKeyPressed = false;
        }

        // Left key
        if (Gdx.input.isKeyJustPressed(leftKey))
        {
            leftStart.invoke(null);
            leftKeyPressed = true;
        }

        if (Gdx.input.isKeyPressed(leftKey))
        {
            left.invoke(null);
        }

        if (!Gdx.input.isKeyPressed(leftKey) && leftKeyPressed)
        {
            leftEnd.invoke(null);
            leftKeyPressed = false;
        }

        // Attack key
        if (Gdx.input.isKeyJustPressed(attackKey))
        {
            attackStart.invoke(null);
            attackKeyPressed = true;
        }

        if (Gdx.input.isKeyPressed(attackKey))
        {
            attack.invoke(null);
        }

        if (!Gdx.input.isKeyPressed(attackKey) && attackKeyPressed)
        {
            attackEnd.invoke(null);
            attackKeyPressed = false;
        }

        // Jump key
        if (Gdx.input.isKeyJustPressed(jumpKey))
        {
            jumpStart.invoke(null);
            jumpKeyPressed = true;
        }

        if (Gdx.input.isKeyPressed(jumpKey))
        {
            jump.invoke(null);
        }

        if (!Gdx.input.isKeyPressed(jumpKey) && jumpKeyPressed)
        {
            jumpEnd.invoke(null);
            jumpKeyPressed = false;
        }
    }
}
