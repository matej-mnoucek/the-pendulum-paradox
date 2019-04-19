package com.thependulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SoundComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.system.PhysicsDebugSystem;
import com.thependulumparadox.model.system.ShootingSystem;
import com.thependulumparadox.model.system.SoundSystem;
import com.thependulumparadox.model.system.StateSystem;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.component.AbstractComponentFactory;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.ComponentFactory;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.EntityBuilder;
import com.thependulumparadox.model.entity.IEntityBuilder;
import com.thependulumparadox.model.system.CameraFollowSystem;
import com.thependulumparadox.model.system.InputSystem;
import com.thependulumparadox.model.system.RenderingSystem;
import com.thependulumparadox.model.system.PhysicsSystem;
import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.state.StateMachine;
import com.thependulumparadox.state.TaggedState;
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

import net.java.games.input.Component;

//TODO: implement "new game" pressed from GameOverScreen.

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


    private boolean soundOn;
    private boolean shooting;
    private float shootingTimer = 0;

    // DEBUG
    ShapeRenderer shapeRenderer;
    Entity player;
    Entity enemy1;
    Entity enemy2;
    TransformComponent transformComponent;
    StateComponent<String> playerState;

    private boolean isMultiplayer = false;
    private ISynchronization proxy;


    // Single player
    public GamePresenter() { isMultiplayer = false; };

    // Multi player
    public GamePresenter(ISynchronization proxy)
    {
        this.proxy = proxy;
        this.isMultiplayer = true;
    }

    @Override
    public void create()
    {
        // DEBUG
        //debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setProjectionMatrix(mainCamera.combined);

        // PLAYER ENTITY
        player = new Entity();
        player.flags = 2;
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
        DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(world);
        dynamicBodyComponent.position(transformComponent.position).dimension(0.7f, 1.5f);
        player.add(dynamicBodyComponent);
        PlayerComponent playerComponent = new PlayerComponent();
        player.add(playerComponent);
        playerState = new StateComponent<>();
        TaggedState<String> initialState = new TaggedState<String>("idle", null);
        playerState.initialState = initialState;
        playerState.states.add(initialState);
        playerState.states.add(new TaggedState<String>("attack", null));
        playerState.states.add(new TaggedState<String>("jump", null));
        player.add(playerState);

        // ENEMY ENTITY
        enemy1 = new Entity();
        enemy1.flags = 4;
        TransformComponent transform = new TransformComponent();
        transform.position = new Vector2(10, 8);
        enemy1.add(transform);
        AnimatedSpriteComponent animatedEnemy = new AnimatedSpriteComponent();
        animatedEnemy.frameDuration = 0.07f;
        animatedEnemy.height = 1.8f;
        animatedEnemy.width = 1.8f;
        animatedEnemy.atlasPath = "packed/ninja_enemy.atlas";
        animatedEnemy.region = "attack";
        enemy1.add(animatedEnemy);
        DynamicBodyComponent dynamicBody = new DynamicBodyComponent(world);
        dynamicBody.position(transform.position).dimension(0.7f, 1.5f);
        enemy1.add(dynamicBody);

        enemy2 = new Entity();
        enemy2.flags = 4;
        TransformComponent transform2 = new TransformComponent();
        transform2.position = new Vector2(20, 8);
        enemy2.add(transform2);
        AnimatedSpriteComponent animatedEnemy2 = new AnimatedSpriteComponent();
        animatedEnemy2.frameDuration = 0.07f;
        animatedEnemy2.height = 1.8f;
        animatedEnemy2.width = 1.8f;
        animatedEnemy2.atlasPath = "packed/knight_enemy.atlas";
        animatedEnemy2.region = "attack";
        enemy2.add(animatedEnemy2);
        DynamicBodyComponent dynamicBody2 = new DynamicBodyComponent(world);
        dynamicBody2.position(transform2.position).dimension(0.7f, 1.5f);
        enemy2.add(dynamicBody2);

        // ECS Systems

        // Camera follow
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);

        // Rendering
        RenderingSystem renderingSystem = new RenderingSystem(mainCamera);

        // Physics
        PhysicsSystem physics = new PhysicsSystem(world);

        // Control
        InputSystem input = new InputSystem();
        ShootingSystem shooting = new ShootingSystem("sprites/bullets/circle_bullet_blue.png", world);

        // States
        StateSystem state = new StateSystem();

        //Sound
        SoundSystem sound = new SoundSystem();
        ecs.addSystem(sound);
        //populate assetmanager with assets
        assetManager.load("sounds/single_gunshot.mp3", Sound.class);
        assetManager.load("sounds/coin_collect.mp3", Sound.class);
        assetManager.load("sounds/jump.mp3", Sound.class);
        assetManager.load("sounds/die.mp3", Sound.class);
        assetManager.load("sounds/GameOver.mp3", Sound.class);
        assetManager.load("sounds/reload.mp3", Sound.class);
        //assetManager.load("sounds/enemy_dead.mp3", Sound.class);

        assetManager.finishLoading();

        //soundEntities
        Entity shootSound = new Entity();
        shootSound.add(new SoundComponent(assetManager.get("sounds/single_gunshot.mp3",Sound.class),true));

        Entity menuMusic = new Entity();
        menuMusic.add(new MusicComponent(Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"))));
        ecs.addEntity(menuMusic);
        Entity inGameMusic = new Entity();
        inGameMusic.add(new MusicComponent(Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3"))));

        /*set music for menu and start playing.
        Set music for gameplay, but do not start it.
        Music not in assetManager because larger sound files needs to be streamed as Gdx.audio-type
        and not Gdx.Sound type*/
        soundOn = true;
        //menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"));
        //inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3"));
        //inGameMusic.setVolume(0.5f);
        //menuMusic.setLooping(true);
        //inGameMusic.setLooping(true);
        //menuMusic.play();

        //define screens
        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen(proxy);
        highScoreScreen = new HighScoreScreen();
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();

        // Link game start
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) ->
        {
            //on first play through set the following entities to ECS
            if (firstPlayThrough) {
                ecs.addEntity(player);
                ecs.addEntity(enemy1);
                ecs.addEntity(enemy2);
                ecs.addSystem(cameraFollowSystem);
                ecs.addSystem(renderingSystem);
                ecs.addSystem(input);
                ecs.addSystem(shooting);
                ecs.addSystem(physics);
                ecs.addSystem(state);
                ecs.addSystem(new PhysicsDebugSystem(world, mainCamera));
                ecs.addSystem(sound);

                firstPlayThrough = false;
            }
            //always set player entity when switching to in-game mode
            else {
                ecs.addEntity(player);
            }
            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ecs.removeEntity(menuMusic);
            //if sound is turned on: start playing in-game music
            if(soundOn) {
                ecs.addEntity(inGameMusic);
            }
            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);
        });

        // Link game start
        ((MenuScreen) menuScreen).getMultiplayerEvent().addHandler((args) ->
        {


            proxy.startQuickGame();

            proxy.setInputHandler(input);

            //on first play through set the following entities to ECS

            if (firstPlayThrough) {
                ecs.addEntity(player);
                ecs.addSystem(cameraFollowSystem);
                ecs.addSystem(renderingSystem);
                ecs.addSystem(physics);
                ecs.addSystem(input);
                ecs.addSystem(sound);
                firstPlayThrough = false;
            }

            //always set player entity when switching to in-game mode
            else {
                ecs.addEntity(player);
            }
            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ecs.removeEntity(menuMusic);
            //if sound is turned on: start playing in-game music
            if(soundOn) {
                ecs.addEntity(inGameMusic);
            }
            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);

        });

        // Create screen and scene for future view state assembly
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera);
        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera);

        //define states. states are made up of one screen and one scene
        viewStateInGame = new ViewState(levelOneScene, inGameScreen);
        viewStateGameOver = new ViewState(levelOneScene, gameOverScreen);
        viewStateMenu = new ViewState(menuScene, menuScreen);
        viewStateHighScore = new ViewState(menuScene, highScoreScreen);
        viewStateSettings = new ViewState(menuScene, settingsScreen);
        viewStateTutorial = new ViewState(menuScene, tutorialScreen);


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
            proxy.startSignInIntent();
        });



        //sound button pressed from gameplay
        ((InGameScreen) inGameScreen).getSoundEvent().addHandler((args) -> {
            //if sound is turned on: pause game music. Set the sound-button in settingsscreen to
            //not be toggled, and vice versa
            if(soundOn){
                ecs.removeEntity(inGameMusic);
                ((SettingsScreen) settingsScreen).setSoundOn(false);
                soundOn = false;
            } else {
                ecs.addEntity(inGameMusic);
                ((SettingsScreen) settingsScreen).setSoundOn(true);
                soundOn = true;
            }
        });

        ((InGameScreen) inGameScreen).getLeftEvent().addHandler((args) -> {
            //proxy.sendAction("L");
            input.moveLeft();
        });

        ((InGameScreen) inGameScreen).getStopLeftEvent().addHandler((args) -> {
            //proxy.sendAction("SL");
            input.stopMoveLeft();
        });

        ((InGameScreen) inGameScreen).getRightEvent().addHandler((args) -> {
            //proxy.sendAction("R");
            input.moveRight();
        });

        ((InGameScreen) inGameScreen).getStopRightEvent().addHandler((args) -> {
            //proxy.sendAction("SR");
            input.stopMoveRight();
        });


        //shoot button pressed in-game. sets boolean variable "shooting" to true. this causes the
        //GamePresenter's update method to perform shooting action
        ((InGameScreen) inGameScreen).getShootEvent().addHandler((args) -> {
            //proxy.sendAction("S");
       //     assetManager.get("sounds/single_gunshot.mp3", Sound.class).play();

            ecs.addEntity(shootSound);

            input.startShooting();
/*            if(shooting){
                shooting = false;
            } else{
                shooting = true;
            }*/
        });

        ((InGameScreen) inGameScreen).getStopshootEvent().addHandler((args) -> {
            //proxy.sendAction("SS");
            input.stopShooting();
        });

        //jump button pressed in-game. Currently this causes the the game to go from play-state to
        //gameOver-state
        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {

            input.jump();
            //proxy.sendAction("J");

            /*// set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(gameOverScreen.getStage());

            //stop game music and start menu music if sound is turned on
            inGameMusic.stop();
            if(soundOn) {
                menuMusic.play();
            }

            //remove Player entity to stop it rendering whilst not in-game
            ecs.removeEntity(player);

            // call on state machine to change state
            try {
                viewMachine.nextState(viewStateGameOver);
            } catch (EInvalidTransition eInvalidTransition) {
                eInvalidTransition.printStackTrace();
            } catch (EStateNotAvailable eStateNotAvailable) {
                eStateNotAvailable.printStackTrace();
            }
*/
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
        });

        //press highscore button from game over screen
        ((GameOverScreen) gameOverScreen).getHighScoreEvent().addHandler((args) -> {
            // set input processor to new State's BaseScreen stage
            Gdx.input.setInputProcessor(highScoreScreen.getStage());
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
            if(soundOn) {
                ecs.removeEntity(menuMusic);
                ((InGameScreen) inGameScreen).setSoundOn(false);
                soundOn = false;
            //if sound is currently not playing: start playing menu-music. set in-game music to be on
            //set boolean "soundOn" to true
            } else {
                ecs.addEntity(menuMusic);
                ((InGameScreen) inGameScreen).setSoundOn(true);
                soundOn = true;
            }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            playerState.requestedState = playerState.states.get(1);
            playerState.transitionRequested = true;
        }

        /*if shoot button is pressed down: variable "shooting" is set to true.
        Shooting timer counts the time since the last bullet fired.
        Every 100ms a gunshot sound is played,
        and decrementAmmo() subtracts 1 from inGameScreen's ammoLabel in HUD
        when shooting button is released: varuable "shooting" is set to false*/
        if(shooting){
            shootingTimer += delta;
            if(shootingTimer > 0.1) {
                ((InGameScreen) inGameScreen).decrementAmmo();
                if(soundOn) {
                    assetManager.get("sounds/single_gunshot.mp3", Sound.class).play();
                }
                shootingTimer = 0;
            }
        }

        //proxy.handleActions();

        // Update ECS
        ecs.update(delta);
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
}
