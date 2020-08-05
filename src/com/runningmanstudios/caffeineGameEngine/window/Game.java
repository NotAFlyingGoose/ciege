/**
 * com.runningmanstudios.caffeineGameEngine.window contains the main classes of the engine
 * @see com.runningmanstudios.caffeineGameEngine.window.Game
 */
package com.runningmanstudios.caffeineGameEngine.window;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.GameBuilder;
import com.runningmanstudios.caffeineGameEngine.checks.event.EventBus;
import com.runningmanstudios.caffeineGameEngine.checks.exceptions.GameBuilderException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.input.Input;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.style.Style;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game class, instance to create a new window
 *
 * if a method is @EngineOnly it is only meant to be used by the engine
 * @author NotAFlyingGoose
 * @see <a href="http://www.runningmanstudios.com/ciege/">Ciege Documentation</a>
 */
public class Game implements Serializable {
	private static boolean firstGame = true;

	public final String VERSION = "1.0.0";
	private final EventBus eventBus;
	private final JFrame window = new JFrame();
	private final SceneManager sceneManager;
	private transient final GameThread gameThread;
	private final Input userInput;

	private GameBuilder builder;
	private File gameFolder;
	private File resourcesFolder;
	private int WIDTH;
	private int HEIGHT;
	private String TITLE;
	private boolean hideResize;

