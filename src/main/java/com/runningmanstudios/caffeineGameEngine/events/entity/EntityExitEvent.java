package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityExitEvent extends EntityEvent {
    public EntityExitEvent(AbstractEntity entity) {
        super(entity);
    }
}
