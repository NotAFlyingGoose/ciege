package com.runningmanstudios.caffeineGameEngine.events.scene.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;

public class SceneEntityRemovedEvent extends SceneEntityEvent{
    public SceneEntityRemovedEvent(Scene scene, AbstractEntity entity) {
        super(scene, entity);
    }
}
