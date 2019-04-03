package com.thependulumparadox.view;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.thependulumparadox.state.IState;
import com.thependulumparadox.view.scene.Scene;
import com.thependulumparadox.view.screen.BaseScreen;

/**
 * This class implements a View state (for MVP and from State pattern).
 * Each view state represents combination of UI and loaded Scene
 * Each view state will communicate with Presenter via events
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

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        scene.render(delta);
        screen.render(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.scene.dispose();
        this.screen.dispose();
    }
}
