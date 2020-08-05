package com.runningmanstudios.caffeineGameEngine.rendering;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera;
import com.runningmanstudios.caffeineGameEngine.events.entity.req.EntityUpdateEvent;
import com.runningmanstudios.caffeineGameEngine.events.scene.entity.SceneEntityAddedEvent;
import com.runningmanstudios.caffeineGameEngine.events.scene.entity.SceneEntityRemovedEvent;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Displayable Scene controlled by SceneManager
 */
public abstract class Scene implements Serializable {
	private final SceneManager sceneManager;
	private final Set<AbstractEntity> entities = new HashSet<>();
	private AbstractCamera camera;

	/**
	 * Creates a Scene. requires a Scene Manager
	 * @param sceneManager Scene Manager
	 */
	public Scene(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	/**
	 * add entity to list of entities, for calling entity methods
	 * @param entity entity to add
	 */
	public void addEntityToScene(AbstractEntity entity) {
		if (!this.sceneManager.getGame().getEventBus().emit(new SceneEntityAddedEvent(this, entity))) {
			this.entities.add(entity);
			this.sceneManager.getGame().addObjectUpdate(entity);
		}
	}

	/**
	 * removes entity from list of entities, for disabling entity methods
	 * @param entity entity to remove
	 */
	public void removeEntityFromScene(AbstractEntity entity){
		if (!this.sceneManager.getGame().getEventBus().emit(new SceneEntityRemovedEvent(this, entity))) {
			this.entities.remove(entity);
			this.sceneManager.getGame().removeObjectUpdate(entity);
		}
	}

	/**
	 * checks to see if the scene's entities list has this entity
	 * @param entity entity to check
	 * @return whether or not the scene has an instance of that entity
	 */
	public boolean entityExists(AbstractEntity entity){
		return this.entities.contains(entity);
	}

	/**
	 * check if entity has been initialized yet
	 * @param entity entity to check
	 * @return whether or not the entity has been initialized yet
	 */
	public boolean entityInitialized(AbstractEntity entity){
		if (this.entityExists(entity)){
			for (AbstractEntity e : this.entities) {
				if (e.equals(entity)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * get all the entities added to the scene
	 * @return all entities
	 */
	public Set<AbstractEntity> getEntitiesInScene(){
		return this.entities;
	}

	/**
	 * call the onCreate() method of all entities
	 */
	public void initEntities() {
		for (AbstractEntity entity : this.entities) {
			if (!entity.created()) {
				entity.callCreate();
			}
		}
	}

	/**
	 * call the onUpdate() method of all entities
	 */
	public void updateEntities() {
		for (AbstractEntity entity : this.entities) {
			if (!this.sceneManager.getGame().getEventBus().emit(new EntityUpdateEvent(entity))) {
				entity.onUpdate();
			}
			entity.doChecks();
		}
	}

	/**
	 * set the position of all entities in a scene
	 * @param x where to set the x
	 * @param y where to set the y
	 */
	public void setEntitiesPos(long x, long y) {
		for (AbstractEntity entity : this.entities) {
			entity.setX(x);
			entity.setY(y);
		}
	}

	/**
	 * move all entities in the scene
	 * @param x how much to move the x (entitiesCurrentX + x)
	 * @param y how much to move the y (entitiesCurrentY + y)
	 */
	public void moveEntities(long x, long y) {
		for (AbstractEntity entity : this.entities) {
			entity.setX(entity.getX() + x);
			entity.setY(entity.getY() + y);
		}
	}

	/**
	 * get a pixels distance from all entity
	 * @param x x position of pixel
	 * @param y y position of pixel
	 * @return average distance from all entities
	 */
	public double getDistFromEntity(int x, int y) {
		double avgdist = 0;
		for (AbstractEntity entity : this.entities) {
			double dist=Math.sqrt((x-entity.getX())*(x-entity.getX()) + (y-entity.getY())*(y-entity.getY()));
			avgdist += dist;
		}
		avgdist /= this.entities.size();
		return avgdist;
	}

	/**
	 * get the Scene Manager of this scene
	 * @return the scene manager
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}

	/**
	 * get the current Scene camera
	 * @return the current camera
	 */
	public AbstractCamera getCamera() {
		return this.camera;
	}

	/**
	 * Set the camera of the scene
	 * @param camera camera to get coordinates from
	 */
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
	}

	/**
	 * abstract onCreate() method, called when Scene Manager switches to this scene
	 */
	public abstract void onCreate();

	/**
	 * abstract onUpdate() method, called multiple times a second in the gameThread
	 */
	public abstract void onUpdate();

	/**
	 * abstract onDraw() method, called multiple times a second in the gameThread
	 * @param sg Scenic Graphics object, for drawing
	 */
	public abstract void onDraw(ScenicGraphics sg);

}
