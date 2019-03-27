package com.pendulumparadox.presenter;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.model.component.AbstractComponentFactory;
import com.pendulumparadox.model.component.AnimatedSpriteComponent;
import com.pendulumparadox.model.component.CameraComponent;
import com.pendulumparadox.model.component.ComponentFactory;
import com.pendulumparadox.model.component.DynamicBodyComponent;
import com.pendulumparadox.model.component.TransformComponent;
import com.pendulumparadox.model.entity.EntityBuilder;
import com.pendulumparadox.model.entity.IEntityBuilder;
import com.pendulumparadox.model.system.CameraFollowSystem;
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
import com.badlogic.gdx.tools.texturepacker.*;

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



    // TEST ANIMATION
    public Animation<TextureRegion> bulletAnimation;
    public TextureAtlas atlas;
    public SpriteBatch spriteBatch;
    float stateTime = 0.0001f;

    @Override
    public void create()
    {

        // TEST ANIMATION
        /*
        TexturePacker.process("animations/hero/idle", "packed", "idle");
        atlas = new TextureAtlas("packed/idle.atlas");
        bulletAnimation = new Animation<TextureRegion>(0.1f,
                atlas.findRegions("idle"), Animation.PlayMode.LOOP);
        spriteBatch = new SpriteBatch();
        */

        // TEST RENDERING SYSTEM
        Entity player = new Entity();
        TransformComponent transform = new TransformComponent();
        transform.position = new Vector2(300,800);
        player.add(transform);
        AnimatedSpriteComponent animated = new AnimatedSpriteComponent();
        animated.frameDuration = 0.1f;
        animated.height = 200;
        animated.width = 200;
        animated.atlasPath = "packed/idle.atlas";
        animated.region = "idle";
        player.add(animated);
        DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent();
        dynamicBodyComponent.center = transform.position;
        dynamicBodyComponent.height = animated.height;
        dynamicBodyComponent.width = animated.width;
        player.add(dynamicBodyComponent);
        CameraComponent cameraComponent = new CameraComponent();
        player.add(cameraComponent);
        ecs.addEntity(player);

        // Camera follow
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(mainCamera);
        ecs.addSystem(cameraFollowSystem);

        // Rendering
        RenderingSystem renderingSystem = new RenderingSystem();
        ecs.addSystem(renderingSystem);

        // Physics
        PhysicsSystem physics = new PhysicsSystem(world);
        ecs.addSystem(physics);

        //RenderingSystem graphicsSystem = new RenderingSystem();
        mainCamera.viewportWidth = 960;
        mainCamera.viewportHeight = 540;

        //ecs.addSystem(graphicsSystem);

        //populate assetmanager with assets
        assetManager.load("sounds/POL-galactic-trek-short.wav", Music.class);
        assetManager.load("single_gunshot.mp3", Sound.class);
        assetManager.load("menuMusic", Music.class);
        assetManager.load("coin_collect.mp3", Sound.class);
        assetManager.load("jump.mp3", Sound.class);
        assetManager.load("die.mp3", Sound.class);
        assetManager.load("GameOver.mp3", Sound.class);
        assetManager.load("reload.mp3", Sound.class);

        // Create screen and scene for future view state assembly
        GameScene levelOneScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"), mainCamera);
        GameScene menuScene = new GameScene(new TmxMapLoader().load("levels/level1.tmx"), mainCamera);
        
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

        /*create input multiplexer. Input multiplexer serves as the inputProcessor
        for the whole game. When an input event is registered, it passes the input
        event to the first inputprocessor added to it. If that processor returns false
        input multiplexer passes the event on to the next processor that was added
        to it and so forth*/
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inGameScreen.getStage());
        inputMultiplexer.addProcessor(menuScreen.getStage());
        inputMultiplexer.addProcessor(gameOverScreen.getStage());
        inputMultiplexer.addProcessor(highScoreScreen.getStage());
        inputMultiplexer.addProcessor(settingsScreen.getStage());
        inputMultiplexer.addProcessor(tutorialScreen.getStage());

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
        Gdx.input.setInputProcessor(inputMultiplexer);



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
        });
    }

    public void update(float delta)
    {
        // Update ECS
        ecs.update(delta);

        // Update physics
        world.step(1/60f, 6, 2);

        // Test camera move (continuous move to the left)
        //mainCamera.position
        //mainCamera.translate(1,0);
        mainCamera.update();
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

        // TEST ANIMATION
        /*
        stateTime += delta;
        TextureRegion currentFrame = bulletAnimation.getKeyFrame(stateTime);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 20, 20, 200, 200);
        spriteBatch.end();
        */


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
