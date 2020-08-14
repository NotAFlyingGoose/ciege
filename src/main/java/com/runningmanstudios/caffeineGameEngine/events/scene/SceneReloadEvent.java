package com.runningmanstudios.caffeineGameEngine.events.scene;

import com.runningmanstudios.caffeineGameEngine.rendering.Scene;

public class SceneReloadEvent extends SceneEvent {
    public SceneReloadEvent(Scene reloadedScene) {
        super(reloadedScene);
    }
}
