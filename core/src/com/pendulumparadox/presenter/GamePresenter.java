package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.pendulumparadox.model.system.PhysicsSystem;
import com.pendulumparadox.state.EInvalidTransition;
import com.pendulumparadox.state.EStateNotAvailable;
import com.pendulumparadox.state.IStateMachine;
import com.pendulumparadox.state.ITransition;
import com.pendulumparadox.state.StateMachine;
import com.pendulumparadox.state.Transition;
import com.pendulumparadox.view.ViewState;
import com.pendulumparadox.view.scene.GameScene;
import com.pendulumparadox.view.screen.BaseScreen;
import com.pendulumparadox.view.screen.GameOverScreen;

import com.pendulumparadox.view.screen.HighScoreScreen;
import com.pendulumparadox.view.screen.InGameScreen;


/**
 * The main control class of the whole game.
 * It is closer to MVP than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{

    public static int V_WIDTH = 960;
    public static int V_HEIGHT = 540;

    //font
    public BitmapFont font24;

    // Component based system root
    Engine ecs = new Engine();

    // Component factory
    AbstractComponentFactory componentFactory = new ComponentFactory();

    // Entity builder
    IEntityBuilder entityBuilder = new EntityBuilder();


    // Physics world
    World world = new World(new Vector2(0, -10), true);

    // MVP view state machine
    IStateMachine viewMachine = new StateMachine();

    // Assets
    AssetManager assetManager = new AssetManager();

    // Camera
    OrthographicCamera mainCamera = new OrthographicCamera();

    // Predefined view states (composed of one screen and one scene)
    ViewState viewStateInGame;
    ViewState viewStateHighScore;


    @Override
    public void create()
    {
        // Physics
        PhysicsSystem physics = new PhysicsSystem(world);
        ecs.addSystem(physics);

        GraphicsSystem graphicsSystem = new GraphicsSystem();
        mainCamera.position.set(new Vector3(400,600,0));
        mainCamera.viewportWidth = 960;
        mainCamera.viewportHeight = 540;
        mainCamera.update();
        ecs.addSystem(graphicsSystem);


        // Create screen and scene for future view state assembly
        GameScene levelScene = new GameScene(new TmxMapLoader().load("level1.tmx"), mainCamera);
        BaseScreen inGameScreen = new InGameScreen();
        BaseScreen highScoreScreen = new HighScoreScreen();
        viewStateInGame = new ViewState(levelScene, inGameScreen);
        viewStateHighScore = new ViewState(levelScene, highScoreScreen);

        // Add state to the state machine
        viewMachine.addState(viewStateInGame);
        viewMachine.addState(viewStateHighScore);

        // Define transition
        Transition inGameToHighScore = new Transition(viewStateInGame, viewStateHighScore);

        // Add transition to the state machine
        viewMachine.addTransition(inGameToHighScore);

        // Setting the entry point == initial state
        try {
            viewMachine.setInitialState(viewStateInGame);
        } catch (EStateNotAvailable eStateNotAvailable) {
            eStateNotAvailable.printStackTrace();
        }

        // Registering event handler == a piece of code that runs everytime the event is invoked
        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {
            // Test of state transition
            try {
                viewMachine.nextState(viewStateHighScore);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });
    }


    public void update(float delta)
    {
        // Update ECS
        ecs.update(delta);

        // Update physics
        world.step(1/60f, 6, 2);
    }

    @Override
    public void render()
    {
        // Render
        super.render();
        Gdx.gl.glClearColor(0.0f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Get delta time value for current frame
        float delta = Gdx.graphics.getDeltaTime();

        // Get current view state and render it
        ((Screen)viewMachine.getCurrentState()).render(delta);

        // Update method
        update(delta);
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
