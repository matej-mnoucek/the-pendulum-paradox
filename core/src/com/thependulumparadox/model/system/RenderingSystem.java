package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.TransformComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * All the logic for graphics components
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

    private List<Texture> sprites = new ArrayList<>();
    private List<Entity> cachedSpriteEntities = new ArrayList<>();
    private List<Animation<TextureRegion>> animations = new ArrayList<>();
    private List<Entity> cachedAnimatedEntities = new ArrayList<>();

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

        // Preload sprites
        for (int i = 0; i < spriteEntities.size(); i++)
        {
            Entity entity = spriteEntities.get(i);
            sprites.add(tearUpSprite(entity));

            // Cache corresponding entity
            cachedSpriteEntities.add(entity);
        }

        // Preload animations
        for (int i = 0; i < animatedEntities.size(); i++)
        {
            Entity entity = animatedEntities.get(i);
            animations.add(tearUpAnimation(entity));

            // Cache corresponding entity
            cachedAnimatedEntities.add(entity);
        }
    }

    @Override
    public void update(float delta)
    {
        // Update state time
        stateTime += delta;

        // Check if some sprites were added to the engine
        if (spriteEntities.size() != cachedSpriteEntities.size())
        {
            // If new entity is present
            for (int i = 0; i < spriteEntities.size(); i++)
            {
                // Get entity
                Entity entity = spriteEntities.get(i);

                if (!cachedSpriteEntities.contains(entity))
                {
                    sprites.add(tearUpSprite(entity));
                    cachedSpriteEntities.add(entity);
                }
            }

            // If an entity was removed
            for (int i = 0; i < cachedSpriteEntities.size(); i++)
            {
                // Get entity
                Entity entity = cachedSpriteEntities.get(i);

                if (!spriteEntities.contains(entity, true))
                {
                    int index = cachedSpriteEntities.indexOf(entity);
                    cachedSpriteEntities.remove(entity);
                    sprites.remove(index);
                }
            }
        }

        // Check if some animations were added to the engine
        if (animatedEntities.size() != cachedAnimatedEntities.size())
        {
            // If new entity was added
            for (int i = 0; i < animatedEntities.size(); i++)
            {
                // Get entity
                Entity entity = animatedEntities.get(i);

                if (!cachedAnimatedEntities.contains(entity))
                {
                    animations.add(tearUpAnimation(entity));
                    cachedAnimatedEntities.add(entity);
                }
            }

            // If an entity was removed
            for (int i = 0; i < cachedAnimatedEntities.size(); i++)
            {
                // Get entity
                Entity entity = cachedAnimatedEntities.get(i);

                if (!animatedEntities.contains(entity, true))
                {
                    int index = cachedAnimatedEntities.indexOf(entity);
                    cachedAnimatedEntities.remove(entity);
                    animations.remove(index);
                }
            }
        }

        // Render static sprites at first
        spriteBatch.begin();
        for (int i = 0; i < spriteEntities.size(); i++)
        {
            Entity entity = spriteEntities.get(i);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            SpriteComponent spriteComponent
                    = spriteComponentMapper.get(entity);

            Texture texture = sprites.get(i);
            spriteBatch.draw(texture,
                    transformComponent.position.x - spriteComponent.width/2.0f,
                    transformComponent.position.y - spriteComponent.height/2.0f,
                    spriteComponent.width,
                    spriteComponent.height);
        }

        // Render animations
        for (int i = 0; i < animatedEntities.size(); i++)
        {
            Entity entity = animatedEntities.get(i);
            TransformComponent transformComponent = transformComponentMapper.get(entity);
            AnimatedSpriteComponent animatedSpriteComponent
                    = animatedSpriteComponentMapper.get(entity);

            TextureRegion currentFrame = animations.get(i).getKeyFrame(stateTime);
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.draw(currentFrame,
                    transformComponent.position.x - animatedSpriteComponent.width/2.0f,
                    transformComponent.position.y - animatedSpriteComponent.height/2.0f,
                    animatedSpriteComponent.width,
                    animatedSpriteComponent.height);
        }
        spriteBatch.end();
    }

    private Texture tearUpSprite(Entity entity)
    {
        SpriteComponent spriteComponent = spriteComponentMapper.get(entity);
        return new Texture(spriteComponent.spritePath);
    }

    private Animation<TextureRegion> tearUpAnimation(Entity entity)
    {
        AnimatedSpriteComponent animatedSpriteComponent
                = animatedSpriteComponentMapper.get(entity);

        TextureAtlas atlas = new TextureAtlas(animatedSpriteComponent.atlasPath);
        Animation<TextureRegion> animation = new Animation<TextureRegion>(
                animatedSpriteComponent.frameDuration,
                atlas.findRegions(animatedSpriteComponent.region),
                Animation.PlayMode.LOOP);



        return animation;
    }
}
