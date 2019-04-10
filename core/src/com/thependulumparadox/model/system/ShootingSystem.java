package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.TransformComponent;

public class ShootingSystem extends EntitySystem
{
    private PooledEngine bulletPool = new PooledEngine();

    private Entity playerEntity;
    private TransformComponent transformComponent;
    private PlayerComponent playerComponent;

    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<PlayerComponent> playerComponentMapper
            = ComponentMapper.getFor(PlayerComponent.class);

    private String bulletSpritePath;
    private Engine engine;

    public ShootingSystem(String bulletSpritePath)
    {
        this.bulletSpritePath = bulletSpritePath;
    }

    public void addedToEngine(Engine engine)
    {
        // Save engine reference
        this.engine = engine;

        // Get entity and corresponding components
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                TransformComponent.class).get()).first();

        transformComponent = transformComponentMapper.get(playerEntity);
        playerComponent = playerComponentMapper.get(playerEntity);
    }

    public void update(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            // Create new bullet
            Entity bullet = bulletPool.createEntity();
            bullet.flags = 0x0008;
            TransformComponent transform = new TransformComponent();
            transform.position = transformComponent.position;
            SpriteComponent sprite = new SpriteComponent();
            sprite.spritePath = bulletSpritePath;
            sprite.height = 0.3f;
            sprite.width = 0.3f;
            BulletComponent bulletComponent = new BulletComponent();
            bulletComponent.damage = playerComponent.damage;
            DynamicBodyComponent dynamic = new DynamicBodyComponent();
            dynamic.center = transformComponent.position;
            dynamic.height = sprite.height;
            dynamic.width = sprite.width;
            dynamic.impulseHorizontal = 5f;
            dynamic.gravityScale = 0.0f;

            // Add all components
            bullet.add(transform);
            bullet.add(sprite);
            bullet.add(bulletComponent);
            bullet.add(dynamic);

            // Add to engine
            engine.addEntity(bullet);

            // Log
            System.out.println("SHOOT!");
        }
    }
}