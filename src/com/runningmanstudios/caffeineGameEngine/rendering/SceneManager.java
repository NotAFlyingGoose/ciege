/**
 * com.runningmanstudios.caffeineGameEngine.rendering contains classes for rendering things to the screen
 * @see com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics
 */
package com.runningmanstudios.caffeineGameEngine.rendering;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.SceneException;
import com.runningmanstudios.caffeineGameEngine.events.scene.SceneReloadEvent;
import com.runningmanstudios.caffeineGameEngine.events.scene.SceneSwitchEvent;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Scene Manager manages scenes for a Game object
 * It is for setting the current Scene visible on the screen.
 *
 * @EngineOnly Do not create a scene manager or attempt to add a new SceneManager to a Game, Game objects will automatically create a SceneManager
 */
public class SceneManager implements Serializable {

	private Game game;
	private boolean sceneInitialized = false;
	private Scene scene;

	/**
	 * create a SceneManager
	 * @param game Game object, if game already has a SceneManager, will throw an error.
	 */
	public SceneManager(Game game) {
		if (game.getSceneManager() == null) this.game = game;
		else try {
			throw new SceneException("Game  ->  \"" + game + "\" <-\nGame already has SceneManager");
		} catch (SceneException e) {
			e.printStackTrace();
		}
	}

	/**
	 * switch the current viewable scene
	 * @param scene scene to switch to
	 */
	public void switchScene(Scene scene) {
		if (!this.game.getEventBus().emit(new SceneSwitchEvent(this.scene, scene))) {
			this.sceneInitialized = false;
			this.scene = scene;
			this.scene.onCreate();
			this.sceneInitialized = true;
		}
	}

	/**
	 * reloads the current scene
	 */
	public void reloadScene() {
		try {
			Scene reloadScene = this.scene.getClass().getDeclaredConstructor(SceneManager.class).newInstance(this);
			if (!this.game.getEventBus().emit(new SceneReloadEvent(reloadScene))) {
				this.sceneInitialized = false;
				this.scene = reloadScene;
				this.scene.onCreate();
				this.sceneInitialized = true;
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the current viewable scene
	 * @return the current scene
	 */
	public Scene getCurrentScene() {
		return this.scene;
	}

	/**
	 * get the game object this Scene Manager is linked to
	 * @return game object
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * is the current scene initialized or not
	 * @return whether onCreate() has been called in the current scene
	 */
	public boolean isSceneInitialized() {
		return this.sceneInitialized;
	}
}
