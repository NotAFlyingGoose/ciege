package com.runningmanstudios.ciegedemo.scenes.platform;

import com.runningmanstudios.caffeineGameEngine.entities.camera.SmoothFollowCamera;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.util.GameTimer;
import com.runningmanstudios.ciegedemo.Cohar;

import java.awt.*;

public class Platform extends Scene {
    GameTimer timer;
    FallingPlayer player;
    Cohar c;
    SmoothFollowCamera camera;
    public Platform(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        this.timer = new GameTimer(500);
        this.player = new FallingPlayer(50,50,3.0,2.5, this.getSceneManager());
        this.c = new Cohar(100,530,1000,200, this.getSceneManager());
        this.camera = new SmoothFollowCamera(this.getSceneManager().getGame(), 0.1f, this.player);
        this.setCamera(this.camera);
        this.addEntityToScene(this.player);
        //addEntityToScene(this.c);
    }

    @Override
    public void onUpdate() {
        if (this.timer.isDone()) {
            this.timer.kill();
            this.camera.followEntityY(false);
        }
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(new Color(195, 251, 249));
        sg.displayRect(-100,-100,10000,10000,true,false);
    }

    /**
     * abstract onExit() method, called when Scene Manager unloads this scene
     */
    @Override
    public void onExit() {

    }
}
