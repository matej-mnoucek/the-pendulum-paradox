package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.pendulumparadox.state.StateMachine;
import com.pendulumparadox.state.Transition;
import com.pendulumparadox.view.ViewState;
import com.pendulumparadox.view.scene.GameScene;
import com.pendulumparadox.view.scene.MainMenuScene;
import com.pendulumparadox.view.screen.BaseScreen;
import com.pendulumparadox.view.screen.GameOverScreen;
import com.pendulumparadox.view.screen.HighScoreScreen;
import com.pendulumparadox.view.screen.InGameScreen;
import com.pendulumparadox.view.screen.MenuScreen;
import com.pendulumparadox.view.screen.SettingsScreen;
import com.pendulumparadox.view.screen.TutorialScreen;

import javax.swing.text.View;


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
    ViewState viewStateGameOver;
    ViewState viewStateMenu;
    ViewState viewStateHighScore;
    ViewState viewStateSettings;
    ViewState viewStateTutorial;
    BaseScreen inGameScreen;
    BaseScreen highScoreScreen;
    BaseScreen gameOverScreen;
    BaseScreen menuScreen;
    BaseScreen settingsScreen;
    BaseScreen tutorialScreen;

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
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("level1.tmx"), mainCamera);
        MainMenuScene menuScene = new MainMenuScene(new TmxMapLoader().load("level1.tmx"), mainCamera);
        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen();
        highScoreScreen = new HighScoreScreen();
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();
        viewStateInGame = new ViewState(levelOneScene, inGameScreen);
        viewStateGameOver = new ViewState(levelOneScene, gameOverScreen);
        viewStateMenu = new ViewState(menuScene, menuScreen);
        viewStateHighScore = new ViewState(menuScene, highScoreScreen);
        viewStateSettings = new ViewState(menuScene, settingsScreen);
        viewStateTutorial = new ViewState(menuScene, tutorialScreen);

        // Add state to the state machine
        viewMachine.addState(viewStateInGame);
        viewMachine.addState(viewStateGameOver);
        viewMachine.addState(viewStateMenu);
        viewMachine.addState(viewStateHighScore);
        viewMachine.addState(viewStateSettings);
        viewMachine.addState(viewStateTutorial);

        // Define transition
        Transition menuToHighScore = new Transition(viewStateMenu, viewStateHighScore);
        Transition menuToInGame = new Transition(viewStateMenu, viewStateInGame);
        Transition menuToSettings = new Transition(viewStateMenu, viewStateSettings);
        Transition menuToTutorial = new Transition(viewStateMenu, viewStateTutorial);
        Transition tutorialToMenu = new Transition(viewStateTutorial, viewStateMenu);
        Transition settingsToMenu = new Transition(viewStateSettings, viewStateMenu);
        Transition inGameToGameOver = new Transition(viewStateInGame, viewStateGameOver);
        Transition gameOverToHighScore = new Transition(viewStateGameOver, viewStateHighScore);
        Transition gameOverToMenu = new Transition(viewStateGameOver, viewStateMenu);
        Transition highScoreToMenu = new Transition(viewStateHighScore, viewStateMenu);
        Transition inGameToHighScore = new Transition(viewStateInGame, viewStateHighScore);

        // Add transition to the state machine
        viewMachine.addTransition(menuToHighScore);
        viewMachine.addTransition(menuToInGame);
        viewMachine.addTransition(menuToSettings);
        viewMachine.addTransition(settingsToMenu);
        viewMachine.addTransition(inGameToGameOver);
        viewMachine.addTransition(gameOverToHighScore);
        viewMachine.addTransition(gameOverToMenu);
        viewMachine.addTransition(highScoreToMenu);
        viewMachine.addTransition(inGameToHighScore);
        viewMachine.addTransition(menuToTutorial);
        viewMachine.addTransition(tutorialToMenu);

        // Setting the entry point == initial state
        try {
            viewMachine.setInitialState(viewStateMenu);
        } catch (EStateNotAvailable eStateNotAvailable) {
            eStateNotAvailable.printStackTrace();
        }
        // Set inputProcessor to entry point's BaseScreen's Stage
        Gdx.input.setInputProcessor(this.menuScreen.getStage());



        // Registering event handler == a piece of code that runs everytime the event is invoked
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateInGame);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.inGameScreen.getStage());
        });
        ((MenuScreen) menuScreen).getSettingsEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateSettings);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.settingsScreen.getStage());
        });
        ((MenuScreen) menuScreen).getHighScoreEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateHighScore);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.highScoreScreen.getStage());
        });
        ((MenuScreen) menuScreen).getTutorialEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateTutorial);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.tutorialScreen.getStage());
        });
        ((InGameScreen) inGameScreen).getLeftEvent().addHandler((args) -> {
            assert true;
        });
        ((InGameScreen) inGameScreen).getRightEvent().addHandler((args) -> {
            assert true;
        });
        ((InGameScreen) inGameScreen).getShootEvent().addHandler((args) -> {
            assert true;
        });
        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateGameOver);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.gameOverScreen.getStage());
        });
        ((GameOverScreen) gameOverScreen).getNewGameEvent().addHandler((args) -> {
            // call on state machine to change state
            try{
                viewMachine.nextState(viewStateInGame);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            }catch (EStateNotAvailable eStateNotAvailable){
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.inGameScreen.getStage());
        });
        ((GameOverScreen) gameOverScreen).getHighScoreEvent().addHandler((args) -> {
            // call on state machine to change state
            try{
                viewMachine.nextState(viewStateHighScore);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            }catch (EStateNotAvailable eStateNotAvailable){
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.highScoreScreen.getStage());
        });
        ((GameOverScreen) gameOverScreen).getMenuEvent().addHandler((args) -> {
            // call on state machine to change state
            try{
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            }catch (EStateNotAvailable eStateNotAvailable){
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.menuScreen.getStage());
        });
        ((HighScoreScreen) highScoreScreen).getMenuEvent().addHandler((args) -> {
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.menuScreen.getStage());
        });
        ((SettingsScreen) settingsScreen).getSoundEvent().addHandler((args) -> {
            assert true;
        });
        ((SettingsScreen) settingsScreen).getMenuEvent().addHandler((args) -> {
            // call on state machine to change state
            try{
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            }catch (EStateNotAvailable eStateNotAvailable){
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(this.menuScreen.getStage());
        });
        ((TutorialScreen) tutorialScreen).getMenuEvent().addHandler((args) -> {
            // call on state machine to change state
            try{
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            }catch (EStateNotAvailable eStateNotAvailable){
                eStateNotAvailable.printStackTrace();
            }
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
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
