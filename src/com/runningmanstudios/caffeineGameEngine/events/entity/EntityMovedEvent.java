package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityMovedEvent extends EntityEvent {
    public EntityMovedEvent(AbstractEntity entity) {
        super(entity);
    }
}
