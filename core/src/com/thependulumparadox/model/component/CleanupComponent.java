package com.thependulumparadox.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class CleanupComponent implements Component {

    Entity player;
    public CleanupComponent(Entity player){
        this.player = player;
    }

    public Entity getPlayer(){
        return player;
    }
}
