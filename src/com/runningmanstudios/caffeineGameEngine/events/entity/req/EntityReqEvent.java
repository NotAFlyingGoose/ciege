package com.runningmanstudios.caffeineGameEngine.events.entity.req;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityEvent;

public class EntityReqEvent extends EntityEvent {
    public EntityReqEvent(AbstractEntity entity) {
        super(entity);
    }
}
