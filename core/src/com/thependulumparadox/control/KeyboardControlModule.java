package com.thependulumparadox.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Implementation of a control module that senses input from keyboard
 * @see ControlModule
 */
public class KeyboardControlModule extends EventControlModule
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
        super.update(delta);

        // Right key
        if (Gdx.input.isKeyJustPressed(rightKey))
        {
            rightStart();
            rightKeyPressed = true;
        }

        if (!Gdx.input.isKeyPressed(rightKey) && rightKeyPressed)
        {
            rightEnd();
            rightKeyPressed = false;
        }

        // Left key
        if (Gdx.input.isKeyJustPressed(leftKey))
        {
            leftStart();
            leftKeyPressed = true;
        }

        if (!Gdx.input.isKeyPressed(leftKey) && leftKeyPressed)
        {
            leftEnd();
            leftKeyPressed = false;
        }

        // Attack key
        if (Gdx.input.isKeyJustPressed(attackKey))
        {
            attackStart();
            attackKeyPressed = true;
        }

        if (!Gdx.input.isKeyPressed(attackKey) && attackKeyPressed)
        {
            attackEnd();
            attackKeyPressed = false;
        }

        // Jump key
        if (Gdx.input.isKeyJustPressed(jumpKey))
        {
            jumpStart();
            jumpKeyPressed = true;
        }

        if (!Gdx.input.isKeyPressed(jumpKey) && jumpKeyPressed)
        {
            jumpEnd();
            jumpKeyPressed = false;
        }
    }
}
