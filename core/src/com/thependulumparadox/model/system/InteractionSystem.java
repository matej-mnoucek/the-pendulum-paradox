package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.misc.StandardAttributes;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.CoinComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.EnhancementComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SoundComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.component.enhancement.Enhancement;
import com.thependulumparadox.observer.Event;
import com.thependulumparadox.observer.EventArgs;
import com.thependulumparadox.observer.ValueEventArgs;

/**
 * System that handles interaction between entities in a level
 * e.g. attacks and receiving damage, picking up enhancements, coins...
 * @see EnemyComponent
 * @see PlayerComponent
 * @see BulletComponent
 * @see CoinComponent
 */
public class InteractionSystem extends EntitySystem
{
    private ImmutableArray<Entity> playerEntities;
    private ImmutableArray<Entity> enemyEntities;

    private ComponentMapper<DynamicBodyComponent> dynamicBodyComponentMapper
            = ComponentMapper.getFor(DynamicBodyComponent.class);
    private ComponentMapper<StaticBodyComponent> staticBodyComponentMapper
            = ComponentMapper.getFor(StaticBodyComponent.class);
    private ComponentMapper<InteractionComponent> interactionComponentMapper
            = ComponentMapper.getFor(InteractionComponent.class);
    private ComponentMapper<BulletComponent> bulletComponentMapper
            = ComponentMapper.getFor(BulletComponent.class);
    private ComponentMapper<CoinComponent> coinComponentMapper
            = ComponentMapper.getFor(CoinComponent.class);
    private ComponentMapper<EnhancementComponent> enhancementComponentMapper
            = ComponentMapper.getFor(EnhancementComponent.class);
    private ComponentMapper<EnemyComponent> enemyComponentMapper
            = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<PlayerComponent> playerComponentMapper
            = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<SoundComponent> soundComponentMapper
            = ComponentMapper.getFor(SoundComponent.class);

    // Enhancement root
    private Enhancement enhancementChain = null;

    // Physics world
    private World world;

    // Events
    public final Event<ValueEventArgs<PlayerComponent>> playerUpdate = new Event<>();
    public final Event<EventArgs> playerDeath = new Event<>();


