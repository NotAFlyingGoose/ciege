/**
 * com.runningmanstudios.caffeineGameEngine.Entities.basic contains classes that extend com.runningmanstudios.caffeineGameEngine.Entities.AbstractEntity
 * it is what it sounds like, it contains some default classes in case you don't want to code it yourself
 */
package com.runningmanstudios.caffeineGameEngine.entities.basic;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * SimplePlayerController uses W, A, S, D keys to move. and detects Collisions
 */
public class SimplePlayerController extends AbstractEntity {
    double velocity;
    private long velX;
    private long velY;

    /**
     * creates a new SimplePlayerController
     * @param x player x
     * @param y player y
     * @param velocity player velocity
     * @param sceneManager Scene Manager
     */
    public SimplePlayerController(int x, int y, double velocity, SceneManager sceneManager) {
        super(x, y, 25, 25, sceneManager);
        this.velocity = velocity;
        sceneManager.getGame().getEventBus().subscribe(this);
    }

    /**
     * creates the box collider and sets the EntityType
     */
    @Override
    public void onCreate() {
        try {
            this.setType(EntityRegistry.get("PLAYER"));
        } catch (EntityRegistryException entityRegistryException) {
            entityRegistryException.printStackTrace();
        }
    }

    /**
     * controls movement and BoxCollider
     */
    @Override
    public void onUpdate() {
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_W)){
            up = true;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_S)){
            down = true;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_A)){
            left = true;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_D)){
            right = true;
        }
        if (left && right) { left = false; right = false; }
        if (up && down) { up = false; down = false; }

        if (up) {
            this.setVelY(-5); } else {
            this.setVelY(0); }
        if (down) {
            this.setVelY(5); }
        if (left) {
            this.setVelX(-5); } else {
            this.setVelX(0); }
        if (right) {
            this.setVelX(5); }

        this.move(this.velX, this.velY);
    }

    public void setVelX(long velX) {
        this.velX = velX;
    }

    public void setVelY(long velY) {
        this.velY = velY;
    }

    /**
     * draws the player
     * @param sg Scenic Graphics object, for drawing
     */
    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.GRAY);
        sg.displayOval(this.getX(), this.getY(), this.getWidth(), this.getHeight(), true, true); }
}
