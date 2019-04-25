package com.thependulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.thependulumparadox.control.EventControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.model.component.CleanupComponent;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.system.AnimationControlSystem;
import com.thependulumparadox.model.system.LevelManagementSystem;
import com.thependulumparadox.model.system.InteractionSystem;
import com.thependulumparadox.model.system.FPSDebugSystem;
import com.thependulumparadox.model.system.PhysicsDebugSystem;

import com.thependulumparadox.model.system.SoundSystem;

import com.thependulumparadox.model.system.StateSystem;
import com.thependulumparadox.model.system.VisualSystem;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.entity.AbstractEntityFactory;
import com.thependulumparadox.model.entity.EntityFactory;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.system.CameraFollowSystem;
import com.thependulumparadox.model.system.ControlSystem;
import com.thependulumparadox.model.system.RenderingSystem;
import com.thependulumparadox.model.system.PhysicsSystem;
import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.state.StateMachine;
import com.thependulumparadox.state.Transition;
import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.scene.GameScene;
import com.thependulumparadox.view.screen.BaseScreen;
import com.thependulumparadox.view.screen.GameOverScreen;
import com.thependulumparadox.view.screen.HighScoreScreen;
import com.thependulumparadox.view.screen.InGameScreen;
import com.thependulumparadox.view.screen.MenuScreen;
import com.thependulumparadox.view.screen.SettingsScreen;
import com.thependulumparadox.view.screen.TutorialScreen;

//TODO: implement "new game" pressed from GameOverScreen.

