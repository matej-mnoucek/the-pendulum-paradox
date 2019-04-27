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
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.entity.EntityFactory;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

/**
 * A scene or map loaded from external Tiled level editor
 * The class also contains logic for creating entities and populating the level
 * @see Scene
 */
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
    private ComponentMapper<StaticBodyComponent> staticBodyComponentMapper
            = ComponentMapper.getFor(StaticBodyComponent.class);

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
            if (rectangle != null)
            {
                // Creating physics body representation
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.x = (rectangle.getRectangle().x + rectangle.getRectangle().width / 2.0f)
                        / Constants.PPM;
                bodyDef.position.y = (rectangle.getRectangle().y + rectangle.getRectangle().height / 2.0f)
                        / Constants.PPM;

                // Add it to the world
                Body body = world.createBody(bodyDef);

                // Create a box (polygon) shape
                PolygonShape polygon = new PolygonShape();

                // Set the polygon shape as a box
                polygon.setAsBox((rectangle.getRectangle().width / 2.0f) / Constants.PPM,
                        (rectangle.getRectangle().height / 2.0f) / Constants.PPM);

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

                // Dispose shape
                polygon.dispose();
            }
        }
    }

    public void destroyCollisionZones()
    {
        for (int i = 0; i < staticBodies.size(); i++)
        {
            Body body = staticBodies.get(i);
            body.setAwake(false);
            body.setActive(false);
            world.destroyBody(body);
            staticBodies.set(i, null);
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
                Rectangle rectangle =  ((RectangleMapObject)object).getRectangle();
                Vector2 position = new Vector2(rectangle.x/ Constants.PPM,
                                               rectangle.y/ Constants.PPM);
                Entity entity = entityFactory.create(object.getName());
                if (entity == null)
                {
                    System.out.print("\nInvalid Object name: " + object.getName());
                    continue;
                }
                entity.getComponent(DynamicBodyComponent.class).position(position);
                LevelObjectComponent objectComponent = new LevelObjectComponent();
                entity.add(objectComponent);

                engine.addEntity(entity);
            }
        }

        // Create enemies
        layer = level.getLayers().get("enemies");
        for (MapObject object : layer.getObjects())
        {
            if (object.getName() != null)
            {
                Rectangle rectangle =  ((RectangleMapObject)object).getRectangle();
                Vector2 position = new Vector2(rectangle.x/ Constants.PPM,
                                                rectangle.y/ Constants.PPM);
                Entity enemy = entityFactory.create(object.getName());
                if (enemy == null)
                {
                    System.out.print("\nInvalid Enemy name: " + object.getName());
                    continue;
                }
                enemy.getComponent(DynamicBodyComponent.class).position(position);
                LevelObjectComponent objectComponent = new LevelObjectComponent();
                enemy.add(objectComponent);

                engine.addEntity(enemy);
            }
        }
    }

    public void destroyEntities()
    {
        Entity[] entities = engine.getEntitiesFor(Family.all(LevelObjectComponent.class).get())
                .toArray(Entity.class);

        for (Entity entity : entities)
        {
            DynamicBodyComponent dynamicBody = dynamicBodyComponentMapper.get(entity);
            if (dynamicBody != null)
            {
                dynamicBody.body.setAwake(false);
                dynamicBody.body.setActive(false);
                world.destroyBody(dynamicBody.body);
                dynamicBody.body = null;
            }

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

    @Override
    public String toString() {

        String name = "untitled";
        if (level.getProperties().containsKey("name"))
        {
            name = (String) level.getProperties().get("name");
        }

        return "GameScene{" +
                "level=" + name +
                '}';
    }
}
