package com.runningmanstudios.caffeineGameEngine.events.entity.req;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityUpdateEvent extends EntityReqEvent {
    public EntityUpdateEvent(AbstractEntity entity) {
        super(entity);
    }
}
