package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityCreateEvent extends EntityEvent {
    public EntityCreateEvent(AbstractEntity entity) {
        super(entity);
    }
}
