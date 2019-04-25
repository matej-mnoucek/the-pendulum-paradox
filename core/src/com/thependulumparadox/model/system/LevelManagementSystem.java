package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.model.component.CleanupComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.multiplayer.ISynchronization;
import com.thependulumparadox.state.IStateMachine;
import com.thependulumparadox.view.ViewState;
import com.thependulumparadox.view.screen.BaseScreen;


public class LevelManagementSystem extends EntitySystem
{
    World world;
    ISynchronization proxy;
    IStateMachine viewMachine;
    ViewState gameOver;
    BaseScreen gameOverScreen;
    Entity menuMusic;


    public LevelManagementSystem(World world, ISynchronization proxy, IStateMachine viewMachine, ViewState gameOver, BaseScreen gameOverScreen) {

        super();
        this.world = world;
        this.proxy = proxy;
        this.viewMachine = viewMachine;
        this.gameOver = gameOver;
        this.gameOverScreen = gameOverScreen;


        menuMusic = new Entity();
        menuMusic.add(new MusicComponent(Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"))));
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {

        ImmutableArray<Entity> cleanups = getEngine().getEntitiesFor(Family.all(CleanupComponent.class).get());
        if (cleanups.size() != 0 ) {
            Entity player = cleanups.first().getComponent(CleanupComponent.class).getPlayer();

            getEngine().getEntitiesFor(Family.all(MusicComponent.class).get()).first().getComponent(MusicComponent.class).play = false;

            ImmutableArray EntitiesToDestroy;
            EntitiesToDestroy = getEngine().getEntitiesFor(Family.all(DynamicBodyComponent.class).get());

            for (Object entity : EntitiesToDestroy) {
                if (((Entity) entity) != player) {
                    world.destroyBody(((Entity) entity).getComponent(DynamicBodyComponent.class).body);
                }
            }

            //proxy.submitScore(player.getComponent(PlayerComponent.class).score);

            getEngine().removeAllEntities();
            getEngine().addEntity(menuMusic);
            getEngine().removeSystem(getEngine().getSystem(ControlSystem.class));

            // call on state machine to change state
            Gdx.input.setInputProcessor(gameOverScreen.getStage());
            viewMachine.nextState(gameOver);
        }
        super.update(deltaTime);
    }
}