	/**
	 * Creates a window and a game folder if not already created
	 * @param windowWidth the width of the window
	 * @param windowHeight the height of the window
	 * @param title the title of the window
	 */
	public Game(int windowWidth, int windowHeight, String title) {
		try {
			Class klass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
			if (klass.isAnnotationPresent(GameBuilder.class)){
				this.builder = (GameBuilder) klass.getAnnotation(GameBuilder.class);
			} else {
				throw new GameBuilderException("Class  ->  \"" + klass.getSimpleName() + "\" <-\nClass creates Game without having @GameBuilder");
			}
		} catch (ClassNotFoundException | GameBuilderException e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (this.builder.makeFolder()) {
			this.gameFolder = new File(System.getProperty("user.home") +System.getProperty("file.separator")+ ".ciege"+System.getProperty("file.separator")+"games"+System.getProperty("file.separator") + this.builder.id() + System.getProperty("file.separator") + (this.builder.versionFolder()?this.builder.version():""));
			this.gameFolder.mkdirs();
			this.resourcesFolder = new File(this.gameFolder.getAbsolutePath()+System.getProperty("file.separator")+"resources"+System.getProperty("file.separator"));
			this.resourcesFolder.mkdirs();
			File data = new File(this.gameFolder.getAbsolutePath()+System.getProperty("file.separator")+"info.json");
			if (!data.exists()) {
				try {
					data.createNewFile();
					FileWriter fw = new FileWriter(data);
					fw.write("{\n  \"game\": {\n    \"id\": \""+this.builder.id()+"\",\n    \"name\": \""+title+"\"\n  },\n  \"author\": {\n    \"id\": \""+this.builder.author().toLowerCase().replace(" ", "")+"\",\n    \"name\": \""+this.builder.author()+"\"\n  }\n}");
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (firstGame) {
			firstGame=false;
			this.printWelcomeMessage();
		}
		this.printStartUpInfo();
		this.WIDTH = windowWidth;
		this.HEIGHT = windowHeight;
		this.TITLE = title;
		this.window.setSize(windowWidth, windowHeight);
		this.window.setResizable(false);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setFocusable(true);
		this.window.setLocationRelativeTo(null);
		this.window.setTitle(title);

		this.eventBus = new EventBus();
		this.sceneManager = new SceneManager(this);
		this.gameThread = new GameThread(this, FPSDisplayMode.HIDE);
		this.userInput = new Input(this);

		this.window.add(this.gameThread);
		this.window.addKeyListener(this.userInput);
		this.window.addMouseListener(this.userInput);
		this.window.addMouseMotionListener(this.userInput);

		this.gameThread.running = true;
		new Thread(this.gameThread, "GameThread").start();
	}

	/**
	 * make the screen show black if the user resizes
	 * IMPORTANT: will not work if setResizable = false
	 * @param show whether to show black or not
	 */
	public void hideResize(boolean show) {
		this.hideResize = show;
	}

	/**
	 * if the user can resize the window
	 * @param resizeable whether to make the window resizable or not
	 */
	public void setResizable(boolean resizeable) {
		this.window.setResizable(resizeable);
		if (resizeable) {
			this.window.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent componentEvent) {
					if (Game.this.hideResize) {
						Game.this.gameThread.fillBlackspace();
					}
				}
			});
		}
	}

	/**
	 * sets the title of the window
	 * @param title new title
	 */
	public void setTitle(String title) {
		this.TITLE = title;
		this.window.setTitle(title);
	}

	/**
	 * gets the game folder created for this game instance
	 * @return game file
	 */
	public File getGameFolder() {
		return this.gameFolder;
	}

	/**
	 * gets the game's resources folder folder created for this game instance
	 * @return game file
	 */
	public File getResourcesFolder() {
		return this.resourcesFolder;
	}

	/**
	 * creates a game folder for this game instance
	 * @param folder folder to get
	 * @return the folder created
	 */
	public File createGameFolder(String folder) {
		File other = new File(this.gameFolder.getAbsolutePath()+System.getProperty("file.separator")+folder+System.getProperty("file.separator"));
		other.mkdirs();
		return other;
	}

	/**
	 * creates a resource folder for this game instance
	 * @param folder folder to create
	 * @return the folder created
	 */
	public File createResourcesFolder(String folder) {
		File other = new File(this.resourcesFolder.getAbsolutePath()+System.getProperty("file.separator")+folder+System.getProperty("file.separator"));
		other.mkdirs();
		return other;
	}

	/**
	 * gets the game's builder
	 * @return game builder
	 */
	public GameBuilder getBuilder() {
		return this.builder;
	}

	/**
	 * adds a string on top of the title (oldTitle + newTitle)
	 * @param title new title to add
	 */
	public void addToTitle(String title) {
		this.window.setTitle(this.TITLE + title);
	}

	/**
	 * sets the size of the window
	 * @param width new width
	 * @param height new height
	 */
	public void setSize(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.window.setSize(width, height);
	}

	/**
	 * how to display the fps indicator
	 * @param mode how to display
	 * TITLE_BAR: adds the fps to the title bar
	 * CONSOLE: prints the fps in the console
	 * BOTH: does both
	 * HIDE: hides the fps
	 */
	public void showFPS(FPSDisplayMode mode) {
		this.gameThread.fpsDisplayMode = mode;
	}

	public void setTargetFPS(int targetFPS) {
		this.gameThread.setTargetFPS(targetFPS);
	}

	/**
	 * make the window fullscreen
	 * IMPORTANT: window must be hidden before changing
	 * @param show make window fullscreen
	 */
	public void fullscreen(boolean show) {
		if (show) {
			this.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.decorate(false);
		} else {
			this.window.setExtendedState(JFrame.NORMAL);
			this.decorate(true);
		}
	}

	/**
	 * show the title bar
	 * IMPORTANT: window must be hidden before changing
	 * @param show show the title bar
	 */
	public void decorate(boolean show) {
		try {
			this.window.setUndecorated(!show);
		} catch (IllegalStateException e) {
			GameLogger.error("remember to hide the window before decorating");
		}
	}

	/**
	 * show the window
	 */
	public void show() {
		this.window.setVisible(true);
	}

	/**
	 * hide the window
	 */
	public void hide() {
		this.window.setVisible(false);
	}

	/**
	 * dispose of the window
	 */
	public void dismiss() {
		this.window.setVisible(false);
		this.window.dispose();
	}

	/**
	 * get the EventBus for this Game Instance
	 * @return the EventBus
	 */
	public EventBus getEventBus() {
		return this.eventBus;
	}

	/**
	 * Get the delta time.
	 * @return the delta time
	 */
	public double getDelta() {
        return this.gameThread.delta;
    }

	/**
	 * Get User input.
	 * @return the input class
	 */
	public Input getInput() {
		return this.userInput;
	}

	/**
	 * Get the scene manager
	 * @return the Scene Manager
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}

	/**
	 * Get window Width
	 * @return the width of the screen
	 */
	public int getWidth() {
		return this.window.getWidth();
	}

	/**
	 * Get window Height
	 * @return the height of the screen
	 */
	public int getHeight() {
		return this.window.getHeight();
	}

	/**
	 * adds an entity to the list of entities that require updates
	 * @param entity entity to add
	 */
	public void addObjectUpdate(AbstractEntity entity) {
		this.gameThread.addUpdates(entity);
	}

	/**
	 * removes an entity from the list of entities that require updates
	 * @param entity entity to remove
	 */
	public void removeObjectUpdate(AbstractEntity entity) {
		this.gameThread.removeUpdates(entity);
	}

	private void printWelcomeMessage() {
		GameLogger.console.println("Caffeine Game Engine v" + this.VERSION, new Color(200, 255, 155));
		GameLogger.console.println("        /~~~~~~~~~~~~~~~~~~~~/|", Style.getColor("silver"));
		GameLogger.console.println("       /            /######/ /|", Style.getColor("silver"));
		GameLogger.console.println("      /            /______/ / |", Style.getColor("silver"));
		GameLogger.console.println("     =======================/||", Style.getColor("silver"));
		GameLogger.console.println("     |CAFFEINE GAME ENGINE|/ ||", Style.getColor("silver"));
		GameLogger.console.println("      |  \\****/     \\__,,__/ ||", Style.getColor("silver"));
		GameLogger.console.println("      |===\\**/       __,,__  ||", Style.getColor("silver"));
		GameLogger.console.println("      |______________\\====/%_||", Style.getColor("silver"));
		GameLogger.console.println("      |   ___        /~~~~\\ % /|", Style.getColor("silver"));
		GameLogger.console.println("     _|  |===|===   /      \\%/ |", Style.getColor("silver"));
		GameLogger.console.println("    | |  |###|     |########| /", Style.getColor("silver"));
		GameLogger.console.println("    |____\\###/______\\######/|/", Style.getColor("silver"));
		GameLogger.console.println("    ~~~~~~~~~~~~~~~~~~~~~~~~", Style.getColor("silver"));
		GameLogger.console.println("█▀▀ █ █▀▀ █▀▀ █▀▀", Style.getColor("brown"));
		GameLogger.console.println("█▄▄ █ ██▄ █▄█ ██▄", Style.getColor("brown"));
		GameLogger.console.println("Thanks for using Ciege! visit our website at https://www.runningmanstudios.com/ciege", new Color(200, 255, 155));
	}

	private void printStartUpInfo() {
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss.n");
		LocalDateTime now = LocalDateTime.now();
		GameLogger.info("Created new Game|"+ date.format(now)+"-"+time.format(now)+"|");
	}

	/**
	 * how to display the fps indicator
	 * TITLE_BAR: adds the fps to the title bar
	 * CONSOLE: prints the fps in the console
	 * BOTH: does both
	 * HIDE: hides the fps
	 */
	public enum FPSDisplayMode {
		TITLE_BAR, CONSOLE, BOTH, HIDE
	}
}
