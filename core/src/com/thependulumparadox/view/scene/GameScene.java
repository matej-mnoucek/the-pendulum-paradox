package com.thependulumparadox.view.scene;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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

import com.thependulumparadox.control.AIJumpAttackControlModule;
import com.thependulumparadox.control.AIWanderAttackControlModule;
import com.thependulumparadox.control.ControlModule;
import com.thependulumparadox.misc.Constants;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.EntityFactory;
import com.thependulumparadox.state.TaggedState;

public class GameScene extends Scene
{
    private MapRenderer renderer;
    private EntityFactory entityFactory;

    public GameScene(TiledMap level, OrthographicCamera camera, World world, Engine engine)
    {
        super(camera);

        // Create renderer
        renderer = new OrthogonalTiledMapRenderer(level, 1 / Constants.PPM);
        renderer.setView(camera);

        // Entity factory
        entityFactory = new EntityFactory(world);


        // Preprocess the level == add physics
        MapLayer layer = level.getLayers().get("CollisionLayer");
        for (MapObject object : layer.getObjects())
        {
            RectangleMapObject rectangle = (RectangleMapObject) object;

            // Creating physics body representation
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.x = (rectangle.getRectangle().x + rectangle.getRectangle().width/2.0f)
                    / Constants.PPM;
            bodyDef.position.y = (rectangle.getRectangle().y + rectangle.getRectangle().height/2.0f)
                    / Constants.PPM;

            // Add it to the world
            Body body = world.createBody(bodyDef);

            // Create a box (polygon) shape
            PolygonShape polygon = new PolygonShape();

            // Set the polygon shape as a box
            polygon.setAsBox((rectangle.getRectangle().width/2.0f) / Constants.PPM,
                    (rectangle.getRectangle().height/2.0f) / Constants.PPM);

            // Create a fixture definition to apply the shape to it
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygon;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 1.0f;
            fixtureDef.restitution = 0.0f;

            // Create a fixture from the box and add it to the body
            body.createFixture(fixtureDef);
        }

        // Create control points
        layer = level.getLayers().get("points");
        for (MapObject object : layer.getObjects())
        {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    // Player spawn point
                    case "start":
                        break;

                    // Level goal
                    case "goal":
                        break;
                }
            }
        }

        // Create collectables
        layer = level.getLayers().get("pickups");
        for (MapObject object : layer.getObjects())
        {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    case "coin":
                        Rectangle rectangle =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position = new Vector2(rectangle.x/ Constants.PPM,
                                rectangle.y/ Constants.PPM);
                        Entity ent = entityFactory.create("10_coin");
                        ent.getComponent(DynamicBodyComponent.class).position(position);
                        engine.addEntity(ent);
                        break;
                    case "ammo":
                        break;
                }
            }
        }

        // Create enemies
        /*
        layer = level.getLayers().get("enemies");
        for (MapObject object : layer.getObjects())
        {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    case "attacking":
                        Rectangle rectangle1 =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position1 = new Vector2(rectangle1.x/ Constants.PPM,
                                rectangle1.y/ Constants.PPM);
                        Entity entity1 = entityFactory.create("knight_enemy");
                        entity1.getComponent(DynamicBodyComponent.class).position(position1);
                        engine.addEntity(entity1);
                        break;

                    case "walking":
                        Rectangle rectangle2 =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position2 = new Vector2(rectangle2.x/ Constants.PPM,
                                rectangle2.y/ Constants.PPM);
                        Entity entity2 = entityFactory.create("ninja_enemy");
                        entity2.getComponent(DynamicBodyComponent.class).position(position2);
                        engine.addEntity(entity2);
                        break;

                    default:
                        break;
                }
            }
        }
        */
    }

    public void repopulate(TiledMap level, World world, Engine engine){


        MapLayer layer = level.getLayers().get("pickups");
        for (MapObject object : layer.getObjects()) {
            if (object.getName() != null) {
                switch (object.getName()) {
                    case "coin":
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position = new Vector2(rect.x/Constants.PPM,rect.y/Constants.PPM);
                        Entity ent = entityFactory.create("10_coin");
                        ent.getComponent(DynamicBodyComponent.class).position(position);
                        engine.addEntity(ent);
                        break;
                    case "ammo":
                        break;
                }
            }
        }

        layer = level.getLayers().get("enemies");
        for (MapObject object : layer.getObjects()) {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    case "attacking": {
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position = new Vector2(rect.x/ Constants.PPM,rect.y/ Constants.PPM);
                        Entity ent = entityFactory.create("knight_enemy");
                        ent.getComponent(DynamicBodyComponent.class).position(position);
                        engine.addEntity(ent);
                    }
                    break;
                    case "walking": {
                        Rectangle rect =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position = new Vector2(rect.x/ Constants.PPM,rect.y/ Constants.PPM);
                        Entity ent = entityFactory.create("ninja_enemy");
                        ent.getComponent(DynamicBodyComponent.class).position(position);
                        engine.addEntity(ent);                    }
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

    }
}
