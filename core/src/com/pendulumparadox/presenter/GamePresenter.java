package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.model.component.AbstractComponentFactory;
import com.pendulumparadox.model.component.ComponentFactory;
import com.pendulumparadox.model.entity.EntityBuilder;
import com.pendulumparadox.model.entity.IEntityBuilder;
import com.pendulumparadox.model.system.GraphicsSystem;
import com.pendulumparadox.state.IStateMachine;
import com.pendulumparadox.state.StateMachine;
import com.pendulumparadox.view.scene.GameScene;

/**
 * The main control class of the whole game.
 * It is closer to MVP than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{
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

    //Current Scene
    GameScene currentScene;
    @Override
    public void create()
    {
        GraphicsSystem graphicsSystem = new GraphicsSystem();
        mainCamera.position.set(new Vector3(400,600,0));
        mainCamera.viewportWidth = 960;
        mainCamera.viewportHeight = 540;
        mainCamera.update();
        ecs.addSystem(graphicsSystem);
        currentScene = new GameScene(new TmxMapLoader().load("level1.tmx"), mainCamera);
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
        Gdx.gl.glClearColor(0.0f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScene.render(Gdx.graphics.getDeltaTime());
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
