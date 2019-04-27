package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.thependulumparadox.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Simple component that allows entities to play sounds. One component can hold more than one sound.
 */
public class SoundComponent implements Component
{
    // All available sounds
    private final Map<String, Sound> sounds;

    // Sounds to play
    public final Queue<Sound> toPlay;

    public SoundComponent()
    {
        sounds = new HashMap<>();
        toPlay = new LinkedList<>();
    }

    public SoundComponent addSound(String key, Sound sound)
    {
        sounds.put(key, sound);
        return this;
    }

    public boolean enqueuePlay(String key)
    {
        if (sounds.containsKey(key))
        {
            toPlay.add(sounds.get(key));
            return true;
        }
        else
        {
            return false;
        }
    }
}