/**
 * The main control class of the whole game.
 * It is closer to MVP pattern than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{
    private static final Color CLEAR_COLOR = new Color(0.873f,0.873f,1f,1f);

    // Component based system root
    Engine ecs = new Engine();

    // Physics world
    World world = new World(new Vector2(0, -10), true);

    // Entity factory
    AbstractEntityFactory entityFactory = new EntityFactory(world);

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

    // Players
    Entity firstPlayer;
    Entity secondPlayer;

    // Multi player
    private boolean multiplayerAvailable = false;
    private boolean firstPlayThrough = true;
    private ISynchronization synchronization;


    // Single player constructor
    public GamePresenter()
    {
        multiplayerAvailable = false;
        this.synchronization = null;
    }

    // Multi player constructor
    public GamePresenter(ISynchronization synchronization)
    {
        multiplayerAvailable = true;
        this.synchronization = synchronization;
    }

    @Override
    public void create()
    {
        // Adjust view
        mainCamera.zoom = 1.5f;

        // Player entity
        firstPlayer = entityFactory.create("first_player");
        firstPlayer.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

        // Define screens
        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen(synchronization);
        highScoreScreen = new HighScoreScreen(synchronization);
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();

        // Load levels and create scenes
        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                mainCamera, world, ecs);
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                mainCamera, world, ecs);

        // Define states, states are made up of one screen and one scene
        viewStateInGame = new ViewState(levelOneScene, inGameScreen);
        viewStateGameOver = new ViewState(levelOneScene, gameOverScreen);
        viewStateMenu = new ViewState(menuScene, menuScreen);
        viewStateHighScore = new ViewState(menuScene, highScoreScreen);
        viewStateSettings = new ViewState(menuScene, settingsScreen);
        viewStateTutorial = new ViewState(menuScene, tutorialScreen);

        // ECS systems
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);
        RenderingSystem renderingSystem = new RenderingSystem(mainCamera);
        PhysicsSystem physics = new PhysicsSystem(world);
        ControlSystem controlSystem = new ControlSystem(world);
        StateSystem state = new StateSystem();
        SoundSystem sound = new SoundSystem();
        LevelManagementSystem cleanup = new LevelManagementSystem(world, synchronization,viewMachine,viewStateGameOver,gameOverScreen);

        //soundEntities
            Entity menuMusic = new Entity();
        menuMusic.add(new MusicComponent(Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"))));
        ecs.addEntity(menuMusic);
        Entity inGameMusic = new Entity();
        inGameMusic.add(new MusicComponent(Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3"))));

        /*set music for menu and start playing.
        Set music for gameplay, but do not start it.
        Music not in assetManager because larger sound files needs to be streamed as Gdx.audio-type
        and not Gdx.Sound type*/
        //sound.soundOn = true;
        //menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"));
        //inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3"));
        //inGameMusic.setVolume(0.5f);
        //menuMusic.setLooping(true);
        //inGameMusic.setLooping(true);
        //menuMusic.play();

        ecs.addSystem(sound);

        // Link game start
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) ->
        {


            //on first play through set the following entities to ECS
            if (firstPlayThrough) {
                ecs.addEntity(firstPlayer);
                ecs.addSystem(state);
                ecs.addSystem(cameraFollowSystem);
                ecs.addSystem(renderingSystem);
                ecs.addSystem(controlSystem);
                ecs.addSystem(physics);

                ecs.addSystem(new PhysicsDebugSystem(world, mainCamera));
                ecs.addSystem(sound);
                ecs.addSystem(new FPSDebugSystem());
                ecs.addSystem(new AnimationControlSystem());
                ecs.addSystem(new InteractionSystem(world));
                ecs.addSystem(new VisualSystem());
                ecs.addSystem(cleanup);

                firstPlayThrough = false;
            }
            //always set player entity when switching to in-game mode
            else {
                levelOneScene.repopulate(new TmxMapLoader().load("levels/level1.tmx"),
                        world, ecs);
                firstPlayer.getComponent(PlayerComponent.class).reset();
                firstPlayer.getComponent(DynamicBodyComponent.class).position(firstPlayer.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                firstPlayer.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(firstPlayer);
                ecs.addSystem(controlSystem);
            }

            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).play = false;
            //ecs.removeEntity(menuMusic);
            ecs.addEntity(inGameMusic);

            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);
        });

        // Link game start
        ((MenuScreen) menuScreen).getMultiplayerEvent().addHandler((args) ->
        {

            if(multiplayerAvailable)
            {
                synchronization.startQuickGame();
            }


            // PLAYER ENTITY
            Entity player1 = entityFactory.create("second_player");



            //on first play through set the following entities to ECS

            if (firstPlayThrough) {
                ecs.addEntity(firstPlayer);
                ecs.addEntity(player1);
                ecs.addSystem(state);
                ecs.addSystem(cameraFollowSystem);
                ecs.addSystem(renderingSystem);
                ecs.addSystem(controlSystem);
                ecs.addSystem(physics);
                ecs.addSystem(new PhysicsDebugSystem(world, mainCamera));
                ecs.addSystem(sound);
                ecs.addSystem(new FPSDebugSystem());
                ecs.addSystem(new AnimationControlSystem());
                ecs.addSystem(new InteractionSystem(world));
                firstPlayThrough = false;
            }

            //always set player entity when switching to in-game mode
            else {

                levelOneScene.repopulate(new TmxMapLoader().load("levels/level1.tmx"),
                        world, ecs);
                firstPlayer.getComponent(PlayerComponent.class).reset();
                firstPlayer.getComponent(DynamicBodyComponent.class).position(firstPlayer.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                firstPlayer.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(firstPlayer);
                player1.getComponent(PlayerComponent.class).reset();
                player1.getComponent(DynamicBodyComponent.class).position(firstPlayer.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                player1.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(player1);

            }


            if(multiplayerAvailable)
            {
                synchronization.setInputHandler((NetworkControlModule)player1.getComponent(ControlComponent.class).controlModule);
            }



            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).play = false;
            //ecs.removeEntity(menuMusic);
            ecs.addEntity(inGameMusic);

            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);

        });

        // Create screen and scene for future view state assembly



        // Add states to the state machine
        viewMachine.addState(viewStateInGame);
        viewMachine.addState(viewStateGameOver);
        viewMachine.addState(viewStateMenu);
        viewMachine.addState(viewStateHighScore);
        viewMachine.addState(viewStateSettings);
        viewMachine.addState(viewStateTutorial);

        // Define transition between states
        Transition menuToHighScore = new Transition(viewStateMenu, viewStateHighScore);
        Transition menuToInGame = new Transition(viewStateMenu, viewStateInGame);
        Transition menuToSettings = new Transition(viewStateMenu, viewStateSettings);
        Transition menuToTutorial = new Transition(viewStateMenu, viewStateTutorial);
        Transition tutorialToMenu = new Transition(viewStateTutorial, viewStateMenu);
        Transition settingsToMenu = new Transition(viewStateSettings, viewStateMenu);
        Transition inGameToGameOver = new Transition(viewStateInGame, viewStateGameOver);
        Transition gameOverToIngame = new Transition(viewStateGameOver, viewStateInGame);
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
        viewMachine.addTransition(gameOverToIngame);
        viewMachine.addTransition(gameOverToHighScore);
        viewMachine.addTransition(gameOverToMenu);
        viewMachine.addTransition(highScoreToMenu);
        viewMachine.addTransition(inGameToHighScore);
        viewMachine.addTransition(menuToTutorial);
        viewMachine.addTransition(tutorialToMenu);

        // Setting the entry point == initial state
        viewMachine.setInitialState(viewStateMenu);

        // Set inputProcessor to entry point's BaseScreen's Stage
        Gdx.input.setInputProcessor(menuScreen.getStage());


        /*all the events that will result from pressing a button somewhere in the game are defined
        bellow. For every possible button-press-action the corresponding Event adds a handler that
         subscribes to this Event.
         Whenever the button is pressed, this EventHandler will perform it's handle()-method.
        addHandler((args) -> {code...}); defines what the handle()-method for that specific
        eventHandlerwill do
        */

        //settings button pressed from main menu
        ((MenuScreen) menuScreen).getSettingsEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(settingsScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateSettings);
        });

        //highscore button pressed from main menu
        ((MenuScreen) menuScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            ((HighScoreScreen)highScoreScreen).populateHighscoreList();
            // call on state machine to change state
            viewMachine.nextState(viewStateHighScore);
        });

        //tutorial button pressed in main menu
        ((MenuScreen) menuScreen).getTutorialEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(tutorialScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateTutorial);
        });

        //google log in button pressed in main menu
        ((MenuScreen) menuScreen).getGoogleLoginEvent().addHandler((args) -> {
            // invoke google play sign in
            synchronization.startSignInIntent();
        });



        //sound button pressed from gameplay
        ((InGameScreen) inGameScreen).getSoundEvent().addHandler((args) -> {
            //if sound is turned on: pause game music. Set the sound-button in settingsscreen to
            //not be toggled, and vice versa

            ((SettingsScreen) settingsScreen).setSoundOn(!sound.soundOn);
            sound.soundOn = !sound.soundOn;

        });



        ((InGameScreen) inGameScreen).getLeftEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("L");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).leftStart();
        });

        ((InGameScreen) inGameScreen).getStopLeftEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SL");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).leftEnd();
        });

        ((InGameScreen) inGameScreen).getRightEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("R");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).rightStart();
        });

        ((InGameScreen) inGameScreen).getStopRightEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SR");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).rightEnd();
        });


        //shoot button pressed in-game. sets boolean variable "shooting" to true. this causes the
        //GamePresenter's update method to perform shooting action
        ((InGameScreen) inGameScreen).getShootEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("S");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).attackStart();
        });

        ((InGameScreen) inGameScreen).getStopshootEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SS");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).attackEnd();
        });

        //jump button pressed in-game. Currently this causes the the game to go from play-state to
        //gameOver-state
        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("J");

            ((EventControlModule) firstPlayer.getComponent(ControlComponent.class).controlModule).jumpStart();
        });


        ((InGameScreen) inGameScreen).getMenuEvent().addHandler((args) -> {
            //remove Player entity to stop it rendering whilst not in-game
                ecs.addEntity(new Entity().add(new CleanupComponent(firstPlayer)));

        });
        //new game pressed from gameOver state
        /*
        TODO: implement this method correctly. this is currently an invalid state transition
         */
        ((GameOverScreen) gameOverScreen).getNewGameEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);
            ((MenuScreen) menuScreen).getNewGameEvent().invoke(null);
        });

        //press highscore button from game over screen
        ((GameOverScreen) gameOverScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            ((HighScoreScreen)highScoreScreen).populateHighscoreList();
            // call on state machine to change state
            viewMachine.nextState(viewStateHighScore);
        });

        //main menu button pressed from game over screen
        ((GameOverScreen) gameOverScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        //Highscore button pressed from main menu screen
        ((HighScoreScreen) highScoreScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        //sound button pressed from settings screen
        ((SettingsScreen) settingsScreen).getSoundEvent().addHandler((args) -> {
            //if sound is currently playing: pause menu-music. Set in-game sound to be off.
            //set boolean variable "soundOn" to false

            ((InGameScreen) inGameScreen).setSoundOn(!sound.soundOn);
            sound.soundOn = !sound.soundOn;

        });

        //if back button pressed from settings screen:
        ((SettingsScreen) settingsScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        //tutorial button pressed from main menu screen
        ((TutorialScreen) tutorialScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });
    }

    public void update(float delta)
    {
        // If multi player game, synchronize
        if(multiplayerAvailable)
        {
            synchronization.synchronize();
        }

        // Update ECS
        ecs.update(delta);
    }

    @Override
    public void render()
    {
        // Render
        super.render();
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
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

        // Get virtual pixels
        int virtualWidth = (int)(((Constants.VIRTUAL_HEIGHT * width) / (float)height) / Constants.PPM);
        int virtualHeight = (int)(Constants.VIRTUAL_HEIGHT / Constants.PPM);

        // Resize
        mainCamera.setToOrtho(false,virtualWidth, virtualHeight);
        mainCamera.update(true);

        // Get current view state and resize it
        ((Screen)viewMachine.getCurrentState()).resize(width, height);
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
