package com.runningmanstudios.caffeineGameEngine.events.scene;

import com.runningmanstudios.caffeineGameEngine.rendering.Scene;

public class SceneSwitchEvent extends SceneEvent {
    private final Scene lastScene;

    public SceneSwitchEvent(Scene lastScene, Scene nextScene) {
        super(nextScene);
        this.lastScene = lastScene;
    }

    public Scene getLastScene() {
        return this.lastScene;
    }
}
