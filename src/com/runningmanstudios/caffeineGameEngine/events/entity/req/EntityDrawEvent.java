package com.runningmanstudios.caffeineGameEngine.events.entity.req;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

public class EntityDrawEvent extends EntityReqEvent {
    private final ScenicGraphics scenicGraphics;

    public EntityDrawEvent(AbstractEntity entity, ScenicGraphics scenicGraphics) {
        super(entity);
        this.scenicGraphics = scenicGraphics;
    }

    public ScenicGraphics getScenicGraphics() {
        return this.scenicGraphics;
    }
}
