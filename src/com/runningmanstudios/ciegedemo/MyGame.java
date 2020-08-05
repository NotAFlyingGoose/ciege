package com.runningmanstudios.ciegedemo;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.GameBuilder;
import com.runningmanstudios.caffeineGameEngine.events.TickEvent;
import com.runningmanstudios.caffeineGameEngine.window.Game;
import com.runningmanstudios.ciegedemo.scenes.Menu;

import java.io.Serializable;

@GameBuilder(id="ciegeDemo", version="1.0", author="Runningman Studios", makeFolder=true, versionFolder=true)
public class MyGame implements Serializable {
	private final Game game;

	public MyGame() {
		this.game = new Game(800, 600, "Ciege Demos");
		this.game.setTargetFPS(64);
		this.game.getSceneManager().switchScene(new Menu(this.game.getSceneManager()));
		this.game.showFPS(Game.FPSDisplayMode.CONSOLE);
		this.game.show();

		System.out.println(this.game.getEventBus().emit(new TickEvent(this.game)));
	}
	
	public static void main(String[] args) {
		new MyGame();
	}
}
