package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.Constants;
import com.pendulumparadox.model.component.AbstractComponentFactory;
import com.pendulumparadox.model.component.AnimatedSpriteComponent;
import com.pendulumparadox.model.component.CameraTargetComponent;
import com.pendulumparadox.model.component.ComponentFactory;
import com.pendulumparadox.model.component.ControlComponent;
import com.pendulumparadox.model.component.DynamicBodyComponent;
import com.pendulumparadox.model.component.TransformComponent;
import com.pendulumparadox.model.entity.EntityBuilder;
import com.pendulumparadox.model.entity.IEntityBuilder;
import com.pendulumparadox.model.system.CameraFollowSystem;
import com.pendulumparadox.model.system.InputSystem;
import com.pendulumparadox.model.system.RenderingSystem;
import com.pendulumparadox.model.system.PhysicsSystem;
import com.pendulumparadox.state.EInvalidTransition;
import com.pendulumparadox.state.EStateNotAvailable;
import com.pendulumparadox.state.IStateMachine;
import com.pendulumparadox.state.StateMachine;
import com.pendulumparadox.state.Transition;
import com.pendulumparadox.view.ViewState;
import com.pendulumparadox.view.scene.GameScene;
import com.pendulumparadox.view.screen.BaseScreen;
import com.pendulumparadox.view.screen.GameOverScreen;
import com.pendulumparadox.view.screen.HighScoreScreen;
import com.pendulumparadox.view.screen.InGameScreen;
import com.pendulumparadox.view.screen.MenuScreen;
import com.pendulumparadox.view.screen.SettingsScreen;
import com.pendulumparadox.view.screen.TutorialScreen;


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
    World world = new World(new Vector2(0, -10), true);

    // MVP view state machine
    IStateMachine viewMachine = new StateMachine();

    // Assets
    AssetManager assetManager = new AssetManager();

    // Camera
    OrthographicCamera mainCamera = new OrthographicCamera();

    // Predefined view states (composed of one screen and one scene)
    private ViewState viewStateInGame;
    private ViewState viewStateGameOver;
    private ViewState viewStateMenu;
    private ViewState viewStateHighScore;
    private ViewState viewStateSettings;
    private ViewState viewStateTutorial;
    private BaseScreen inGameScreen;
    private BaseScreen highScoreScreen;
    private BaseScreen gameOverScreen;
    private BaseScreen menuScreen;
    private BaseScreen settingsScreen;
    private BaseScreen tutorialScreen;

    private Boolean firstPlayThrough = true;

    private Music menuMusic;
    private boolean soundOn;

    // DEBUG
    FPSLogger fpsLogger;
    Box2DDebugRenderer debugRenderer;
    ShapeRenderer shapeRenderer;
    Entity player;
    TransformComponent transformComponent;

    @Override
    public void create() {
        // DEBUG
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setProjectionMatrix(mainCamera.combined);
        fpsLogger = new FPSLogger();

        // TEST PLAYER
        // ECS Entity
        player = new Entity();
        transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(3, 8);
        player.add(transformComponent);
        AnimatedSpriteComponent animated = new AnimatedSpriteComponent();
        animated.frameDuration = 0.1f;
        animated.height = 1.8f;
        animated.width = 1.8f;
        animated.atlasPath = "packed/idle.atlas";
        animated.region = "idle";
        player.add(animated);
        DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent();
        dynamicBodyComponent.center = transformComponent.position;
        dynamicBodyComponent.height = 1.5f;
        dynamicBodyComponent.width = 0.7f;
        player.add(dynamicBodyComponent);
        CameraTargetComponent cameraComponent = new CameraTargetComponent();
        player.add(cameraComponent);
        ControlComponent controlComponent = new ControlComponent();
        player.add(controlComponent);

        // ECS Systems
        // Camera follow
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);

        // Rendering
        RenderingSystem renderingSystem = new RenderingSystem(mainCamera);

        // Physics
        PhysicsSystem physics = new PhysicsSystem(world);

        // Control
        InputSystem input = new InputSystem();


        //populate assetmanager with assets
        assetManager.load("sounds/single_gunshot.mp3", Sound.class);
        assetManager.load("sounds/coin_collect.mp3", Sound.class);
        assetManager.load("sounds/jump.mp3", Sound.class);
        assetManager.load("sounds/die.mp3", Sound.class);
        assetManager.load("sounds/GameOver.mp3", Sound.class);
        assetManager.load("sounds/reload.mp3", Sound.class);
        //assetManager.load("sounds/enemy_dead.mp3", Sound.class);
        assetManager.finishLoading();

        /*set music for menu and start playing. Music not in assetManager because larger sound
        files needs to be streamed rather than be defined as Sound Class*/
        soundOn = true;
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"));
        menuMusic.setLooping(true);
        menuMusic.play();

        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen();
        highScoreScreen = new HighScoreScreen();
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();

        // Link game start
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) ->
        {
            if (firstPlayThrough) {
                ecs.addEntity(player);
                ecs.addSystem(cameraFollowSystem);
                ecs.addSystem(renderingSystem);
                ecs.addSystem(physics);
                ecs.addSystem(input);

                firstPlayThrough = false;
            } else {
                ecs.addEntity(player);
            }
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            if(soundOn) {
                menuMusic.stop();
                ((InGameScreen) inGameScreen).playGameMusic();
            }
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateInGame);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        // Create screen and scene for future view state assembly
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera);
        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera);

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
        Gdx.input.setInputProcessor(menuScreen.getStage());


        /*this event is already defined above
        // Registering event handler == a piece of code that runs everytime the event is invoked
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) -> {
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateInGame);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });
        */

        ((MenuScreen) menuScreen).getSettingsEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(settingsScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateSettings);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((MenuScreen) menuScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateHighScore);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((MenuScreen) menuScreen).getTutorialEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(tutorialScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateTutorial);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((InGameScreen) inGameScreen).getSoundEvent().addHandler((args) -> {
            if(soundOn){
                ((InGameScreen) inGameScreen).stopGameMusic();
                ((SettingsScreen) settingsScreen).setSoundOn(false);
                soundOn = false;
            } else {
                ((InGameScreen) inGameScreen).playGameMusic();
                ((SettingsScreen) settingsScreen).setSoundOn(true);
                soundOn = true;
            }
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
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(gameOverScreen.getStage());
            if(soundOn) {
                ((InGameScreen) inGameScreen).getGameMusic().stop();
                menuMusic.play();
            }
            //remove Player entity to stop it rendering whilst not ingame
            ecs.removeEntity(player);
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateGameOver);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }

        });

        ((GameOverScreen) gameOverScreen).getNewGameEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateInGame);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((GameOverScreen) gameOverScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateHighScore);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((GameOverScreen) gameOverScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });
        ((HighScoreScreen) highScoreScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((SettingsScreen) settingsScreen).getSoundEvent().addHandler((args) -> {
            if(soundOn) {
                menuMusic.pause();
                ((InGameScreen) inGameScreen).setSoundOn(false);
                soundOn = false;
            } else {
                menuMusic.play();
                ((InGameScreen) inGameScreen).setSoundOn(true);
                soundOn = true;
            }
        });

        ((SettingsScreen) settingsScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });

        ((TutorialScreen) tutorialScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateMenu);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
        });
    }

    private float accumulator = 0;
    private float timeStep = 1/60.0f;
    public void update(float delta)
    {
        // Update ECS
        ecs.update(delta);

        // Update physics
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }
    }

    @Override
    public void render()
    {
        // Render
        super.render();
        Gdx.gl.glClearColor(0.0f, 0.4f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Get delta time value for current frame
        float delta = Gdx.graphics.getDeltaTime();

        // Get current view state and render it
        ((Screen)viewMachine.getCurrentState()).render(delta);

        // DEBUG
        /*
        shapeRenderer.setProjectionMatrix(mainCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(transformComponent.position.x, transformComponent.position.y,100);
        shapeRenderer.end();
        */
        //debugRenderer.render(world, mainCamera.combined);
        fpsLogger.log();

        // Update method
        update(delta);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);

        // Virtual pixels
        mainCamera.setToOrtho(false,
                (Constants.VIRTUAL_HEIGHT * width / (float)height) / Constants.PPM,
                Constants.VIRTUAL_HEIGHT / Constants.PPM);
        mainCamera.update(true);
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

    public boolean isSoundOn() {
        return soundOn;
    }
}
