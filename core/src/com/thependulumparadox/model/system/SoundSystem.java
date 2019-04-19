package com.thependulumparadox.model.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.thependulumparadox.model.component.MusicComponent;
import com.thependulumparadox.model.component.SoundComponent;

/**
 * Music playback rules
 */
public class SoundSystem extends EntitySystem
{
    public ImmutableArray<Entity> sounds;
    public MusicComponent music = null;
    public Engine engine;


    private ComponentMapper<SoundComponent> soundComponentMapper
            = ComponentMapper.getFor(SoundComponent.class);
    private ComponentMapper<MusicComponent> musicComponentMapper
            = ComponentMapper.getFor(MusicComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        sounds = engine.getEntitiesFor(Family.all(SoundComponent.class).get());


    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        sounds = engine.getEntitiesFor(Family.all(SoundComponent.class).get());
        ImmutableArray<Entity> temp = engine.getEntitiesFor(Family.all(MusicComponent.class).get());
        System.out.println(temp.size());
        if (temp.size() != 0) {
            MusicComponent tempmusic = musicComponentMapper.get(temp.first());
            if (tempmusic != music) {
                tempmusic.play();
                music = tempmusic;
            }
        }
        for (Entity ent : sounds){
            SoundComponent sound = soundComponentMapper.get(ent);
              sound.play();
              engine.removeEntity(ent);


        }


    }
}
