package com.pendulumparadox.view.screen;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LobbyScreen extends BaseScreen
{

    @Override
    public void show() {
        throw new NotImplementedException();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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

    }
}
