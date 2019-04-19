package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Music;

public class MusicComponent implements Component {
    public Music music;

    public MusicComponent(Music music){
        this.music = music;
    }

    public void play(){
        music.setLooping(true);
        music.play();
    }

    public void stop(){
        music.stop();
        music.dispose();
    }
}
