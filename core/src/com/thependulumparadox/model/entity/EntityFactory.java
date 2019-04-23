package com.thependulumparadox.model.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.thependulumparadox.control.ControlModule;
import com.thependulumparadox.control.EventControlModule;
import com.thependulumparadox.control.KeyboardControlModule;
import com.thependulumparadox.control.NetworkControlModule;
import com.thependulumparadox.misc.Range;
import com.thependulumparadox.model.component.AnimatedSpriteComponent;
import com.thependulumparadox.model.component.BulletComponent;
import com.thependulumparadox.model.component.BulletVisualsComponent;
import com.thependulumparadox.model.component.ControlComponent;
import com.thependulumparadox.model.component.DynamicBodyComponent;
import com.thependulumparadox.model.component.EnhancementVisualsComponent;
import com.thependulumparadox.model.component.InteractionComponent;
import com.thependulumparadox.model.component.PlayerComponent;
import com.thependulumparadox.model.component.SpriteComponent;
import com.thependulumparadox.model.component.StateComponent;
import com.thependulumparadox.model.component.TransformComponent;
import com.thependulumparadox.model.entity.AbstractEntityFactory;
import com.thependulumparadox.state.TaggedState;

/**
 * Factory for all components (makes component creation easier)
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
                        "sprites/bullets/circle_bullet_blue.png");
                bulletVisual1.add(new Range<Float>(0f,1000f),
                        "sprites/bullets/circle_bullet_red.png");
                player1.add(bulletVisual1);

                EnhancementVisualsComponent enhancementVisual1 = new EnhancementVisualsComponent();
                enhancementVisual1.addDefense(new Range<Float>(0f, 1000f),
                        "sprites/power_ups/powerup_aura_red.png");
                player1.add(enhancementVisual1);

                AnimatedSpriteComponent animated1 = new AnimatedSpriteComponent("packed/hero_player.atlas");
                animated1.frameDuration(0.1f);
                animated1.height = 1.8f;
                animated1.width = 1.8f;
                player1.add(animated1);

                DynamicBodyComponent dynamicBodyComponent1 = new DynamicBodyComponent(world);
                dynamicBodyComponent1.position(transform1.position)
                        .dimension(0.7f, 1.5f)
                        .properties(0, 50f, 10f, 0f);
                player1.add(dynamicBodyComponent1);

                PlayerComponent playerComponent1 = new PlayerComponent();
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

                ControlModule module1 = new EventControlModule();
                ControlComponent control1 = new ControlComponent(module1);
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
                bulletVisual.add(new Range<Float>(0f,1000f),
                        "sprites/bullets/circle_bullet_red.png");
                player2.add(bulletVisual);

                EnhancementVisualsComponent enhancementVisual = new EnhancementVisualsComponent();
                enhancementVisual.addDefense(new Range<Float>(0f, 1000f),
                        "sprites/power_ups/powerup_aura_red.png");
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
                        .properties(0, 50f, 10f, 0f);
                player2.add(dynamicBodyComponent);

                PlayerComponent playerComponent = new PlayerComponent();
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

                ControlModule module = new NetworkControlModule();
                ControlComponent control = new ControlComponent(module);
                player2.add(control);
                return player2;

            case "knight_enemy":
                System.out.println("XXX");
                break;
            case "ninja_enemy":
                System.out.println("XXX");
                break;
            case "ninja_boy_enemy":
                System.out.println("XXX");
                break;
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
                bullet.add(bulletDynamic);
                return bullet;

            case "10_coin":
                System.out.println("XXX");
                break;
            case "20_coin":
                System.out.println("XXX");
                break;
            case "defense_enhancement":
                System.out.println("XXX");
                break;
            case "life_enhancement":
                System.out.println("XXX");
                break;
            case "damage_enhancement":
                System.out.println("XXX");
                break;
            case "level_end":
                // TODO: Add proper level end entity
                return pool.createEntity();
        }

        return null;
    }
}
