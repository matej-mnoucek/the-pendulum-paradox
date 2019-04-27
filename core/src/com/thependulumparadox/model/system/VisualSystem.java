package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.misc.Pair;
import com.thependulumparadox.misc.Range;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.BulletVisualsComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.EnhancementVisualsComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;

/**
 * System that renders enhancement auras and different bullets based on player stats
 * @see BulletVisualsComponent
 * @see EnhancementVisualsComponent
 */
public class VisualSystem extends EntitySystem
{
    // Cached entities
    private ImmutableArray<Entity> playerBulletVisualEntities;
    private ImmutableArray<Entity> enemyBulletVisualEntities;
    private ImmutableArray<Entity> enhancementVisualEntities;

    private ComponentMapper<PlayerComponent> playerComponentMapper
            = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<EnemyComponent> enemyComponentMapper
            = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<SpriteComponent> spriteComponentMapper
            = ComponentMapper.getFor(SpriteComponent.class);

    private ComponentMapper<BulletVisualsComponent> bulletVisualsComponentMapper
            = ComponentMapper.getFor(BulletVisualsComponent.class);
    private ComponentMapper<EnhancementVisualsComponent> enhancementVisualsComponentMapper
            = ComponentMapper.getFor(EnhancementVisualsComponent.class);


    public void addedToEngine(Engine engine)
    {
        playerBulletVisualEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                BulletVisualsComponent.class).get());
        enemyBulletVisualEntities = engine.getEntitiesFor(Family.all(EnemyComponent.class,
                BulletVisualsComponent.class).get());
        enhancementVisualEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                EnhancementVisualsComponent.class, SpriteComponent.class).get());
    }

    public void update(float deltaTime)
    {
        // Process visuals
        for (int i = 0; i < playerBulletVisualEntities.size(); i++)
        {
            Entity entity = playerBulletVisualEntities.get(i);
            BulletVisualsComponent component = bulletVisualsComponentMapper.get(entity);
            PlayerComponent player = playerComponentMapper.get(entity);

            // Search for the appropriate sprite
            // By default fallback to default sprite
            component.currentSprite = component.defaultSprite;

            // If yes then search for corresponding one based on ranges
            for (int j = 0; j < component.bulletSprites.size(); j++)
            {
                Pair<Range<Float>, Sprite> pair = component.bulletSprites.get(j);

                if (pair.key.contains(player.current.damage))
                {
                    component.currentSprite = pair.value;
                    break;
                }
            }
        }

        for (int i = 0; i < enemyBulletVisualEntities.size(); i++)
        {
            Entity entity = enemyBulletVisualEntities.get(i);
            BulletVisualsComponent component = bulletVisualsComponentMapper.get(entity);
            EnemyComponent enemy = enemyComponentMapper.get(entity);

            // Search for the appropriate sprite
            // By default fallback to default sprite
            component.currentSprite = component.defaultSprite;

            // If yes then search for corresponding one based on ranges
            for (int j = 0; j < component.bulletSprites.size(); j++)
            {
                Pair<Range<Float>, Sprite> pair = component.bulletSprites.get(j);

                if (pair.key.contains(enemy.current.damage))
                {
                    component.currentSprite = pair.value;
                    break;
                }
            }
        }

        for (int i = 0; i < enhancementVisualEntities.size(); i++)
        {
            Entity entity = enhancementVisualEntities.get(i);
            EnhancementVisualsComponent component = enhancementVisualsComponentMapper.get(entity);
            PlayerComponent player = playerComponentMapper.get(entity);
            SpriteComponent sprite = spriteComponentMapper.get(entity);

            // Search for the appropriate sprite
            // By default fallback to no sprite
            sprite.sprite = null;

            // If yes then search for corresponding one based on ranges
            for (int j = 0; j < component.defenseSprites.size(); j++)
            {
                Pair<Range<Float>, Sprite> pair = component.defenseSprites.get(j);

                if (pair.key.contains(player.current.defense))
                {
                    sprite.sprite = pair.value;
                    break;
                }
            }
        }
    }
}