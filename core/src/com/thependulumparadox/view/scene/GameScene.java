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
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.TransformComponent;

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
                Entity newEntity;
                switch (object.getName())
                {
                    case "start":
                        //Create player character(s)
                        break;
                    case "goal":
                        //Create Goal
                        break;
                    case "enemy_attacking":

                        // ENEMY ENTITY
                        newEntity = new Entity(); //entityPool.createEntity();
                        newEntity.flags = 4;
                        TransformComponent transform = new TransformComponent();
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        transform.position = new Vector2(rect.x,rect.y);
                        newEntity.add(transform);
                        AnimatedSpriteComponent animatedEnemy = new AnimatedSpriteComponent("packed/ninja_enemy.atlas");
                        animatedEnemy.frameDuration(0.07f);
                        animatedEnemy.height = 1.8f;
                        animatedEnemy.width = 1.8f;
                        animatedEnemy.currentAnimation = "attack";
                        newEntity.add(animatedEnemy);
                        DynamicBodyComponent dynamicBody = new DynamicBodyComponent(world);
                        dynamicBody.position(transform.position)
                                .dimension(0.7f, 1.5f).activate(true);
                        newEntity.add(dynamicBody);
                        EnemyComponent enemyComponent1 = new EnemyComponent();
                        newEntity.add(enemyComponent1);
                        InteractionComponent interaction1 = new InteractionComponent();
                        newEntity.add(interaction1);
                        engine.addEntity(newEntity);
                        break;
                    case "enemy_walking":

                        newEntity = new Entity();// nentityPool.createEntity();
                        newEntity.flags = 4;
                        TransformComponent transform2 = new TransformComponent();

                        Rectangle rect2 =  ((RectangleMapObject)object).getRectangle();
                        transform2.position = new Vector2(rect2.x/ Constants.PPM,rect2.y/ Constants.PPM);
                        newEntity.add(transform2);
                        System.out.print(transform2.position);
                        AnimatedSpriteComponent animatedEnemy2 = new AnimatedSpriteComponent("packed/knight_enemy.atlas");
                        animatedEnemy2.frameDuration(0.07f);
                        animatedEnemy2.height = 1.8f;
                        animatedEnemy2.width = 1.8f;
                        animatedEnemy2.currentAnimation = "attack";
                        newEntity.add(animatedEnemy2);
                        DynamicBodyComponent dynamicBody2 = new DynamicBodyComponent(world);
                        dynamicBody2.position(transform2.position)
                            .dimension(0.7f, 1.5f).activate(true);
                        newEntity.add(dynamicBody2);
                        EnemyComponent enemyComponent2 = new EnemyComponent();
                        newEntity.add(enemyComponent2);
                        InteractionComponent interaction2 = new InteractionComponent();
                        newEntity.add(interaction2);
                        engine.addEntity(newEntity);
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
