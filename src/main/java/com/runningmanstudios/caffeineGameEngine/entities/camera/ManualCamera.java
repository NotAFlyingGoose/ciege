/**
 * com.runningmanstudios.caffeineGameEngine.Entities.camera contains the AbstractCamera class and some default cameras
 * @see com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera
 */
package com.runningmanstudios.caffeineGameEngine.entities.camera;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.window.Game;

/**
 * Basic Manual camera
 * Must be controlled normally
 */
public class ManualCamera extends AbstractCamera{
    public ManualCamera(Game game, AbstractEntity entity) {
        super(game, entity);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void onUpdate() {

    }

    /**
     * move right by amt
     * @param amt how much to move
     */
    public void moveRight(int amt) {
        this.setX(this.getX() + amt);
    }

    /**
     * move left by amt
     * @param amt how much to move
     */
    public void moveLeft(int amt) {
        this.setX(this.getX() - amt);
    }

    /**
     * move up by amt
     * @param amt how much to move
     */
    public void moveUp(int amt) {
        this.setY(this.getY() - amt);
    }

    /**
     * move down by amt
     * @param amt how much to move
     */
    public void moveDown(int amt) {
        this.setY(this.getY() + amt);
    }
}
