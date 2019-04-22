package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;

public class SoundComponent implements Component
{
    public Sound sound;
    public Boolean playOnce;
    public Boolean isPlaying = false;

    public SoundComponent(Sound sound, Boolean playOnce){
        this.sound= sound;
        this.playOnce = playOnce;
    }

    public void play(){
        sound.play();
    }
}
