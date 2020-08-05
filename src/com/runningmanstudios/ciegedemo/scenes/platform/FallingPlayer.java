package com.runningmanstudios.ciegedemo.scenes.platform;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FallingPlayer extends AbstractEntity {
    private final int maxjump = 10;
    private final int jump = 0;
    private final double velocity;
    private final double velocityScale;

    // Some necessary constants.
    public int width = this.getSceneManager().getGame().getWidth(), height = this.getSceneManager().getGame().getWidth();
    public int floorHeight = this.height - 64;

    // Variables that controls the player's position and velocity on both axes.
    private float x = 0;
    private float y = this.floorHeight;
    private final float speed = 3;
    private float jumpStrength;
    private final float weight = 1;

    public FallingPlayer(int x, int y, double velocity, double velocityScale, SceneManager sceneManager) {
        super(x, y, sceneManager);
        this.velocity = velocity;
        this.velocityScale = velocityScale;
    }

    @Override
    public void onCreate() {
        try {
            this.setType(EntityRegistry.get("PLAYER"));
        } catch (EntityRegistryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_SPACE) && this.y >= this.floorHeight) {
            this.jumpStrength = 24; // Will result in the player moving upwards.
        }

        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_A)) {
            this.x -= this.speed;
        }

        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_D)) {
            this.x += this.speed;
        }

        this.y -= this.jumpStrength; // Move the player on the y-axis based on the strength of the jump.
        this.jumpStrength -= this.weight; // Gradually decrease the strength of the jump by the player's weight.

        if (this.y >= this.floorHeight) this.y = this.floorHeight; // Ensure the player does not fall through the floor.

        this.setX((long) this.x);
        this.setY((long) this.y);
    }

    @Override
    public void onDraw(ScenicGraphics scenicGraphics) {
        Graphics g = scenicGraphics.getGraphics();
        g.setColor(Color.green);
        g.fillRect(0, this.height - 32, this.width *2, 300);

        g.setColor(Color.gray);
        g.fillRect((int) this.x, (int) this.y -32, 32, 64);
    }
}
