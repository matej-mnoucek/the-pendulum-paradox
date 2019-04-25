package com.thependulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.thependulumparadox.control.EventControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.levels.ILevelManager;
import com.thependulumparadox.levels.LevelManager;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.system.AnimationControlSystem;
import com.thependulumparadox.model.system.LevelBoundarySystem;
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
import com.thependulumparadox.model.system.CameraFollowSystem;
import com.thependulumparadox.model.system.ControlSystem;
import com.thependulumparadox.model.system.RenderingSystem;
import com.thependulumparadox.model.system.PhysicsSystem;
import com.thependulumparadox.observer.ValueEventArgs;
import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.state.StateMachine;
import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.scene.GameScene;
import com.thependulumparadox.view.screen.BaseScreen;
import com.thependulumparadox.view.screen.GameOverScreen;
import com.thependulumparadox.view.screen.HighScoreScreen;
import com.thependulumparadox.view.screen.InGameScreen;
import com.thependulumparadox.view.screen.MenuScreen;
import com.thependulumparadox.view.screen.SettingsScreen;
import com.thependulumparadox.view.screen.TutorialScreen;

/**
 * The main control class of the whole game.
 * It is closer to MVP pattern than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{
    private static final Color CLEAR_COLOR = new Color(0.50f,0.70f,1f,1f);

    // Component based system root
    Engine ecs = new Engine();

    // Physics world
    World world = new World(new Vector2(0, -10), true);

    // Entity factory
    AbstractEntityFactory entityFactory = new EntityFactory(world);

    // MVP view state machine
    IStateMachine viewMachine = new StateMachine(true);

    // Level manager
    ILevelManager levels = new LevelManager(viewMachine);

    // Camera
    OrthographicCamera mainCamera = new OrthographicCamera();

    // Predefined view states for view machine
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
    Entity mainPlayer = null;
    Entity secondPlayer = null;

    // Multi player
    private boolean multiplayerAvailable = false;
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
        // Player entity
        mainPlayer = entityFactory.create("first_player");



        // Define screens
        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen();
        highScoreScreen = new HighScoreScreen();
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();

        // Create menu view states, states are made up of one screen and one scene
        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                mainCamera, world, ecs);
        viewStateMenu = new ViewState(menuScene, menuScreen);
        viewStateHighScore = new ViewState(menuScene, highScoreScreen);
        viewStateSettings = new ViewState(menuScene, settingsScreen);
        viewStateTutorial = new ViewState(menuScene, tutorialScreen);


        // Load levels and add them to the manager
        mainCamera.zoom = 1.5f;
        GameScene levelScene1 = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                mainCamera, world, ecs);
        GameScene levelScene2 = new GameScene(new TmxMapLoader().load("levels/level2.tmx"),
                mainCamera, world, ecs);

        levels.addLevel(levelScene1, inGameScreen, gameOverScreen);
        levels.addLevel(levelScene2, inGameScreen, gameOverScreen);

        // Add states to the state machine
        viewMachine.addState(viewStateMenu);
        viewMachine.addState(viewStateHighScore);
        viewMachine.addState(viewStateSettings);
        viewMachine.addState(viewStateTutorial);

        // Setting the entry point == initial state
        viewMachine.setInitialState(viewStateMenu);

        // Set inputProcessor to entry point's BaseScreen's Stage
        Gdx.input.setInputProcessor(menuScreen.getStage());


        // ECS systems
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);
        RenderingSystem renderingSystem = new RenderingSystem(mainCamera);
        PhysicsSystem physicsSystem = new PhysicsSystem(world);
        ControlSystem controlSystem = new ControlSystem(world);
        StateSystem stateSystem = new StateSystem();
        SoundSystem soundSystem = new SoundSystem();
        InteractionSystem interactionSystem = new InteractionSystem(world);
        LevelBoundarySystem levelBoundarySystem = new LevelBoundarySystem();
        AnimationControlSystem animationControlSystem = new AnimationControlSystem();
        VisualSystem visualSystem = new VisualSystem();

        // Menu entities
        Entity menuMusic = new Entity();
        menuMusic.add(new MusicComponent(
                Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"))));
        ecs.addEntity(menuMusic);

        Entity inGameMusic = new Entity();
        MusicComponent musicComponent = new MusicComponent(
                Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3")));
        musicComponent.play = false;
        inGameMusic.add(musicComponent);
        ecs.addEntity(inGameMusic);
        ecs.addSystem(soundSystem);


        // Add some DEBUG systems
        ecs.addSystem(new PhysicsDebugSystem(world, mainCamera));
        ecs.addSystem(new FPSDebugSystem());


        // LINK MENU LABELS TO VALUES
        interactionSystem.playerUpdate.addHandler((args)->
        {
            ValueEventArgs<PlayerComponent> vargs = args;
            ((InGameScreen)inGameScreen).setScore(vargs.value.score);
            ((InGameScreen)inGameScreen).setLives(vargs.value.current.lives);
        });


        // TWO WAYS HOW CAN PLAYER DIE == FALLING OUT OF BOUNDS, DYING
        // Player out of bounds
        levelBoundarySystem.playerOutOfBounds.addHandler((args) ->
        {
            levelBoundarySystem.checkBoundaries = false;


            // Remove players from the system
            ecs.removeEntity(mainPlayer);

            // Clear forces and reset position
            DynamicBodyComponent body = mainPlayer.getComponent(DynamicBodyComponent.class);
            body.body.setLinearVelocity(0,0);
            body.body.setAngularVelocity(0);
            body.position(new Vector2(0,0));

            // Deal with second player
            if (secondPlayer != null)
            {
                // Remove players from the system
                ecs.removeEntity(secondPlayer);

                // Clear forces and reset position
                DynamicBodyComponent body2 = secondPlayer.getComponent(DynamicBodyComponent.class);
                body2.body.setLinearVelocity(0,0);
                body2.body.setAngularVelocity(0);
                body2.position(new Vector2(0,0));
            }


            //Change music
            ((MusicComponent)menuMusic.getComponents().get(0)).play = true;
            ((MusicComponent)inGameMusic.getComponents().get(0)).play = false;

            // Delete all level entities
            ((GameScene)levels.currentLevelScene()).destroyEntities();

            // Show game over screen
            Gdx.input.setInputProcessor(levels.currentGameOverViewState().getScreen().getStage());
            viewMachine.nextState(levels.currentGameOverViewState());
        });

        // Link interaction system events
        interactionSystem.playerDeath.addHandler((args) ->
        {
            levelBoundarySystem.checkBoundaries = false;


            // Remove players from the system
            ecs.removeEntity(mainPlayer);

            // Clear forces and reset position
            DynamicBodyComponent body = mainPlayer.getComponent(DynamicBodyComponent.class);
            body.body.setLinearVelocity(0,0);
            body.body.setAngularVelocity(0);
            body.position(new Vector2(0,0));

            // Deal with second player
            if (secondPlayer != null)
            {
                // Remove players from the system
                ecs.removeEntity(secondPlayer);

                // Clear forces and reset position
                DynamicBodyComponent body2 = secondPlayer.getComponent(DynamicBodyComponent.class);
                body2.body.setLinearVelocity(0,0);
                body2.body.setAngularVelocity(0);
                body2.position(new Vector2(0,0));
            }


            //Change music
            ((MusicComponent)menuMusic.getComponents().get(0)).play = true;
            ((MusicComponent)inGameMusic.getComponents().get(0)).play = false;

            // Delete all level entities
            ((GameScene)levels.currentLevelScene()).destroyEntities();

            // Show game over screen
            Gdx.input.setInputProcessor(levels.currentGameOverViewState().getScreen().getStage());
            viewMachine.nextState(levels.currentGameOverViewState());
        });

        // THE GAME CAN ALSO END ARTIFICIALLY BY GETTING BACK TO MAIN MENU
        ((InGameScreen) inGameScreen).getMenuEvent().addHandler((args) ->
        {
            levelBoundarySystem.checkBoundaries = false;


            // Remove players from the system
            ecs.removeEntity(mainPlayer);

            // Clear forces and reset position
            DynamicBodyComponent body = mainPlayer.getComponent(DynamicBodyComponent.class);
            body.body.setLinearVelocity(0,0);
            body.body.setAngularVelocity(0);
            body.position(new Vector2(0,0));

            // Deal with second player
            if (secondPlayer != null)
            {
                // Remove players from the system
                ecs.removeEntity(secondPlayer);

                // Clear forces and reset position
                DynamicBodyComponent body2 = secondPlayer.getComponent(DynamicBodyComponent.class);
                body2.body.setLinearVelocity(0,0);
                body2.body.setAngularVelocity(0);
                body2.position(new Vector2(0,0));
            }

            //Change music
            ((MusicComponent)menuMusic.getComponents().get(0)).play = true;
            ((MusicComponent)inGameMusic.getComponents().get(0)).play = false;

            // Delete all level entities
            ((GameScene)levels.currentLevelScene()).destroyEntities();

            // Set menu transition
            Gdx.input.setInputProcessor(menuScreen.getStage());
            viewMachine.nextState(viewStateMenu);
        });



        // ONE WAY HOW CAN PLAYER FINISH A LEVEL == ALL PLAYERS REACH LEVEL END
        // Next level
        levelBoundarySystem.levelEndReached.addHandler((args)->
        {
            // If last level go to the first level
            if (levels.isCurrentLevelLast())
            {
                // Clean existing level entities
                levels.currentLevelScene().dispose();
                levels.reset();
            }
            else
            {
                levels.currentLevelScene().dispose();
                levels.nextLevel();
            }


            // Move player to the correct spot
            GameScene scene = (GameScene) levels.currentLevelScene();
            mainPlayer.getComponent(PlayerComponent.class).defaults();
            mainPlayer.getComponent(DynamicBodyComponent.class).position(scene.getStartPoint());
            mainPlayer.getComponent(DynamicBodyComponent.class).wakeup();

            // Do view transition
            viewMachine.nextState(levels.currentInGameViewState());
            ((GameScene)levels.currentLevelScene()).populate();

            // Reset control system in order to include newly generated entities
            ecs.addSystem(controlSystem);
            ecs.addSystem(stateSystem);
        });


        // HOW TO START A GAME
        // Link game start event
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) ->
        {
            // Add player
            ecs.addEntity(mainPlayer);
            mainPlayer.getComponent(PlayerComponent.class).defaults();
            mainPlayer.getComponent(DynamicBodyComponent.class)
                    .position(((GameScene)levels.currentLevelScene()).getStartPoint())
                    .wakeup();

            // Create entities for the first level
            ((GameScene)levels.currentLevelScene()).populate();

            // Include system for checking player position in the level
            ecs.addSystem(levelBoundarySystem);
            levelBoundarySystem.levelEndPoint = ((GameScene)levels.currentLevelScene()).getEndPoint();
            levelBoundarySystem.checkBoundaries = true;

            // Add all basic systems
            ecs.addSystem(stateSystem);
            ecs.addSystem(cameraFollowSystem);
            ecs.addSystem(renderingSystem);
            ecs.addSystem(controlSystem);
            ecs.addSystem(physicsSystem);
            ecs.addSystem(animationControlSystem);
            ecs.addSystem(interactionSystem);
            ecs.addSystem(visualSystem);


            // Set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());

            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).play = false;
            ((MusicComponent)inGameMusic.getComponents().get(0)).play = true;

            // call on state machine to change state
            viewMachine.nextState(levels.currentInGameViewState());
        });

        // Link game start
        ((MenuScreen) menuScreen).getMultiPlayerEvent().addHandler((args) ->
        {
            if(multiplayerAvailable)
            {
                synchronization.startQuickGame();
            }

            // Create second player entity
            secondPlayer = entityFactory.create("second_player");
            Vector2 player2Position = ((GameScene)levels.currentLevelScene()).getStartPoint();
            // Move player a bit to the side in order to prevent overlap
            player2Position.x += 1;
            secondPlayer.getComponent(DynamicBodyComponent.class)
                    .position(player2Position)
                    .wakeup();
            ecs.addEntity(secondPlayer);

            if(multiplayerAvailable)
            {
                synchronization.setInputHandler((NetworkControlModule)secondPlayer
                        .getComponent(ControlComponent.class).controlModule);
            }


            // Add main player
            ecs.addEntity(mainPlayer);
            mainPlayer.getComponent(PlayerComponent.class).defaults();
            mainPlayer.getComponent(DynamicBodyComponent.class)
                    .position(((GameScene)levels.currentLevelScene()).getStartPoint())
                    .wakeup();

            // Create entities for the first level
            ((GameScene)levels.currentLevelScene()).populate();

            // Include system for checking player position in the level
            ecs.addSystem(levelBoundarySystem);
            levelBoundarySystem.levelEndPoint = ((GameScene)levels.currentLevelScene()).getEndPoint();
            levelBoundarySystem.checkBoundaries = true;

            // Add all basic systems
            ecs.addSystem(stateSystem);
            ecs.addSystem(cameraFollowSystem);
            ecs.addSystem(renderingSystem);
            ecs.addSystem(controlSystem);
            ecs.addSystem(physicsSystem);
            ecs.addSystem(animationControlSystem);
            ecs.addSystem(interactionSystem);
            ecs.addSystem(visualSystem);


            // Set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());

            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).play = false;
            ((MusicComponent)inGameMusic.getComponents().get(0)).play = true;

            // call on state machine to change state
            viewMachine.nextState(levels.currentInGameViewState());

        });


        // REST OF BUTTON EVENTS FROM VARIOUS MENUS
        // Settings button pressed from main menu
        ((MenuScreen) menuScreen).getSettingsEvent().addHandler((args) ->
        {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(settingsScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateSettings);
        });

        // Highscore button pressed from main menu
        ((MenuScreen) menuScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            ((HighScoreScreen)highScoreScreen).populateHighScoreList("");
            // call on state machine to change state
            viewMachine.nextState(viewStateHighScore);
        });

        // Tutorial button pressed in main menu
        ((MenuScreen) menuScreen).getTutorialEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(tutorialScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateTutorial);
        });

        // Google log in button pressed in main menu
        ((MenuScreen) menuScreen).getGoogleLoginEvent().addHandler((args) -> {
            // invoke google play sign in
            synchronization.startSignInIntent();
        });

        // Sound button pressed from gameplay
        ((InGameScreen) inGameScreen).getSoundEvent().addHandler((args) ->
        {
            ((SettingsScreen) settingsScreen).setSound(!soundSystem.soundOn);
            soundSystem.soundOn = !soundSystem.soundOn;
        });

        // new game pressed from gameOver state
        ((GameOverScreen) gameOverScreen).getNewGameEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(levels.currentInGameViewState());

            ((MenuScreen) menuScreen).getNewGameEvent().invoke(null);
        });

        // press highscore button from game over screen
        ((GameOverScreen) gameOverScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
            ((HighScoreScreen)highScoreScreen).populateHighScoreList("");
            // call on state machine to change state
            viewMachine.nextState(viewStateHighScore);
        });

        // main menu button pressed from game over screen
        ((GameOverScreen) gameOverScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        // Highscore button pressed from main menu screen
        ((HighScoreScreen) highScoreScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        // sound button pressed from settings screen
        ((SettingsScreen) settingsScreen).getSoundEvent().addHandler((args) -> {
            //if sound is currently playing: pause menu-music. Set in-game sound to be off.
            //set boolean variable "soundOn" to false

            ((InGameScreen) inGameScreen).setSound(!soundSystem.soundOn);
            soundSystem .soundOn = !soundSystem.soundOn;

        });

        // if back button pressed from settings screen:
        ((SettingsScreen) settingsScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });

        // tutorial button pressed from main menu screen
        ((TutorialScreen) tutorialScreen).getMenuEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(menuScreen.getStage());
            // call on state machine to change state
            viewMachine.nextState(viewStateMenu);
        });



        // CONTROL EVENTS
        ((InGameScreen) inGameScreen).getLeftEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("L");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).leftStart();
        });

        ((InGameScreen) inGameScreen).getStopLeftEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SL");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).leftEnd();
        });

        ((InGameScreen) inGameScreen).getRightEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("R");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).rightStart();
        });

        ((InGameScreen) inGameScreen).getStopRightEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SR");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).rightEnd();
        });

        ((InGameScreen) inGameScreen).getShootEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("S");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).attackStart();
        });

        ((InGameScreen) inGameScreen).getStopShootEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("SS");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).attackEnd();
        });

        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {

            if(multiplayerAvailable)
                synchronization.sendAction("J");

            ((EventControlModule) mainPlayer.getComponent(ControlComponent.class).controlModule).jumpStart();
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
