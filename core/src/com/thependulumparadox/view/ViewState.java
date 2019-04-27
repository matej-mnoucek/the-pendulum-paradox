package com.thependulumparadox.view;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.thependulumparadox.state.IState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.BaseScreen;

/**
 * This class implements a View state (for MVP and from state pattern).
 * Each view state represents combination of UI and loaded scene
 * Each view state communicates back with Presenter via events
 */
public class ViewState implements IState, Screen
{
    private BaseScreen screen;
    private Scene scene;

    public ViewState(Scene scene, BaseScreen screen)
    {
        this.scene = scene;
        this.screen = screen;
    }

    public BaseScreen getScreen() {
        return screen;
    }
    public Scene getScene() {
        return scene;
    }

    @Override
    public void execute()
    {
        // No actions related to this state
    }

    @Override
    public void show()
    {
        scene.show();
        screen.show();
    }

    @Override
    public void render(float delta)
    {
        scene.render(delta);
        screen.render(delta);
    }

    @Override
    public void resize(int width, int height)
    {
        scene.resize(width, height);
        screen.resize(width, height);
    }

    @Override
    public void pause()
    {
        scene.pause();
        screen.pause();
    }

    @Override
    public void resume()
    {
        scene.resume();
        screen.resume();
    }

    @Override
    public void hide()
    {
        scene.hide();
        screen.hide();
    }

    @Override
    public void dispose()
    {
        scene.dispose();
        screen.dispose();
    }
}
