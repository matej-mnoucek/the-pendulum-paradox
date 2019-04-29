package com.thependulumparadox.model.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.control.AIJumpAttackControlModule;
import com.thependulumparadox.control.AIRunAroundControlModule;
import com.thependulumparadox.control.AIWanderAttackControlModule;
import com.thependulumparadox.control.ControlModule;
import com.thependulumparadox.control.ControlModuleMultiplexer;
import com.thependulumparadox.control.KeyboardControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.misc.Range;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.BulletVisualsComponent;
import com.thependulumparadox.model.component.CoinComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnemyComponent;
import com.thependulumparadox.model.component.EnhancementComponent;
import com.thependulumparadox.model.component.EnhancementVisualsComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SoundComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.StaticBodyComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.component.enhancement.AddDefenseEnhancement;
import com.thependulumparadox.model.component.enhancement.AddLifeEnhancement;
import com.thependulumparadox.model.component.enhancement.Enhancement;
import com.thependulumparadox.model.component.enhancement.MultiplyDamageEnhancement;
import com.thependulumparadox.state.TaggedState;

/**
 * Implementation of factory for entities (makes their creation easier)
 * @see AbstractEntityFactory
 */
public class EntityFactory extends AbstractEntityFactory
{
    // Entity pool
    private PooledEngine pool = new PooledEngine();

    // Physics world
    private World world;

    public EntityFactory(World world)
    {
        this.world = world;
    }

