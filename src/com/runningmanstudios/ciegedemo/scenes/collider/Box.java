package com.runningmanstudios.ciegedemo.scenes.collider;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.components.SimpleCollider;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;

public class Box extends AbstractEntity {
    SimpleCollider sc = new SimpleCollider(this);
    float _acc = 1f;
    float _dcc = 0.2f;
    public Box(int startX, float velXMultiplier, float velYMultiplier, float maxVel, SceneManager sceneManager) {
        super(startX, 100, 200, 200, sceneManager);
        this.setVelX(this._acc *velXMultiplier);
        this.setVelY(this._acc *velYMultiplier);
        this.setMaxVel(maxVel);
    }

    @Override
    public void onCreate() {
        this.addComponent(this.sc);
    }

    @Override
    public void onUpdate() {
        this.move((int) this.getVelX(), (int) this.getVelY());

        if (this.getX()+ this.getWidth()>= this.getSceneManager().getGame().getWidth()) {
            this.setVelX(this.getVelX()- this._dcc);
        }
        if (this.getX()<=0) {
            this.setVelX(this.getVelX()+ this._dcc);
        }

        if (this.getY()+ this.getHeight()>= this.getSceneManager().getGame().getHeight()) {
            this.setVelY(this.getVelY()- this._dcc);
        }
        if (this.getY()<=0) {
            this.setVelY(this.getVelY()+ this._dcc);
        }

        this.sc.MoveWithCollisions();
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.white);
        sg.displayRect(this.sc.getLiteralBounds(), false, false);
    }
}
