package com.runningmanstudios.caffeineGameEngine.events.entity;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

@EventBuilder
public class EntityCollisionEvent extends EntityEvent {
    private final AbstractEntity collidedWith;

    public EntityCollisionEvent(AbstractEntity entity, AbstractEntity collidedWith) {
        super(entity);
        this.collidedWith = collidedWith;
    }

    public AbstractEntity getCollidedWith() {
        return this.collidedWith;
    }
}
