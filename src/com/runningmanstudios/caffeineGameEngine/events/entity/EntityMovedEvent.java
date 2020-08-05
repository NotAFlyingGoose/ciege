package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

@EventBuilder
public class EntityMovedEvent extends EntityEvent {
    public EntityMovedEvent(AbstractEntity entity) {
        super(entity);
    }
}
