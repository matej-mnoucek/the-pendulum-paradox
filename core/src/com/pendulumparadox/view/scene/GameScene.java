package com.pendulumparadox.view.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pendulumparadox.Constants;
import com.pendulumparadox.TiledHandler.OrthogonalTiledMapObjectHandler;
import com.pendulumparadox.presenter.GamePresenter;

public class GameScene extends Scene
{
    private MapRenderer renderer;

    public GameScene(TiledMap level, World world, OrthographicCamera camera)
    {
        super(camera);

        // Create renderer
        renderer = new OrthogonalTiledMapRenderer(level, 1 / Constants.PPM);
        renderer.setView(camera);

        // Preprocess the level == add physics
        MapLayer layer = level.getLayers().get("collision_layer");
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
