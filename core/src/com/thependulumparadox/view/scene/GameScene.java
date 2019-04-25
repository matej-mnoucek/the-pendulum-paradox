package com.thependulumparadox.view.scene;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
import com.thependulumparadox.model.component.LevelObjectComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.entity.EntityFactory;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends Scene
{
    private World world;
    private Engine engine;
    private TiledMap level;
    private MapRenderer renderer;
    private EntityFactory entityFactory;

    private Vector2 startPoint = new Vector2(0,0);
    private Vector2 endPoint = new Vector2(0,0);

    private List<Body> staticBodies = new ArrayList<>();

    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);

    public GameScene(TiledMap level, OrthographicCamera camera, World world, Engine engine)
    {
        super(camera);

        // Level, engine and world
        this.level = level;
        this.world = world;
        this.engine = engine;


        // Create renderer
        renderer = new OrthogonalTiledMapRenderer(level, 1 / Constants.PPM);
        renderer.setView(camera);

        // Entity factory
        entityFactory = new EntityFactory(world);


        // Extract control points
        MapLayer layer = level.getLayers().get("points");
        for (MapObject object : layer.getObjects())
        {
            if (object.getName() != null)
            {
                switch (object.getName())
                {
                    // Player spawn point
                    case "start":
                        Rectangle rectangleStart = ((RectangleMapObject)object).getRectangle();
                        startPoint = new Vector2(rectangleStart.x / Constants.PPM,
                                rectangleStart.y / Constants.PPM);
                        break;

                    // Level goal
                    case "goal":
                        Rectangle rectangleEnd = ((RectangleMapObject)object).getRectangle();
                        endPoint = new Vector2(rectangleEnd.x / Constants.PPM,
                                rectangleEnd.y / Constants.PPM);
                        break;
                }
            }
        }
    }

    public void createCollisionZones()
    {
        // Create static bodies
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

            // Cache the body
            staticBodies.add(body);
        }
    }

    public void destroyCollisionZones()
    {
        for (Body body : staticBodies)
        {
            world.destroyBody(body);
        }
        staticBodies.clear();
    }

    public void createEntities()
    {
        // Create collectables
        MapLayer layer = level.getLayers().get("pickups");
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
                        Entity entity = entityFactory.create("10_coin");
                        entity.getComponent(DynamicBodyComponent.class).position(position);

                        LevelObjectComponent object1 = new LevelObjectComponent();
                        entity.add(object1);

                        engine.addEntity(entity);
                        break;
                    case "ammo":
                        break;
                }
            }
        }

        // Create enemies
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

                        LevelObjectComponent object1 = new LevelObjectComponent();
                        entity1.add(object1);

                        engine.addEntity(entity1);
                        break;

                    case "walking":
                        Rectangle rectangle2 =  ((RectangleMapObject)object).getRectangle();
                        Vector2 position2 = new Vector2(rectangle2.x/ Constants.PPM,
                                rectangle2.y/ Constants.PPM);
                        Entity entity2 = entityFactory.create("ninja_enemy");
                        entity2.getComponent(DynamicBodyComponent.class).position(position2);

                        LevelObjectComponent object2 = new LevelObjectComponent();
                        entity2.add(object2);

                        engine.addEntity(entity2);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    public void destroyEntities()
    {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(LevelObjectComponent.class).get());
        for (Entity entity : entities)
        {
            DynamicBodyComponent body = dynamicBodyComponentMapper.get(entity);
            world.destroyBody(body.body);
            engine.removeEntity(entity);
        }
    }

    @Override
    public void render(float delta)
    {
        // Set view and render scene
        renderer.setView(camera);
        renderer.render();
    }

    public void populate()
    {
        // Populate the level
        createCollisionZones();
        createEntities();
    }

    @Override
    public void dispose()
    {
        // Clear everything
        destroyEntities();
        destroyCollisionZones();
    }

    public Vector2 getStartPoint()
    {
        return startPoint;
    }

    public Vector2 getEndPoint()
    {
        return endPoint;
    }
}
