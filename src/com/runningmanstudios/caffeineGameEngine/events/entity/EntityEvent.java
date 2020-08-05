package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.checks.event.Event;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityEvent extends Event {
    private final AbstractEntity entity;

    public EntityEvent(AbstractEntity entity) {
        this.entity = entity;
    }

    @Override
    public AbstractEntity getTarget() {
        return this.entity;
    }
}
