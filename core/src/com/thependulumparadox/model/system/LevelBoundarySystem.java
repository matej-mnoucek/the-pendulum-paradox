package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.view.scene.GameScene;

/**
 * System that evaluates players position in a level and prevents him from leaving its bounds
 * (e.g. by falling through water) and also detects that he has already arrived to the end point
 */
public class LevelBoundarySystem extends EntitySystem
{
    private final float DEATH_BOUND = -0.3f;
    private final float CLOSE_THRESHOLD = 0.3f;

    private Entity mainPlayer;

    private ImmutableArray<Entity> playerEntities;
    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<PlayerComponent> playerComponentMapper
            = ComponentMapper.getFor(PlayerComponent.class);

    public Event<EventArgs> levelEndReached = new Event<>();
    public Event<EventArgs> playerOutOfBounds = new Event<>();

    // Is active
    public boolean checkBoundaries = true;

    // Level endpoint to check
    public Vector2 levelEndPoint = new Vector2(0,0);


    @Override
    public void addedToEngine(Engine engine)
    {
        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                TransformComponent.class).get());

        for (Entity entity : playerEntities)
        {
            PlayerComponent player = playerComponentMapper.get(entity);

            // The first player
            if (player.id == 1)
            {
                mainPlayer = entity;
                break;
            }
        }
    }

    @Override
    public void update(float deltaTime)
    {
        if (playerEntities.size() > 0 && checkBoundaries)
        {
            // Detect if main player reached level end
            DynamicBodyComponent body = dynamicBodyComponentMapper.get(mainPlayer);

            if (body.body.getPosition().dst(levelEndPoint) < CLOSE_THRESHOLD)
            {
                levelEndReached.invoke(null);
            }


            // Detect if main player is out of level bounds
            Vector2 bottomBound = new Vector2(0, DEATH_BOUND);
            DynamicBodyComponent body2 = dynamicBodyComponentMapper.get(mainPlayer);

            if (body2.body.getPosition().y < bottomBound.y)
            {
                playerOutOfBounds.invoke(null);
            }
        }
    }
}
