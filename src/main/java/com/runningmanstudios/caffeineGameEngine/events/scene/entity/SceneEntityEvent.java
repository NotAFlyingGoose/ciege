package com.runningmanstudios.caffeineGameEngine.events.scene.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.events.scene.SceneEvent;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;

public class SceneEntityEvent extends SceneEvent {
    private final AbstractEntity entity;
    public SceneEntityEvent(Scene scene, AbstractEntity entity) {
        super(scene);
        this.entity = entity;
    }
    public AbstractEntity getEntity() {
        return this.entity;
    }
}
