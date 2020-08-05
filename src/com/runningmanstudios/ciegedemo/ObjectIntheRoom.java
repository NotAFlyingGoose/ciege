package com.runningmanstudios.ciegedemo;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;

public class ObjectIntheRoom extends AbstractEntity {
    public ObjectIntheRoom(int x, int y, int width, int height, SceneManager sceneManager) {
        super(x, y, width, height, sceneManager);
    }

    @Override
    public void onCreate() {
        this.setZIndex(0);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.red);
        sg.displayRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(),true,false);
    }
}
