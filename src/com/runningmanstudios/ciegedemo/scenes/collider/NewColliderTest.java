package com.runningmanstudios.ciegedemo.scenes.collider;

import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;
import java.awt.event.KeyEvent;

public class NewColliderTest extends Scene {
    TestEntity e;
    Box b1;
    Box b2;
    Box b3;
    Box b4;
    Box b5;
    /**
     * Creates a Scene. requires a Scene Manager
     *
     * @param sceneManager Scene Manager
     */
    public NewColliderTest(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        this.e = new TestEntity(this.getSceneManager());
        this.b1 = new Box(0, 3, 6, 5f, this.getSceneManager());
        this.b2 = new Box(this.getSceneManager().getGame().getWidth()-50, 2, 5, 13.25f, this.getSceneManager());
        this.b3 = new Box(this.getSceneManager().getGame().getWidth()/6, 3, 1.0f, 14, this.getSceneManager());
        this.b4 = new Box(this.getSceneManager().getGame().getWidth()/2, 6, 3, 8, this.getSceneManager());
        this.b5 = new Box(this.getSceneManager().getGame().getWidth()/3, 3, 5, 16, this.getSceneManager());
        this.addEntityToScene(this.e);
        this.addEntityToScene(this.b1);
        this.addEntityToScene(this.b2);
        this.addEntityToScene(this.b3);
        this.addEntityToScene(this.b4);
        this.addEntityToScene(this.b5);
    }

    @Override
    public void onUpdate() {
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_R)) {
            this.getSceneManager().reloadScene();
        }
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.black);
        sg.displayRect(0, 0, this.getSceneManager().getGame().getWidth(), this.getSceneManager().getGame().getHeight(), true, false);
    }
}
