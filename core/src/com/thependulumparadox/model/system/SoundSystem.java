package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.thependulumparadox.misc.Pair;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.SoundComponent;

/**
 * This system handles music and sounds playback
 * @see SoundComponent
 * @see MusicComponent
 */
public class SoundSystem extends EntitySystem
{
    private ImmutableArray<Entity> soundEntities;
    private ImmutableArray<Entity> musicEntities;

    private ComponentMapper<SoundComponent> soundComponentMapper
            = ComponentMapper.getFor(SoundComponent.class);
    private ComponentMapper<MusicComponent> musicComponentMapper
            = ComponentMapper.getFor(MusicComponent.class);

    // Turn sound on or off
    public boolean soundOn = true;

    
    @Override
    public void addedToEngine(Engine engine)
    {
        soundEntities = engine.getEntitiesFor(Family.all(SoundComponent.class).get());
        musicEntities = engine.getEntitiesFor(Family.all(MusicComponent.class).get());
    }
    
    @Override
    public void update(float deltaTime)
    {
        // Deal with sounds at first
        for (int i = 0; i < soundEntities.size(); i++)
        {
            Entity entity = soundEntities.get(i);
            SoundComponent soundComponent = soundComponentMapper.get(entity);

            // Play once sounds
            while(!soundComponent.toPlay.isEmpty())
            {
               Sound sound = soundComponent.toPlay.remove();

               if(soundOn)
               {
                   sound.play();
               }
            }
        }

        // Then with music
        for (int i = 0; i < musicEntities.size(); i++)
        {
            Entity entity = musicEntities.get(i);
            MusicComponent musicComponent = musicComponentMapper.get(entity);

            if (musicComponent.play && !musicComponent.music.isPlaying() && soundOn)
            {
                musicComponent.music.play();
            }

            if (!musicComponent.play && musicComponent.music.isPlaying())
            {
                musicComponent.music.stop();
            }

            if(musicComponent.music.isPlaying() && !soundOn)
            {
                musicComponent.music.stop();
            }
        }
    }
}

