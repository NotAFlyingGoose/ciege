package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityUpdateEvent extends EntityEvent {
    public EntityUpdateEvent(AbstractEntity entity) {
        super(entity);
    }
}
