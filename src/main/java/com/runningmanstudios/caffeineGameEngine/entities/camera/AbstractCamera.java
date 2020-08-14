/**
 * com.runningmanstudios.caffeineGameEngine.Entities.camera contains the AbstractCamera class and some default cameras
 * @see com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera
 */
package com.runningmanstudios.caffeineGameEngine.entities.camera;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.IEntity;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.io.Serializable;

/**
 * AbstractCamera can move to follow entities
 */
public abstract class AbstractCamera implements IEntity, Serializable {
    public boolean init;
    private AbstractEntity entity;
    private long x = 0, y = 0;
    private long target_x = 0, target_y = 0;
    private boolean followX = true;
    private boolean followY = true;
    private final Game game;
    private EntityRegistry.EntityType type;{ try {
        this.type = EntityRegistry.get("BASIC"); } catch (EntityRegistryException e) { e.printStackTrace(); } }

    /**
     * creates a new AbstractCamera
     * @param game Game instance
     * @param entity Entity to follow
     */
    public AbstractCamera(Game game, AbstractEntity entity){
        this.entity = entity;
        this.game = game;
        this.lock();
    }

    /**
     * immediately go to the entity's position
     */
    public void lock() {
        this.x = this.entity.getX() - this.game.getWidth()/2;
        this.y = this.entity.getY() - this.game.getHeight()/2;
        this.init = true;
    }

    /**
     * get the x of the camera
     * @return the x of the camera
     */
    public long getX() {
        return this.x;
    }

    /**
     * get the y of the camera
     * @return the y of the camera
     */
    public long getY() {
        return this.y;
    }

    /**
     * set the x of the camera
     * @param x new x of the camera
     */
    public void setX(long x){
        if (this.followX) {
            this.x = x;
        }
    }

    /**
     * set the y of the camera
     * @param y new y of the camera
     */
    public void setY(long y){
        if (this.followY) {
            this.y = y;
        }
    }

    /**
     * get the target x of the camera
     * @return target x of the camera
     */
    public long getTargetX() {
        return this.target_x;
    }

    /**
     * get the target y of the camera
     * @return target y of the camera
     */
    public long getTargetY() {
        return this.target_y;
    }

    /**
     * set the target x of the camera
     * @param target_x new target x of the camera
     */
    public void setTargetX(long target_x){
        if (this.followX) {
            this.target_x = target_x;
        }
    }

    /**
     * set the target y of the camera
     * @param target_y new target y of the camera
     */
    public void setTargetY(long target_y){
        if (this.followY) {
            this.target_y = target_y;
        }
    }

    /**
     * get the entity this camera is following
     * @return the entity this camera is following
     */
    public AbstractEntity getEntity() {
        return this.entity;
    }

    /**
     * change the entity this camera is following
     * @param entity new entity
     */
    public void switchEntity(AbstractEntity entity){
        this.entity = entity;
    }

    /**
     * get the entity type of this camera
     * @return the entity type of this camera
     */
    public EntityRegistry.EntityType getType() {
        return this.type;
    }

    /**
     * set the entity type of this camera
     * @param type new entity type
     */
    public void setType(EntityRegistry.EntityType type) {
        this.type = type;
    }

    /**
     * whether or not to follow the entities x position or not
     * @param follow follow the entity's x position
     */
    public void followEntityX(boolean follow) {
        this.followX = follow;
    }

    /**
     * whether or not to follow the entities y position or not
     * @param follow follow the entity's y position
     */
    public void followEntityY(boolean follow) {
        this.followY = follow;
    }
}
