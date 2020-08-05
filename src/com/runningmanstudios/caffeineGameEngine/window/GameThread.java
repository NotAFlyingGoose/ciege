/**
 * com.runningmanstudios.caffeineGameEngine.window contains the main classes' of the engine
 * @see com.runningmanstudios.caffeineGameEngine.window.Game
 */
package com.runningmanstudios.caffeineGameEngine.window;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.events.DrawEvent;
import com.runningmanstudios.caffeineGameEngine.events.TickEvent;
import com.runningmanstudios.caffeineGameEngine.events.entity.req.EntityDrawEvent;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.util.FancyMath;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

/**
 * Game Thread, heart of the engine
 * @EngineOnly
 * Game thread is not meant to be instanced or referenced other than the Engine
 * @see Game
 */
public class GameThread extends JPanel implements Runnable {
	private final Set<AbstractEntity> entityUpdates = new HashSet<>();
	private List<AbstractEntity> entityUpdateRequests = new ArrayList<AbstractEntity>();
	private final Game game;
	public boolean running = false;
	public Game.FPSDisplayMode fpsDisplayMode;
    public double delta = 0.0;
	private ScenicGraphics sg;
	private int fillBlackspace;
	private int targetFPS = 64;

	/**
	 * Creates a game thread
	 * @param game Game object
	 * @param fpsDisplayMode How to display the fps detailed description in Game
	 */
	public GameThread(Game game, Game.FPSDisplayMode fpsDisplayMode) {
		this.game = game;
		this.fpsDisplayMode = fpsDisplayMode;
		this.setFocusable(true);
	}

	/**
	 * run method for Thread
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000.0 / this.targetFPS;
		int frames = 0;
		int updates = 0;

		while (this.running) {
			long now = System.nanoTime();
			this.delta += (now - lastTime) / ns;
			lastTime = now;

			//render and update
			if (this.delta >= 1){
				this.update();
				updates++;
				this.render();
				frames++;
				this.delta--;
			}

			//every second
			if (System.currentTimeMillis() - timer > 1000){
				long mem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				if (this.fpsDisplayMode == Game.FPSDisplayMode.TITLE_BAR || this.fpsDisplayMode == Game.FPSDisplayMode.BOTH) {
					this.game.addToTitle(" | " + frames + " fps");
				}
				if (this.fpsDisplayMode == Game.FPSDisplayMode.CONSOLE || this.fpsDisplayMode == Game.FPSDisplayMode.BOTH){
					String rps = "";
					NumberFormat nf = NumberFormat.getInstance();
					double kb = mem / 100.0;
					boolean useKb = false;
					rps = String.format("%-10s|%17s%17s    |    %s", frames + " fps", "memory usage ", useKb ? nf.format(FancyMath.getNumber(kb)) + FancyMath.getDecimalString(kb, true): mem + (useKb?" kb":" bytes"), "delta " + this.delta);
					GameLogger.info(rps);
				}
				if (frames < this.targetFPS-1) {
					GameLogger.warn("Running " + (this.targetFPS - frames) + " ticks behind!");
				}
				ns = 1000000000.0 / this.targetFPS;
				timer += 1000;
				frames = 0;
				updates = 0;
			}
		}
		
	}

	/**
	 * update method
	 * calls update for the Current Scene and all Entities in that Scene
	 */
	public void update() {
		if (!this.game.getEventBus().emit(new TickEvent(this.game))) {
			try {
				if (!this.isCurrentScreenNull()) {
					if (this.game.getSceneManager().getCurrentScene().getCamera() != null) {
						if (!this.game.getSceneManager().getCurrentScene().getCamera().init) {
							this.game.getSceneManager().getCurrentScene().getCamera().onCreate();
						}
						this.game.getSceneManager().getCurrentScene().getCamera().onUpdate();
					}
					if (this.game.getSceneManager().isSceneInitialized()) {
						this.game.getSceneManager().getCurrentScene().initEntities();
						this.game.getSceneManager().getCurrentScene().onUpdate();
						this.game.getSceneManager().getCurrentScene().updateEntities();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * render method
	 * calls repaint
	 */
	public void render() {
		this.repaint();
	}

	/**
	 * paint method
	 * creates a BufferedImage, calls the onDraw() for the Current Scene, and all Entities inside that Scene
	 * then draws the BufferedImage to the screen.
	 */
	public void paint(Graphics g){
		BufferedImage gameGraphics = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

		// Create a graphics which can be used to draw into the buffered image
		Graphics2D g2d = gameGraphics.createGraphics();
		if (!this.game.getEventBus().emit(new DrawEvent(g2d))) {
			this.sg = new ScenicGraphics(g2d);
			if (this.sg.antialias) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			if (this.sg.antialiasText) {
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			if (this.sg.normalization) {
				g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			}
			if (!this.isCurrentScreenNull()) {
				if (this.game.getSceneManager().getCurrentScene().getCamera() != null) {
					g2d.translate(-(int) this.game.getSceneManager().getCurrentScene().getCamera().getX(), -(int) this.game.getSceneManager().getCurrentScene().getCamera().getY());
				}

				this.game.getSceneManager().getCurrentScene().onDraw(this.sg);

				this.entityUpdateRequests = sortEntities(this.entityUpdateRequests);
				for (AbstractEntity entity : this.entityUpdateRequests) {
					if (this.game.getSceneManager().getCurrentScene().entityExists(entity)) {
						if (!this.game.getEventBus().emit(new EntityDrawEvent(entity, this.sg))) {
							entity.onDraw(this.sg);
						}
					}
				}

				if (this.game.getSceneManager().getCurrentScene().getCamera() != null) {
					g2d.translate((int) this.game.getSceneManager().getCurrentScene().getCamera().getX(), (int) this.game.getSceneManager().getCurrentScene().getCamera().getY());
				}
			}
			if (this.fillBlackspace > 0) {
				g2d.setColor(Color.black);
				g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
				this.fillBlackspace--;
			}
		}
		g.drawImage(gameGraphics, 0, 0, this.getWidth(), this.getHeight(), this);
		g.dispose();
	}

	public static List<AbstractEntity> sortEntities(List<AbstractEntity> entities) {
		List<AbstractEntity> entityList = new ArrayList<>(entities);

		entityList.sort(Comparator.comparing(AbstractEntity::getZIndex));

		return new ArrayList<>(entityList);
	}
	
	public boolean isCurrentScreenNull() {
		return this.game.getSceneManager().getCurrentScene() == null;
	}
	
	public void addUpdates(AbstractEntity objecttoadd) {
		this.entityUpdateRequests.add(objecttoadd);
	}
	
	public void removeUpdates(AbstractEntity objecttoadd) {
		this.entityUpdateRequests.remove(objecttoadd);
	}

	public void fillBlackspace() {
		this.fillBlackspace =2;
	}

	public void setTargetFPS(int targetFPS) {
		this.targetFPS=targetFPS;
	}
}
