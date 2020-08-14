/**
 * com.runningmanstudios.caffeineGameEngine.Entities.camera contains the AbstractCamera class and some default cameras
 * @see com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera
 */
package com.runningmanstudios.caffeineGameEngine.entities.camera;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.window.Game;

/**
 * Basic Follow camera
 * Keeps entity x and y in the middle of the screen
 */
public class FollowCamera extends AbstractCamera{
    private final Game game;

    /**
     * creates the FollowCamera
     * @param game Game object
     * @param entity entity to follow
     */
    public FollowCamera(Game game, AbstractEntity entity) {
        super(game, entity);
        this.game = game;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onExit() {

    }

    /**
     * go to the entity's position
     */
    @Override
    public void onUpdate(){
        this.setTargetX(this.getEntity().getX() - this.getEntity().getWidth()/2);
        this.setTargetY(this.getEntity().getY() - this.getEntity().getHeight()/2);

        this.setX(this.getEntity().getX() - this.game.getWidth()/2);
        this.setY(this.getEntity().getY() - this.game.getHeight()/2);
    }
}
