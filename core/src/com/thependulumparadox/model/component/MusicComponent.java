package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Music;

/**
 * Simple component that allows entities to play music
 */
public class MusicComponent implements Component
{
    public final Music music;
    public boolean play;

    public MusicComponent(Music music)
    {
        this.music = music;
        this.play = true;
    }
}
