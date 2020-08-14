/**
 * com.runningmanstudios.caffeineGameEngine.Entities' contains classes for creating Entities
 * @see com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity
 */
package com.runningmanstudios.caffeineGameEngine.entities;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.components.EntityComponent;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityMovedEvent;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityCreateEvent;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityExitEvent;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.util.FancyMath;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractEntity for creating entities that can have interactions, their own onUpdate() method, their own onDraw() method,
 * and are separate from the Scene
 */
public abstract class AbstractEntity implements IEntity, Serializable {
	private long lastX, lastY, lastZIndex, x, y, zIndex = 0, width = -1, height = -1;
	private final float acceleration = 1f;
	private final float deceleration = 0.5f;
	private float velX;
	private float velY;
	private float lastVelX;
	private float lastVelY;
	private float maxVel = 5f;
	private boolean moved = false, init = false;
	private EntityRegistry.EntityType type;{ try {
		this.type = EntityRegistry.get("BASIC"); } catch (EntityRegistryException e) { e.printStackTrace(); } }
	private final SceneManager sceneManager;
	private final Map<String, EntityComponent> entityComponents = new HashMap<>();

	/**
	 * creates an AbstractEntity with x and y but no width and height
	 * @param x entity x
	 * @param y entity y
	 * @param sceneManager Scene Manager
	 */
	public AbstractEntity(int x, int y, SceneManager sceneManager) {
		this.x = x;
		this.y = y;
		this.sceneManager = sceneManager;
	}