    public InteractionSystem(World world)
    {
        this.world = world;

        // Define collision handling
        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                // Handle collisions between dynamic bodies
                if(a.getUserData() instanceof Entity && b.getUserData() instanceof Entity)
                {
                    Entity entityA = (Entity) a.getUserData();
                    Entity entityB = (Entity) b.getUserData();

                    // Player and coin
                    if(entityA.flags == 2 && entityB.flags == 32)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityA);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityB);

                        }
                    }

                    if(entityB.flags == 2 && entityA.flags == 32)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityB);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityA);
                        }
                    }

                    // Player and enhancement
                    if(entityA.flags == 2 && entityB.flags == 16)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityA);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityB);
                        }
                    }

                    if(entityB.flags == 2 && entityA.flags == 16)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityB);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityA);
                        }
                    }

                    // Player bullet and enemy
                    if(entityA.flags == 8 && entityB.flags == 4)
                    {
                        BulletComponent bullet = bulletComponentMapper.get(entityA);
                        InteractionComponent interaction = interactionComponentMapper.get(bullet.shotBy);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityB);
                        }
                    }

                    if(entityB.flags == 8 && entityA.flags == 4)
                    {
                        BulletComponent bullet = bulletComponentMapper.get(entityB);
                        InteractionComponent interaction = interactionComponentMapper.get(bullet.shotBy);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityA);
                        }
                    }

                    // Enemy bullet and player
                    if(entityA.flags == 8 && entityB.flags == 2)
                    {
                        BulletComponent bullet = bulletComponentMapper.get(entityA);
                        InteractionComponent interaction = interactionComponentMapper.get(bullet.shotBy);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityB);
                        }
                    }

                    if(entityB.flags == 8 && entityA.flags == 2)
                    {
                        BulletComponent bullet = bulletComponentMapper.get(entityB);
                        InteractionComponent interaction = interactionComponentMapper.get(bullet.shotBy);
                        if (interaction != null)
                        {
                            interaction.interactions.add(entityA);
                        }
                    }


                    // Enemy and player in its addTrigger
                    if(entityA.flags == 2 && entityB.flags == 4)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityB);
                        if (interaction != null)
                        {
                            //System.out.println("ENEMY_TRIGGER_B");
                            interaction.interactions.add(entityA);
                        }
                    }

                    if(entityB.flags == 2 && entityA.flags == 4)
                    {
                        InteractionComponent interaction = interactionComponentMapper.get(entityA);
                        if (interaction != null)
                        {
                            //System.out.println("ENEMY_TRIGGER_A");
                            interaction.interactions.add(entityB);
                        }
                    }
                }


                // Handle remaining collisions
                // Bullets with anything
                if(a.getUserData() instanceof Entity)
                {
                    Entity entityA = (Entity) a.getUserData();
                    if(entityA.flags == 8)
                    {
                        DynamicBodyComponent body = dynamicBodyComponentMapper.get(entityA);
                        if (body != null)
                        {
                            body.toDestroy = true;
                        }
                    }
                }

                if(b.getUserData() instanceof Entity)
                {
                    Entity entityB = (Entity) b.getUserData();
                    if(entityB.flags == 8)
                    {
                        DynamicBodyComponent body = dynamicBodyComponentMapper.get(entityB);
                        if (body != null)
                        {
                            body.toDestroy = true;
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) { }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
        });
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class,
                InteractionComponent.class).get());
        enemyEntities = engine.getEntitiesFor(Family.all(EnemyComponent.class,
                InteractionComponent.class).get());
    }

    @Override
    public void update(float delta)
    {
        // Handle player interactions
        for (int i = 0; i < playerEntities.size(); i++)
        {
            Entity player = playerEntities.get(i);
            InteractionComponent interactionComponent = interactionComponentMapper.get(player);
            PlayerComponent playerComponent = playerComponentMapper.get(player);
            SoundComponent soundComponent = soundComponentMapper.get(player);

            // Invoke player update event
            playerUpdate.invoke(new ValueEventArgs<>(playerComponent));

            // Apply enhancements
            applyEnhancementChain(playerComponent, delta);

            for (int j = 0; j < interactionComponent.interactions.size(); j++)
            {
                Entity interactionEntity = interactionComponent.interactions.get(j);

                // Shoot enemy
                if (interactionEntity.flags == 4)
                {
                    EnemyComponent enemyComponent = enemyComponentMapper.get(interactionEntity);

                    // Apply damage to enemy
                    enemyComponent.current.health -= playerComponent.current.damage;
                    if (enemyComponent.current.health <= 0)
                    {
                        enemyComponent.current.lives--;
                        enemyComponent.current.health = enemyComponent.current.defense;
                    }

                    // If he is dead, finish him!!!
                    if (enemyComponent.current.lives <= 0)
                    {
                        // Increment score
                        playerComponent.score += enemyComponent.score;


                        // Remove enemy
                        DynamicBodyComponent body = dynamicBodyComponentMapper.get(interactionEntity);
                        if (body.body != null)
                        {
                            body.body.setActive(false);
                            body.body.setAwake(false);
                            world.destroyBody(body.body);
                            body.body = null;
                            getEngine().removeEntity(interactionEntity);
                        }

                        // Play sound
                        if (soundComponent != null)
                            soundComponent.enqueuePlay("die");
                    }
                }

                // Interaction with coin
                if (interactionEntity.flags == 32)
                {
                    CoinComponent coin = coinComponentMapper
                            .get(interactionEntity);
                    DynamicBodyComponent body = dynamicBodyComponentMapper.get(interactionEntity);

                    // Add collected value
                    playerComponent.score += coin.value;

                    // Destroy coin after collection
                    if (body.body != null)
                    {
                        body.body.setActive(false);
                        body.body.setAwake(false);
                        world.destroyBody(body.body);
                        body.body = null;
                        getEngine().removeEntity(interactionEntity);
                    }


                    // Play sound
                    if (soundComponent != null)
                        soundComponent.enqueuePlay("collect");
                }

                // Interaction with enhancement
                if (interactionEntity.flags == 16)
                {
                    EnhancementComponent enhancement = enhancementComponentMapper
                            .get(interactionEntity);
                    DynamicBodyComponent body = dynamicBodyComponentMapper.get(interactionEntity);

                    // Permanent enhancement
                    if(enhancement.enhancement.isPermanent())
                    {
                        enhancement.enhancement.apply(playerComponent.base);
                    }
                    // Time-limited enhancement
                    else
                    {
                        if (enhancementChain == null)
                        {
                            enhancementChain = enhancement.enhancement;
                        }
                        else
                        {
                            enhancementChain.chain(enhancement.enhancement);
                        }
                    }
                    applyEnhancementChain(playerComponent, delta);


                    // Destroy enhancement after collection
                    if (body.body != null)
                    {
                        body.body.setActive(false);
                        body.body.setAwake(false);
                        world.destroyBody(body.body);
                        body.body = null;
                        getEngine().removeEntity(interactionEntity);
                    }

                    // Play sound
                    if (soundComponent != null)
                        soundComponent.enqueuePlay("collect");
                }
            }
            // All processed, clear the list
            interactionComponent.interactions.clear();
        }

        // Handle enemy interactions
        for (int i = 0; i < enemyEntities.size(); i++)
        {
            Entity enemy = enemyEntities.get(i);
            InteractionComponent interactionComponent = interactionComponentMapper.get(enemy);
            EnemyComponent enemyComponent = enemyComponentMapper.get(enemy);

            for (int j = 0; j < interactionComponent.interactions.size(); j++)
            {
                Entity interactionEntity = interactionComponent.interactions.get(j);

                // Interaction with player
                if (interactionEntity.flags == 2)
                {
                    PlayerComponent playerComponent = playerComponentMapper.get(interactionEntity);

                    // Apply damage to player
                    playerComponent.base.health -= enemyComponent.current.damage;

                    applyEnhancementChain(playerComponent, delta);
                    if (playerComponent.current.health <= 0)
                    {
                        playerComponent.base.lives--;
                        playerComponent.base.health = playerComponent.current.defense;
                    }
                    applyEnhancementChain(playerComponent, delta);


                    // If he is dead, finish him!!!
                    if (playerComponent.current.lives <= 0)
                    {
                        // Invoke player dead event
                        playerDeath.invoke(null);

                        // Play sound
                        SoundComponent soundComponent = soundComponentMapper.get(interactionEntity);
                        if (soundComponent != null)
                            soundComponent.enqueuePlay("die");
                    }
                }
            }
            // All processed, clear the list
            interactionComponent.interactions.clear();
        }
    }

    private void applyEnhancementChain(PlayerComponent playerComponent, float delta)
    {
        try
        {
            playerComponent.current = (StandardAttributes) playerComponent.base.clone();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (enhancementChain != null)
        {
            enhancementChain.step(delta);
            enhancementChain.apply(playerComponent.current);
        }
    }
}
