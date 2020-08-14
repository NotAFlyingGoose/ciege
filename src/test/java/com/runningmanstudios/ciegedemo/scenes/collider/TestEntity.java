package com.runningmanstudios.ciegedemo.scenes.collider;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.components.SimpleCollider;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestEntity extends AbstractEntity {
    float _acc = 1f;
    float _dcc = 0.5f;
    SimpleCollider sc = new SimpleCollider(this);
    public TestEntity(SceneManager sceneManager) {
        super(100, 100, 32, 32, sceneManager);
    }

    @Override
    public void onCreate() {
        this.addComponent(this.sc);
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onUpdate() {
        this.move((long) this.getVelX(), (long) this.getVelY());

        //vertical
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_W)) this.setVelY(this.getVelY()- this._acc);
        else if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_S)) this.setVelY(this.getVelY()+ this._acc);
        else if (!this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_W) && !this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_S)) {
            if (this.getVelY() > 0) this.setVelY(this.getVelY()- this._dcc);
            else if (this.getVelY() < 0) this.setVelY(this.getVelY()+ this._dcc);
        }

        //horizontal
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_A)) this.setVelX(this.getVelX()- this._acc);
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_D)) this.setVelX(this.getVelX()+ this._acc);
        else if (!this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_A) && !this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_D)) {
            if (this.getVelX() > 0) this.setVelX(this.getVelX()- this._dcc);
            else if (this.getVelX() < 0) this.setVelX(this.getVelX()+ this._dcc);
        }

        this.sc.MoveWithCollisions();
    }

    @Override
    public void onDraw(ScenicGraphics sg) {

        sg.setColor(Color.white);
        sg.displayRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), true, false);
    }
}