	/**
	 * creates an AbstractEntity with x, y, width and height
	 * @param x entity x
	 * @param y entity y
	 * @param width entity width
	 * @param height entity height
	 * @param sceneManager Scene Manager
	 */
	public AbstractEntity(int x, int y, int width, int height, SceneManager sceneManager) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sceneManager = sceneManager;
	}

	/**
	 * gets the x position of this entity
	 * @return x of this entity
	 */
	public long getX() {
		return this.x;
	}

	/**
	 * gets the y position of this entity
	 * @return y of this entity
	 */
	public long getY() {
		return this.y;
	}

	/**
	 * gets the velocity x of this entity
	 * @return velocity x of this entity
	 */
	public float getVelX() {
		this.velX = FancyMath.clamp(this.velX, -this.maxVel, this.maxVel);
		return this.velX;
	}

	/**
	 * gets the velocity y of this entity
	 * @return velocity y of this entity
	 */
	public float getVelY() {
		this.velY = FancyMath.clamp(this.velY, -this.maxVel, this.maxVel);
		return this.velY;
	}

	/**
	 * gets the width of this entity
	 * @return width of this entity
	 */
	public long getWidth() {
		return this.width;
	}

	/**
	 * gets the height of this entity
	 * @return height of this entity
	 */
	public long getHeight() {
		return this.height;
	}

	/**
	 * gets the z-index of this entity
	 * z-index can be used to sort entities, higher z-indexs will be drawn on top of smaller ones
	 * @return z-index of this entity
	 */
	public long getZIndex() {
		return this.zIndex;
	}

	/**
	 * sets the x position of this entity
	 * @param x new x of this entity
	 */
	public void setX(long x){
		this.lastX = this.x;
		this.x = x;
		this.moved = true;
	}

	/**
	 * sets the y position of this entity
	 * @param y new y of this entity
	 */
	public void setY(long y){
		this.lastY = this.y;
		this.y = y;
		this.moved = true;
	}

	/**
	 * sets the maximum velocity of this entity
	 * @param maxVel new maximum velocity of this entity
	 */
	public void setMaxVel(float maxVel) {
		this.maxVel = maxVel;
	}

	/**
	 * sets the x of this entity
	 * @param velX new velocity x of this entity
	 */
	public void setVelX(float velX){
		this.lastVelX = this.velX;
		this.velX = velX;
		this.velX = FancyMath.clamp(this.velX, -this.maxVel, this.maxVel);
		this.moved = true;
	}

	/**
	 * sets the velocity y of this entity
	 * @param velY new velocity y of this entity
	 */
	public void setVelY(float velY){
		this.lastVelY = this.velY;
		this.velY = velY;
		this.velY = FancyMath.clamp(this.velY, -this.maxVel, this.maxVel);
		this.moved = true;
	}

	/**
	 * sets the width position of this entity
	 * @param width new width of this entity
	 */
	public void setWidth(long width){
		this.width = width;
	}

	/**
	 * sets the height position of this entity
	 * @param height new height of this entity
	 */
	public void setHeight(long height){
		this.height = height;
	}

	/**
	 * sets the z-index of this entity
	 * z-index can be used to sort entities, higher z-indexs will be drawn on top of smaller ones
	 * @param zIndex new z-index of this entity
	 */
	public void setZIndex(long zIndex){
		this.lastZIndex = this.zIndex;
		this.zIndex = zIndex;
		this.moved = true;
	}

	/**
	 * moves the entity; add plusx to the x, and plusy to the y
	 * @param plusx amount to add to x of this entity
	 * @param plusy amount to add to y of this entity
	 */
	public void move(long plusx, long plusy) {
		this.lastX = this.x;
		this.lastY = this.y;
		this.x += plusx;
		this.y += plusy;
		this.moved = true;
	}

	/**
	 * gets the current EntityType of this entity
	 * @return current EntityType
	 */
	public EntityRegistry.EntityType getType() {
		return this.type;
	}

	/**
	 * sets the current EntityType of this entity
	 * @param type new type; must be created using EntityRegistry.register()
	 */
	public void setType(EntityRegistry.EntityType type) {
		this.type = type;
	}

	/**
	 * abstract onCreate() method, called when Scene initializes this entity
	 */
	public abstract void onCreate();

	/**
	 * does checks on the entity to see whether or not to emit certain Events
	 */
	public void doChecks() {
		if (this.moved) {
			if (this.sceneManager.getGame().getEventBus().emit(new EntityMovedEvent(this))) {
				System.out.println("cancel");
				this.x = this.lastX;
				this.y = this.lastY;
				this.zIndex = this.lastZIndex;
			}
			this.moved = false;
		}
	}

	/**
	 * abstract onUpdate() method, called multiple times a second in the gameThread
	 */
	public abstract void onUpdate();

	/**
	 * abstract onDraw() method, called multiple times a second in the gameThread
	 * @param sg Scenic Graphics object, for drawing
	 */
	public abstract void onDraw(ScenicGraphics sg);

	/**
	 * gets the Scene Manager of this entity
	 * @return the scene manager of this entity
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}

	/**
	 * adds an entity component to this entity
	 * @param component component to add to entity
	 * IMPORTANT NOTE! Components are stored as the name of the class of the component to uppercase form e.g. EntityComponent -> "ENTITYCOMPONENT"
	 */
	public void addComponent(EntityComponent component) {
		this.entityComponents.put(component.getClass().getSimpleName().toUpperCase(), component);
		component.onCreate();
	}

	/**
	 * removes an entity component from this entity
	 * @param component component to remove from the entity
	 * IMPORTANT NOTE! Components are stored as the name of the class of the component to uppercase form e.g. EntityComponent -> "ENTITYCOMPONENT"
	 */
	public void removeComponent(Class<? extends EntityComponent> component) {
		this.entityComponents.get(component.getSimpleName().toUpperCase()).onExit();
		this.entityComponents.remove(component.getSimpleName().toUpperCase());
	}

	/**
	 * removes an entity component from this entity
	 * @param componentName component id to remove from this entity
	 * IMPORTANT NOTE! Components are stored as the name of the class of the component to uppercase form e.g. EntityComponent -> "ENTITYCOMPONENT"
	 */
	public void removeComponent(String componentName) {
		this.entityComponents.get(componentName).onExit();
		this.entityComponents.remove(componentName);
	}

	/**
	 * gets an entity component from this entity
	 * @param component component to get from this entity
	 * IMPORTANT NOTE! Components are stored as the name of the class of the component to uppercase form e.g. EntityComponent -> "ENTITYCOMPONENT"
	 */
	public EntityComponent getComponent(Class<? extends EntityComponent> component) {
		return this.entityComponents.get(component.getSimpleName().toUpperCase());
	}

	/**
	 * gets an entity component from this entity
	 * @param componentName component id to get from this entity
	 * IMPORTANT NOTE! Components are stored as the name of the class of the component to uppercase form e.g. EntityComponent -> "ENTITYCOMPONENT"
	 */
	public EntityComponent getComponent(String componentName) {
		return this.entityComponents.get(componentName);
	}

	/**
	 * if the entity has been initialized
	 * @return whether or not the entity has been initialized
	 */
    public boolean created() {
		return this.init;
	}

	/**
	 * calls the onCreate() method if it hasn't been called
	 */
	public void callCreate() {
    	if (!this.init) {
    		this.sceneManager.getGame().getEventBus().emit(new EntityCreateEvent(this));
			this.init = true;
    		this.onCreate();
		}
	}

	public void callExit() {
		if (this.init) {
			this.sceneManager.getGame().getEventBus().emit(new EntityExitEvent(this));
			for (EntityComponent component : entityComponents.values()) {
				component.onExit();
			}
			this.init = false;
			this.onExit();
		}
	}
}
