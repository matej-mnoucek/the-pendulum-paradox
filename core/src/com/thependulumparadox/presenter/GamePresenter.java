package com.thependulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.Array;
import com.thependulumparadox.control.EventControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.control.AIJumpAttackControlModule;
import com.thependulumparadox.control.AIRunAroundControlModule;
import com.thependulumparadox.control.EventControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.model.component.CleanupComponent;
import com.thependulumparadox.model.component.CoinComponent;
import com.thependulumparadox.model.component.EnhancementComponent;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.control.ControlModule;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.enhancement.AddDefenseEnhancement;
import com.thependulumparadox.model.component.enhancement.AddLifeEnhancement;
import com.thependulumparadox.model.component.enhancement.Enhancement;
import com.thependulumparadox.model.component.enhancement.MultiplyDamageEnhancement;
import com.thependulumparadox.model.system.AnimationControlSystem;
import com.thependulumparadox.model.system.CleanupSystem;
import com.thependulumparadox.model.system.InteractionSystem;
import com.thependulumparadox.model.system.FPSDebugSystem;
import com.thependulumparadox.model.system.PhysicsDebugSystem;

import com.thependulumparadox.model.system.SoundSystem;

import com.thependulumparadox.model.system.StateSystem;
import com.thependulumparadox.model.system.VisualSystem;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.entity.AbstractEntityFactory;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.entity.EntityFactory;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.EntityBuilder;
import com.thependulumparadox.model.entity.IEntityBuilder;
import com.thependulumparadox.model.system.CameraFollowSystem;
import com.thependulumparadox.model.system.ControlSystem;
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

import java.util.ArrayList;

//TODO: implement "new game" pressed from GameOverScreen.

/**
 * The main control class of the whole game.
 * It is closer to MVP than to MVC and that's why it's called Presenter
 */
public class GamePresenter extends Game
{
    // Component based system root
    Engine ecs = new Engine();

    // Physics world
    World world = new World(new Vector2(0, -10), true);

    // Component factory
    AbstractEntityFactory entityFactory = new EntityFactory(world);

    // Entity builder
    IEntityBuilder entityBuilder = new EntityBuilder();


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


    private boolean shooting;
    private float shootingTimer = 0;

    // DEBUG
    ShapeRenderer shapeRenderer;
    Entity player;
    TransformComponent transformComponent;
    StateComponent playerState;


    private ISynchronization proxy;



    // Multi player
    public GamePresenter(ISynchronization proxy)
    {
        this.proxy = proxy;
    }

    @Override
    public void create()
    {
        // Adjust view
        mainCamera.zoom = 1.5f;

        // PLAYER ENTITY
        player = entityFactory.create("first_player");
        player.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));


        // ENEMY EXAMPLE
