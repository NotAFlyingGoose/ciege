package com.runningmanstudios.caffeineGameEngine.events.entity.req;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

public class EntityCreateEvent extends EntityReqEvent {
    public EntityCreateEvent(AbstractEntity entity) {
        super(entity);
    }
}
