package com.pendulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component//, ApplicationListener
{
    //TODO: definitely better ways of doing this
    enum animationTypes{
        IDLE,
        RUNNING,
        SHOOTING,
        JUMPING,
        //FALLING, //TODO: fix later. have to rename the jump files that are dropping
        DEATH,
        ANIMATION_COUNT
    }

    public Animation [] animations;
    private animationTypes currentAnimation;
    private int animationFps = 30;
    public AnimationComponent(){
        //TODO: make this dynamic
        int animationCount = animationTypes.ANIMATION_COUNT.ordinal();
        animations = new Animation[animationCount];

        //TODO: Find proper way to enter path. I'm certain there is a better way, and this might not work on mobile
        TextureAtlas atlas = new TextureAtlas(Gdx.files.local("..\\game-android\\assets\\robot.atlas"));
        float frameDuration = 1.0f/animationFps;
        animations[animationTypes.DEATH.ordinal()]    =  new Animation<TextureRegion>(frameDuration,atlas.findRegions("Dead"));
        animations[animationTypes.RUNNING.ordinal()]  =  new Animation<TextureRegion>(frameDuration,atlas.findRegions("Run"));
        animations[animationTypes.SHOOTING.ordinal()] =  new Animation<TextureRegion>(frameDuration,atlas.findRegions("Shoot"));
        animations[animationTypes.IDLE.ordinal()]     =  new Animation<TextureRegion>(frameDuration,atlas.findRegions("Idle"));
        animations[animationTypes.JUMPING.ordinal()]  =  new Animation<TextureRegion>(frameDuration,atlas.findRegions("Jump"));
        currentAnimation = animationTypes.IDLE;
    }

    public TextureRegion getCurrentFrame(float time){
        return (TextureRegion)animations[currentAnimation.ordinal()].getKeyFrame(time);
    }

    public TextureRegion getCurrentFrame(float time, boolean looping){
        return (TextureRegion)animations[currentAnimation.ordinal()].getKeyFrame(time,looping);
    }


      //for sprite sheets if we/when needed. TODO: not finished
//    public AnimationComponent(){
//        //TODO: make this dynamic
//        int animationCount = animationTypes.ANIMATION_COUNT.ordinal();
//        int frameCols = 6;
//        int frameRows = 5;
//
//        Texture texture = new Texture(Gdx.files.internal("animation_sheet.png"));
//        animations = new Animation[animationCount];
//        TextureRegion[][] textureRegion = TextureRegion.split(texture,
//                texture.getWidth() / frameCols,
//                texture.getHeight() / frameRows);
//
//        for (int i = 0; i < animationCount; i++){
//            int index = 0;
//            TextureRegion[] walkFrames = new TextureRegion[frameCols * frameRows];
//
//            for (int y = 0; y < frameRows; y++) {
//                for (int x = 0; x < frameCols; x++) {
//                    walkFrames[index++] = textureRegion[y][x];
//                }
//            }
//        }
//    }


}