/*        Entity enemy = entityFactory.create("knight_enemy");
        enemy.getComponent(DynamicBodyComponent.class).position(new Vector2(10,8));
        ecs.addEntity(enemy);*/

        ////create shadow player



        //define screens
        inGameScreen = new InGameScreen();
        gameOverScreen = new GameOverScreen();
        menuScreen = new MenuScreen(proxy);
        highScoreScreen = new HighScoreScreen(proxy);
        settingsScreen = new SettingsScreen();
        tutorialScreen = new TutorialScreen();



        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera, ecs);
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"),
                world, mainCamera, ecs);

        //define states. states are made up of one screen and one scene
        viewStateInGame = new ViewState(levelOneScene, inGameScreen);
        viewStateGameOver = new ViewState(levelOneScene, gameOverScreen);
        viewStateMenu = new ViewState(menuScene, menuScreen);
        viewStateHighScore = new ViewState(menuScene, highScoreScreen);
        viewStateSettings = new ViewState(menuScene, settingsScreen);
        viewStateTutorial = new ViewState(menuScene, tutorialScreen);
        // ECS Systems

        // Camera follow
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);

        // Rendering
        RenderingSystem renderingSystem = new RenderingSystem(mainCamera);

        // Physics
        PhysicsSystem physics = new PhysicsSystem(world);

        // Control
        ControlSystem controlSystem = new ControlSystem(entityFactory);

        // States
        StateSystem state = new StateSystem();

        //Sound
        SoundSystem sound = new SoundSystem();

        //cleanup
        CleanupSystem cleanup = new CleanupSystem(world,proxy,viewMachine,viewStateGameOver,gameOverScreen);

        ecs.addSystem(sound);

        //populate assetmanager with assets
        assetManager.load("sounds/die.mp3", Sound.class);
        assetManager.load("sounds/GameOver.mp3", Sound.class);
        assetManager.load("sounds/reload.mp3", Sound.class);
        //assetManager.load("sounds/enemy_dead.mp3", Sound.class);

        assetManager.finishLoading();

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
        sound.soundOn = true;
        //menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"));
        //inGameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/inGameMusic.mp3"));
        //inGameMusic.setVolume(0.5f);
        //menuMusic.setLooping(true);
        //inGameMusic.setLooping(true);
        //menuMusic.play();


        // Link game start
        ((MenuScreen) menuScreen).getNewGameEvent().addHandler((args) ->
        {


            //on first play through set the following entities to ECS
            if (firstPlayThrough) {
                ecs.addEntity(player);
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
                player.getComponent(PlayerComponent.class).reset();
                player.getComponent(DynamicBodyComponent.class).position(player.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                player.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(player);
                ecs.addSystem(controlSystem);
            }

            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).stop();
            ecs.removeEntity(menuMusic);
            ecs.addEntity(inGameMusic);

            // call on state machine to change state
            viewMachine.nextState(viewStateInGame);
        });

        // Link game start
        ((MenuScreen) menuScreen).getMultiplayerEvent().addHandler((args) ->
        {


            proxy.startQuickGame();


            // PLAYER ENTITY
            Entity player1 = entityFactory.create("second_player");



            //on first play through set the following entities to ECS

            if (firstPlayThrough) {
                ecs.addEntity(player);
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
                player.getComponent(PlayerComponent.class).reset();
                player.getComponent(DynamicBodyComponent.class).position(player.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                player.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(player);
                player1.getComponent(PlayerComponent.class).reset();
                player1.getComponent(DynamicBodyComponent.class).position(player.getComponent(TransformComponent.class).position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                player1.getComponent(DynamicBodyComponent.class).position(new Vector2(5,4));

                ecs.addEntity(player1);

            }
            proxy.setInputHandler((NetworkControlModule)player1.getComponent(ControlComponent.class).controlModule);



            //set inGameScreen's stage as the input processor
            Gdx.input.setInputProcessor(inGameScreen.getStage());
            //stop menu music. will call stop() method on menu music even if sound is currently turned off
            ((MusicComponent)menuMusic.getComponents().get(0)).stop();
            ecs.removeEntity(menuMusic);
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
            proxy.startSignInIntent();
        });



        //sound button pressed from gameplay
        ((InGameScreen) inGameScreen).getSoundEvent().addHandler((args) -> {
            //if sound is turned on: pause game music. Set the sound-button in settingsscreen to
            //not be toggled, and vice versa

                ((SettingsScreen) settingsScreen).setSoundOn(!sound.soundOn);
                sound.soundOn = !sound.soundOn;

        });



        ((InGameScreen) inGameScreen).getLeftEvent().addHandler((args) -> {
            proxy.sendAction("L");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).leftStart();
        });

        ((InGameScreen) inGameScreen).getStopLeftEvent().addHandler((args) -> {
            proxy.sendAction("SL");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).leftEnd();
        });

        ((InGameScreen) inGameScreen).getRightEvent().addHandler((args) -> {
            proxy.sendAction("R");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).rightStart();
        });

        ((InGameScreen) inGameScreen).getStopRightEvent().addHandler((args) -> {
            proxy.sendAction("SR");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).rightEnd();
        });


        //shoot button pressed in-game. sets boolean variable "shooting" to true. this causes the
        //GamePresenter's update method to perform shooting action
        ((InGameScreen) inGameScreen).getShootEvent().addHandler((args) -> {
            proxy.sendAction("S");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).attackStart();

        });

        ((InGameScreen) inGameScreen).getStopshootEvent().addHandler((args) -> {
            proxy.sendAction("SS");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).attackEnd();
        });

        //jump button pressed in-game. Currently this causes the the game to go from play-state to
        //gameOver-state
        ((InGameScreen) inGameScreen).getJumpEvent().addHandler((args) -> {
            proxy.sendAction("J");
            ((EventControlModule) player.getComponent(ControlComponent.class).controlModule).jumpStart();

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


        ((InGameScreen) inGameScreen).getMenuEvent().addHandler((args) -> {
            //remove Player entity to stop it rendering whilst not in-game
                ecs.addEntity(new Entity().add(new CleanupComponent(player)));

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
        /*if shoot button is pressed down: variable "shooting" is set to true.
        Shooting timer counts the time since the last bullet fired.
        Every 100ms a gunshot sound is played,
        and decrementAmmo() subtracts 1 from inGameScreen's ammoLabel in HUD
        when shooting button is released: varuable "shooting" is set to false*/
      /*  if(shooting){
            shootingTimer += delta;
            if(shootingTimer > 0.1) {
                ((InGameScreen) inGameScreen).decrementAmmo();
                if(soundOn) {
                    assetManager.get("sounds/single_gunshot.mp3", Sound.class).play();
                }
                shootingTimer = 0;
            }
        }*/

        // TODO: Implement no proxy version
        proxy.handleActions();

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