    @Override
    public Entity create(String entity)
    {
        switch(entity)
        {
            case "first_player":
                Entity player1 = pool.createEntity();
                player1.flags = 2;

                TransformComponent transform1 = new TransformComponent();
                player1.add(transform1);

                SpriteComponent sprite1 = new SpriteComponent(null);
                sprite1.rotationSpeed = 1f;
                sprite1.width = 2.0f;
                sprite1.height = 2.0f;
                player1.add(sprite1);

                BulletVisualsComponent bulletVisual1 = new BulletVisualsComponent(
                        "sprites/bullets/circle_bullet_red.png");
                bulletVisual1.add(new Range<Float>(150f,1000f),
                        "sprites/bullets/circle_bullet_violet.png");
                player1.add(bulletVisual1);

                EnhancementVisualsComponent enhancementVisual1 = new EnhancementVisualsComponent();
                enhancementVisual1.addDefense(new Range<Float>(200f, 1000f),
                        "sprites/power_ups/powerup_aura_red.png");
                player1.add(enhancementVisual1);

                AnimatedSpriteComponent animated1 = new AnimatedSpriteComponent(
                        "packed/hero_player.atlas");
                animated1.frameDuration(0.1f);
                animated1.height = 1.8f;
                animated1.width = 1.8f;
                player1.add(animated1);

                DynamicBodyComponent dynamicBodyComponent1 = new DynamicBodyComponent(world);
                dynamicBodyComponent1.position(transform1.position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 100f, 1f, 0f);
                player1.add(dynamicBodyComponent1);

                PlayerComponent playerComponent1 = new PlayerComponent(1);
                player1.add(playerComponent1);

                StateComponent state1 = new StateComponent();
                state1.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                        .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                        .add(new TaggedState("jumpRight")).add(new TaggedState("jumpLeft"))
                        .add(new TaggedState("shootRight")).add(new TaggedState("shootLeft"))
                        .initial("idleRight");
                player1.add(state1);

                InteractionComponent interaction1 = new InteractionComponent();
                player1.add(interaction1);

                SoundComponent sound1 = new SoundComponent();
                sound1.addSound("shoot", Gdx.audio.newSound(Gdx.files.internal("sounds/single_gunshot.mp3")))
                        .addSound("jump", Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3")))
                        .addSound("collect", Gdx.audio.newSound(Gdx.files.internal("sounds/coin_collect.mp3")))
                        .addSound("die", Gdx.audio.newSound(Gdx.files.internal("sounds/die.mp3")));
                player1.add(sound1);

                ControlModule module1 = new KeyboardControlModule();
                ControlModule multiplexer1 = new ControlModuleMultiplexer(module1, null);
                ControlComponent control1 = new ControlComponent(multiplexer1);
                player1.add(control1);

                return player1;

            case "second_player":
                Entity player2 = pool.createEntity();
                player2.flags = 2;

                TransformComponent transform = new TransformComponent();
                player2.add(transform);

                SpriteComponent sprite = new SpriteComponent(null);
                sprite.rotationSpeed = 1f;
                sprite.width = 2.0f;
                sprite.height = 2.0f;
                player2.add(sprite);

                BulletVisualsComponent bulletVisual = new BulletVisualsComponent(
                        "sprites/bullets/circle_bullet_blue.png");
                bulletVisual.add(new Range<Float>(150f,1000f),
                        "sprites/bullets/circle_bullet_green.png");
                player2.add(bulletVisual);

                EnhancementVisualsComponent enhancementVisual = new EnhancementVisualsComponent();
                enhancementVisual.addDefense(new Range<Float>(200f, 1000f),
                        "sprites/power_ups/powerup_aura_blue.png");
                player2.add(enhancementVisual);

                AnimatedSpriteComponent animated = new AnimatedSpriteComponent(
                        "packed/hero_multiplayer.atlas");
                animated.frameDuration(0.1f);
                animated.height = 1.8f;
                animated.width = 1.8f;
                player2.add(animated);

                DynamicBodyComponent dynamicBodyComponent = new DynamicBodyComponent(world);
                dynamicBodyComponent.position(transform.position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 100f, 1f, 0f);
                player2.add(dynamicBodyComponent);

                PlayerComponent playerComponent = new PlayerComponent(2);
                player2.add(playerComponent);

                StateComponent state = new StateComponent();
                state.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                        .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                        .add(new TaggedState("jumpRight")).add(new TaggedState("jumpLeft"))
                        .add(new TaggedState("shootRight")).add(new TaggedState("shootLeft"))
                        .initial("idleRight");
                player2.add(state);

                InteractionComponent interaction = new InteractionComponent();
                player2.add(interaction);

                SoundComponent sound2 = new SoundComponent();
                sound2.addSound("shoot", Gdx.audio.newSound(Gdx.files.internal("sounds/single_gunshot.mp3")))
                        .addSound("jump", Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3")))
                        .addSound("collect", Gdx.audio.newSound(Gdx.files.internal("sounds/coin_collect.mp3")))
                        .addSound("die", Gdx.audio.newSound(Gdx.files.internal("sounds/die.mp3")));
                player2.add(sound2);

                ControlModule module = new NetworkControlModule(dynamicBodyComponent.body);
                ControlComponent control = new ControlComponent(module);
                player2.add(control);

                return player2;

            case "knight_enemy":
                Entity enemy1 = pool.createEntity();
                enemy1.flags = 4;

                TransformComponent transformEnemy1 = new TransformComponent();
                transformEnemy1.position = new Vector2(0,0);
                enemy1.add(transformEnemy1);

                AnimatedSpriteComponent animatedEnemy1 = new AnimatedSpriteComponent(
                        "packed/knight_enemy.atlas");
                animatedEnemy1.frameDuration(0.07f);
                animatedEnemy1.height = 1.8f;
                animatedEnemy1.width = 1.6f;
                enemy1.add(animatedEnemy1);

                DynamicBodyComponent dynamicBodyEnemy1 = new DynamicBodyComponent(world);
                dynamicBodyEnemy1.position(transformEnemy1.position)
                        .dimension(0.7f, 1.5f)
                        .addTrigger(1f)
                        .properties(0, 50f, 10f, 0f)
                        .activate(true);
                enemy1.add(dynamicBodyEnemy1);

                StateComponent stateEnemy1 = new StateComponent();
                stateEnemy1.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                        .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                        .add(new TaggedState("attackRight")).add(new TaggedState("attackLeft"))
                        .initial("idleRight");
                enemy1.add(stateEnemy1);

                EnemyComponent enemyComponent1 = new EnemyComponent();
                enemy1.add(enemyComponent1);

                InteractionComponent interactionEnemy1 = new InteractionComponent();
                enemy1.add(interactionEnemy1);

                ControlModule moduleEnemy1 = new AIWanderAttackControlModule();
                ControlComponent controlEnemy1 = new ControlComponent(moduleEnemy1);
                controlEnemy1.backToIdleSpeedThreshold = 0f;
                enemy1.add(controlEnemy1);

                return enemy1;

            case "ninja_boy_enemy":
                Entity enemy2 = pool.createEntity();
                enemy2.flags = 4;

                TransformComponent transformEnemy2 = new TransformComponent();
                transformEnemy2.position = new Vector2(0,0);
                enemy2.add(transformEnemy2);

                AnimatedSpriteComponent animatedEnemy2 = new AnimatedSpriteComponent(
                        "packed/ninja_boy_enemy.atlas");
                animatedEnemy2.frameDuration(0.07f);
                animatedEnemy2.height = 1.6f;
                animatedEnemy2.width = 1.1f;
                enemy2.add(animatedEnemy2);

                DynamicBodyComponent dynamicBodyEnemy2 = new DynamicBodyComponent(world);
                dynamicBodyEnemy2.position(transformEnemy2.position)
                        .dimension(0.7f, 1.5f)
                        .addTrigger(1f)
                        .properties(0, 50f, 10f, 0f)
                        .activate(true);
                enemy2.add(dynamicBodyEnemy2);

                StateComponent stateEnemy2 = new StateComponent();
                stateEnemy2.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                        .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                        .add(new TaggedState("attackRight")).add(new TaggedState("attackLeft"))
                        .initial("idleRight");
                enemy2.add(stateEnemy2);

                EnemyComponent enemyComponent2 = new EnemyComponent();
                enemy2.add(enemyComponent2);

                InteractionComponent interactionEnemy2 = new InteractionComponent();
                enemy2.add(interactionEnemy2);

                ControlModule moduleEnemy2 = new AIRunAroundControlModule();
                ControlComponent controlEnemy2 = new ControlComponent(moduleEnemy2);
                controlEnemy2.backToIdleSpeedThreshold = 0f;
                enemy2.add(controlEnemy2);

                return enemy2;

            case "ninja_enemy":
                Entity enemy3 = pool.createEntity();
                enemy3.flags = 4;

                TransformComponent transformEnemy3 = new TransformComponent();
                transformEnemy3.position = new Vector2(0,0);
                enemy3.add(transformEnemy3);

                AnimatedSpriteComponent animatedEnemy3 = new AnimatedSpriteComponent(
                        "packed/ninja_enemy.atlas");
                animatedEnemy3.frameDuration(0.07f);
                animatedEnemy3.height = 1.6f;
                animatedEnemy3.width = 1.1f;
                enemy3.add(animatedEnemy3);

                DynamicBodyComponent dynamicBodyEnemy3 = new DynamicBodyComponent(world);
                dynamicBodyEnemy3.position(transformEnemy3.position)
                        .dimension(0.7f, 1.5f)
                        .addTrigger(1f)
                        .properties(0, 50f, 10f, 0f)
                        .activate(true);
                enemy3.add(dynamicBodyEnemy3);

                StateComponent stateEnemy3 = new StateComponent();
                stateEnemy3.add(new TaggedState("idleLeft")).add(new TaggedState("idleRight"))
                        .add(new TaggedState("runLeft")).add(new TaggedState("runRight"))
                        .add(new TaggedState("jumpRight")).add(new TaggedState("jumpLeft"))
                        .initial("idleRight");
                enemy3.add(stateEnemy3);

                EnemyComponent enemyComponent3 = new EnemyComponent();
                enemy3.add(enemyComponent3);

                InteractionComponent interactionEnemy3 = new InteractionComponent();
                enemy3.add(interactionEnemy3);

                ControlModule moduleEnemy3 = new AIJumpAttackControlModule();
                ControlComponent controlEnemy3 = new ControlComponent(moduleEnemy3);
                controlEnemy3.backToIdleSpeedThreshold = 0.1f;
                enemy3.add(controlEnemy3);

                return enemy3;

            case "bullet":
                Entity bullet = pool.createEntity();
                bullet.flags = 8;

                TransformComponent bulletTransform = new TransformComponent();
                bulletTransform.position = new Vector2(new Vector2(0,0));
                bullet.add(bulletTransform);

                SpriteComponent bulletSprite = new SpriteComponent(null);
                bulletSprite.height = 0.3f;
                bulletSprite.width = 0.3f;
                bullet.add(bulletSprite);

                BulletComponent bulletComponent = new BulletComponent(null);
                bullet.add(bulletComponent);

                DynamicBodyComponent bulletDynamic = new DynamicBodyComponent(world);
                bulletDynamic.position(new Vector2(0,0)).dimension(bulletSprite.width,
                        bulletSprite.height).gravityScale(0.0f).activate(true);
                bulletDynamic.body.setBullet(true);
                bullet.add(bulletDynamic);
                return bullet;

            case "10_coin":
                Entity coin1 = pool.createEntity();
                coin1.flags = 32;

                TransformComponent transformCoin1 = new TransformComponent();
                transformCoin1.position = new Vector2(0,0);
                coin1.add(transformCoin1);

                SpriteComponent spriteCoin1 = new SpriteComponent(
                        "sprites/collectables/wheel2.png");
                spriteCoin1.rotationSpeed = 1.5f;
                spriteCoin1.height = 1f;
                spriteCoin1.width = 1f;
                coin1.add(spriteCoin1);

                DynamicBodyComponent dynamicBodyCoin1 = new DynamicBodyComponent(world);
                dynamicBodyCoin1.position(transformCoin1.position)
                        .dimension(0.7f, 1.5f)
                        .gravityScale(0)
                        .makeTrigger()
                        .activate(true);
                coin1.add(dynamicBodyCoin1);

                CoinComponent componentCoin1 = new CoinComponent();
                componentCoin1.value = 10;
                coin1.add(componentCoin1);

                return coin1;

            case "20_coin":
                Entity coin2 = pool.createEntity();
                coin2.flags = 32;

                TransformComponent transformCoin2 = new TransformComponent();
                transformCoin2.position = new Vector2(7,8);
                coin2.add(transformCoin2);

                SpriteComponent spriteCoin2 = new SpriteComponent(
                        "sprites/collectables/wheel1.png");
                spriteCoin2.rotationSpeed = 1.5f;
                spriteCoin2.height = 1f;
                spriteCoin2.width = 1f;
                coin2.add(spriteCoin2);

                DynamicBodyComponent dynamicBodyCoin2 = new DynamicBodyComponent(world);
                dynamicBodyCoin2.position(transformCoin2.position)
                        .dimension(0.7f, 1.5f)
                        .gravityScale(0)
                        .makeTrigger()
                        .activate(true);
                coin2.add(dynamicBodyCoin2);
                
                CoinComponent componentCoin2 = new CoinComponent();
                componentCoin2.value = 20;
                coin2.add(componentCoin2);

                return coin2;

            case "life_enhancement":
                Entity enhancement1 = pool.createEntity();
                enhancement1.flags = 16;

                TransformComponent transformEnhancement1 = new TransformComponent();
                transformEnhancement1.position = new Vector2(0,0);
                enhancement1.add(transformEnhancement1);

                SpriteComponent spriteEnhancement1 = new SpriteComponent(
                        "sprites/power_ups/powerup_red.png");
                spriteEnhancement1.rotationSpeed = 1.5f;
                spriteEnhancement1.height = 1f;
                spriteEnhancement1.width = 1f;
                enhancement1.add(spriteEnhancement1);

                DynamicBodyComponent dynamicBodyEnhancement1 = new DynamicBodyComponent(world);
                dynamicBodyEnhancement1.position(transformEnhancement1.position)
                        .dimension(0.7f, 1.5f)
                        .gravityScale(0)
                        .makeTrigger()
                        .activate(true);
                enhancement1.add(dynamicBodyEnhancement1);

                Enhancement implEnhancement1 = new AddLifeEnhancement(1);
                EnhancementComponent componentEnhancement1 = new EnhancementComponent(implEnhancement1);
                enhancement1.add(componentEnhancement1);

                return enhancement1;

            case "defense_enhancement":
                Entity enhancement2 = pool.createEntity();
                enhancement2.flags = 16;

                TransformComponent transformEnhancement2 = new TransformComponent();
                transformEnhancement2.position = new Vector2(0,0);
                enhancement2.add(transformEnhancement2);

                SpriteComponent spriteEnhancement2 = new SpriteComponent(
                        "sprites/power_ups/powerup_green.png");
                spriteEnhancement2.rotationSpeed = 1.5f;
                spriteEnhancement2.height = 1f;
                spriteEnhancement2.width = 1f;
                enhancement2.add(spriteEnhancement2);

                DynamicBodyComponent dynamicBodyEnhancement2 = new DynamicBodyComponent(world);
                dynamicBodyEnhancement2.position(transformEnhancement2.position)
                        .dimension(0.7f, 1.5f)
                        .gravityScale(0)
                        .makeTrigger()
                        .activate(true);
                enhancement2.add(dynamicBodyEnhancement2);

                Enhancement implEnhancement2 = new AddDefenseEnhancement(200,15);
                EnhancementComponent componentEnhancement2 = new EnhancementComponent(implEnhancement2);
                enhancement2.add(componentEnhancement2);

                return enhancement2;

            case "damage_enhancement":
                Entity enhancement3 = pool.createEntity();
                enhancement3.flags = 16;

                TransformComponent transformEnhancement3 = new TransformComponent();
                transformEnhancement3.position = new Vector2(0,0);
                enhancement3.add(transformEnhancement3);

                SpriteComponent spriteEnhancement3 = new SpriteComponent(
                        "sprites/power_ups/powerup_blue.png");
                spriteEnhancement3.rotationSpeed = 1.5f;
                spriteEnhancement3.height = 1f;
                spriteEnhancement3.width = 1f;
                enhancement3.add(spriteEnhancement3);

                DynamicBodyComponent dynamicBodyEnhancement3 = new DynamicBodyComponent(world);
                dynamicBodyEnhancement3.position(transformEnhancement3.position)
                        .dimension(0.7f, 1.5f)
                        .gravityScale(0)
                        .makeTrigger()
                        .activate(true);
                enhancement3.add(dynamicBodyEnhancement3);
                
                Enhancement implEnhancement3 = new MultiplyDamageEnhancement(4, 20);
                EnhancementComponent componentEnhancement3 = new EnhancementComponent(implEnhancement3);
                enhancement3.add(componentEnhancement3);

                return enhancement3;

            case "level_end":
                // TODO: Add proper level end entity
                return pool.createEntity();
        }

        return null;
    }
}
