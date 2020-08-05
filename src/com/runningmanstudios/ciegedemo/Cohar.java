package com.runningmanstudios.ciegedemo;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import java.awt.*;

public class Cohar extends AbstractEntity {
    public Cohar(int x, int y, int width, int height, SceneManager sceneManager) {
        super(x, y, width, height, sceneManager);
    }

    @Override
    public void onCreate() {
        this.setZIndex(5);
        try {
            this.setType(EntityRegistry.get("CHARACTER"));
        } catch (EntityRegistryException entityRegistryExeption) {
            entityRegistryExeption.printStackTrace();
        }
        GameLogger.info("hey");
    }

    @Override
    public void onUpdate() {
        this.move(1, 0);
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.orange);
        sg.displayRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(),true,false);
    }
}
