package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.model.component.AbstractComponentFactory;
import com.pendulumparadox.model.component.ComponentFactory;
import com.pendulumparadox.model.entity.EntityBuilder;
import com.pendulumparadox.model.entity.IEntityBuilder;
import com.pendulumparadox.state.IStateMachine;
import com.pendulumparadox.state.StateMachine;
import com.pendulumparadox.view.screen.HighScoreScreen;
import com.pendulumparadox.view.screen.InGameScreen;
import com.pendulumparadox.view.screen.MenuScreen;

/**
 * The main control class of the whole game.
 * It is closer to MVP than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{

    public static int V_WIDTH = 600;
    public static int V_HEIGHT = 600;
    // Component based system root
    Engine ecs = new Engine();

    // Component factory
    AbstractComponentFactory componentFactory = new ComponentFactory();

    // Entity builder
    IEntityBuilder entityBuilder = new EntityBuilder();


    // Physics world
    World physics = new World(new Vector2(0, -10), true);

    // MVP view state machine
    IStateMachine viewMachine = new StateMachine();

    // Assets
    AssetManager assetManager = new AssetManager();

    // Camera
    OrthographicCamera mainCamera = new OrthographicCamera();

    MenuScreen screen = new MenuScreen();
    //InGameScreen screen = new InGameScreen();
    //HighScoreScreen screen = new HighScoreScreen();

    @Override
    public void create()
    {
        screen.create();
    }

    public void update(float delta)
    {
        // Update ECS
        ecs.update(delta);

        // Update physics
        physics.step(1/60f, 6, 2);
    }

    @Override
    public void render()
    {
        // Render
        super.render();
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        screen.render();

        // Update
        update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }
}
