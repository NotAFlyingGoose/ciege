/**
 * com.runningmanstudios.caffeineGameEngine.Entities.camera contains the AbstractCamera class and some default cameras
 * @see com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera
 */
package com.runningmanstudios.caffeineGameEngine.entities.camera;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.window.Game;

/**
 * Smooth Follow camera
 * Loosely Follows the entity
 */
public class SmoothFollowCamera extends AbstractCamera{
    private final Game game;
    private final float lerp;

    /**
     * creates the SmoothFollowCamera
     * @param game Game object
     * @param entity entity to follow
     */
    public SmoothFollowCamera(Game game, float lerp, AbstractEntity entity) {
        super(game, entity);
        this.lerp = lerp;
        this.game = game;
    }

    @Override
    public void onCreate() {

    }

    /**
     * loosely to the entity's position
     */
    @Override
    public void onUpdate() {
        this.setTargetX(this.getEntity().getX() - this.game.getWidth()/2);
        this.setTargetY(this.getEntity().getY() - this.game.getHeight()/2);

        this.setX((long) (this.getX() + (this.getTargetX() - this.getX()) * this.lerp * this.game.getDelta()));
        this.setY((long) (this.getY() + (this.getTargetY() - this.getY()) * this.lerp * this.game.getDelta()));
    }
}
