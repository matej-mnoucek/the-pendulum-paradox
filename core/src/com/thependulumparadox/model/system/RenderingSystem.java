package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.TransformComponent;

/**
 * This system is responsible for drawing all entity sprites
 * (even the ones controlled by animations)
 * @see SpriteComponent
 * @see AnimatedSpriteComponent
 */
public class RenderingSystem extends EntitySystem
{
    private ImmutableArray<Entity> spriteEntities;
    private ImmutableArray<Entity> animatedEntities;

    private ComponentMapper<TransformComponent> transformComponentMapper
            = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<SpriteComponent> spriteComponentMapper
            = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<AnimatedSpriteComponent> animatedSpriteComponentMapper
            = ComponentMapper.getFor(AnimatedSpriteComponent.class);

    private SpriteBatch spriteBatch = new SpriteBatch();
    private float stateTime = 0.0f;

    private OrthographicCamera camera;

    public RenderingSystem(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine)
    {

        spriteEntities = engine.getEntitiesFor(Family.all(TransformComponent.class,
                SpriteComponent.class).get());
        animatedEntities = engine.getEntitiesFor(Family.all(TransformComponent.class,
                AnimatedSpriteComponent.class).get());
    }

    @Override
    public void update(float delta)
    {
        // Update state time
        stateTime += delta;

        // Render static sprites at first
        spriteBatch.begin();
        for (int i = 0; i < spriteEntities.size(); i++)
        {
            Entity entity = spriteEntities.get(i);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            SpriteComponent spriteComponent
                    = spriteComponentMapper.get(entity);

            // Handle rotation
            spriteComponent.rotationAngle += spriteComponent.rotationSpeed;

            // Draw
            if(spriteComponent.sprite != null)
            {
                spriteBatch.draw(spriteComponent.sprite,
                        transformComponent.position.x - spriteComponent.width / 2.0f,
                        transformComponent.position.y - spriteComponent.height / 2.0f,
                        spriteComponent.width / 2.0f,
                        spriteComponent.height / 2.0f,
                        spriteComponent.width,
                        spriteComponent.height,
                        1.0f, 1.0f, spriteComponent.rotationAngle);
            }
        }

        // Render animations
        for (int i = 0; i < animatedEntities.size(); i++)
        {
            Entity entity = animatedEntities.get(i);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            AnimatedSpriteComponent animatedSpriteComponent
                    = animatedSpriteComponentMapper.get(entity);

            TextureRegion textureRegion = animatedSpriteComponent.animations
                    .get(animatedSpriteComponent.currentAnimation).getKeyFrame(stateTime);
            spriteBatch.setProjectionMatrix(camera.combined);

            spriteBatch.draw(textureRegion,
                    transformComponent.position.x - animatedSpriteComponent.width/2.0f,
                    transformComponent.position.y - animatedSpriteComponent.height/2.0f,
                    animatedSpriteComponent.width,
                    animatedSpriteComponent.height);
        }
        spriteBatch.end();
    }
}
