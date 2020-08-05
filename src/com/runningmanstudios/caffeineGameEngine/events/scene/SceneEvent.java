package com.runningmanstudios.caffeineGameEngine.events.scene;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;
import com.runningmanstudios.caffeineGameEngine.checks.event.Event;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;

@EventBuilder
public class SceneEvent extends Event {
    private final Scene scene;

    public SceneEvent(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Scene getTarget() {
        return this.scene;
    }
}
