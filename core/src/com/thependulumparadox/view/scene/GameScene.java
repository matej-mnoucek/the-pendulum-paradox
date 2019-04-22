package com.thependulumparadox.view.scene;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.control.AIControlModule;
import com.thependulumparadox.control.ControlModule;
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.state.TaggedState;

import org.lwjgl.Sys;

import java.util.ArrayList;

public class GameScene extends Scene
{
    private MapRenderer renderer;
    //private PooledEngine entityPool = new PooledEngine();
    public GameScene(TiledMap level, World world, OrthographicCamera camera, Engine engine)
    {
        super(camera);
        //ArrayList<Entity> tiledObjects = new ArrayList<>();

        // Create renderer
        renderer = new OrthogonalTiledMapRenderer(level, 1 / Constants.PPM);
        renderer.setView(camera);

        // Preprocess the level == add physics
        MapLayer layer = level.getLayers().get("CollisionLayer");
        for (MapObject object : layer.getObjects())
        {
            RectangleMapObject rectangle = (RectangleMapObject) object;

            // Creating physics body representation
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.x = (rectangle.getRectangle().x + rectangle.getRectangle().width/2)
                    / Constants.PPM;
            bodyDef.position.y = (rectangle.getRectangle().y + rectangle.getRectangle().height/2)
                    / Constants.PPM;

            // Add it to the world
            Body body = world.createBody(bodyDef);

            // Create a box (polygon) shape
            PolygonShape polygon = new PolygonShape();

            // Set the polygon shape as a box
            polygon.setAsBox((rectangle.getRectangle().width/2) / Constants.PPM,
                    (rectangle.getRectangle().height/2) / Constants.PPM);

            // Create a fixture definition to apply the shape to it
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygon;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 1.0f;
            fixtureDef.restitution = 0.0f;

            // Create a fixture from the box and add it to the body
            body.createFixture(fixtureDef);
        }

        layer = level.getLayers().get("EnemySpawnLayer");
        for (MapObject object : layer.getObjects()) {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    case "start":
                        //Create player character(s)
                        break;
                    case "goal":
                        //Create Goal
                        break;
                    case "enemy_attacking": {

                        Entity entity;
                        entity = new Entity();
                        entity.flags = 4;
                        TransformComponent transform = new TransformComponent();
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        transform.position = new Vector2(rect.x/ Constants.PPM,rect.y/ Constants.PPM);
                        entity.add(transform);
                        AnimatedSpriteComponent animatedEnemy = new AnimatedSpriteComponent("packed/knight_enemy.atlas");
                        animatedEnemy.frameDuration(0.07f);
                        animatedEnemy.height = 1.8f;
                        animatedEnemy.width = 1.8f;
                        animatedEnemy.currentAnimation = "attackLeft";
                        entity.add(animatedEnemy);
                        DynamicBodyComponent dynamicBody = new DynamicBodyComponent(world);
                        dynamicBody.position(transform.position)
                                .dimension(0.7f, 1.5f)
                                //.trigger(2.0f)
                                .activate(true);
                        entity.add(dynamicBody);
                        EnemyComponent enemyComponent = new EnemyComponent();
                        entity.add(enemyComponent);
                        InteractionComponent interaction = new InteractionComponent();
                        entity.add(interaction);
                        engine.addEntity(entity);
                    }
                        break;
                    case "enemy_walking": {

                        Entity entity;
                        entity = new Entity();
                        entity.flags = 4;
                        TransformComponent transform = new TransformComponent();
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        transform.position = new Vector2(rect.x/ Constants.PPM,rect.y/ Constants.PPM);
                        entity.add(transform);
                        AnimatedSpriteComponent animatedEnemy = new AnimatedSpriteComponent("packed/hero_player.atlas");
                        animatedEnemy.frameDuration(0.07f);
                        animatedEnemy.height = 1.8f;
                        animatedEnemy.width = 1.8f;
                        entity.add(animatedEnemy);
                        DynamicBodyComponent dynamicBody = new DynamicBodyComponent(world);
                        dynamicBody.position(transform.position)
                                .dimension(0.7f, 1.5f)
                                .properties(0, 50f, 10f, 0f)
                                //.trigger(2.0f)
                                .activate(true);
                        entity.add(dynamicBody);
                        EnemyComponent enemyComponent = new EnemyComponent();
                        entity.add(enemyComponent);
                        InteractionComponent interaction = new InteractionComponent();
                        entity.add(interaction);
                        ControlModule module = new AIControlModule();
                        StateComponent state = new StateComponent();
                        state.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                                .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                                .add(new TaggedState("jumpRight")).add(new TaggedState("jumpLeft"))
                                .add(new TaggedState("shootRight")).add(new TaggedState("shootLeft"))
                                .initial("idleRight");
                        entity.add(state);
                        ControlComponent control = new ControlComponent(module);
                        entity.add(control);
                        engine.addEntity(entity);
                    }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        // Set view and render scene
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        super.dispose();
    }
}
