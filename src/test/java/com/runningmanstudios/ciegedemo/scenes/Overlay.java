package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.basic.Button;
import com.runningmanstudios.caffeineGameEngine.entities.camera.AbstractCamera;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Overlay extends AbstractEntity {
    public Button menu;
    public Button cancel;
    private boolean show = false;
    /**
     * creates an AbstractEntity with x, y, width and height
     *
     * @param sceneManager Scene Manager
     */
    public Overlay(SceneManager sceneManager, int z) {
        super(0, 0, sceneManager.getGame().getWidth(), sceneManager.getGame().getHeight(), sceneManager);
        setZIndex(100+z);
    }

    /**
     * abstract onCreate() method, called when Scene initializes this entity
     */
    @Override
    public void onCreate() {
        cancel = new Button(20, 50, 100, 50, getSceneManager(), false) {
            final String text = "CANCEL";
            @Override
            public void onCreate() {}

            @Override
            public void onUpdate() {}

            @Override
            public void onDraw(ScenicGraphics sg) {
                if (this.isHovered()) {
                    sg.setColor(Color.gray);
                } else {
                    sg.setColor(Color.lightGray);
                }

                if (this.isClicked()) {
                    sg.setColor(Color.lightGray);
                }

                sg.displayRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), true, true);

                sg.setColor(Color.darkGray);
                sg.displayText(this.text, this.getX(), this.getY(), true);
            }
            @Override
            public void onAction() {
                show = false;
            }
        };
        menu = new Button(20, 150, 100, 50, getSceneManager(), false) {
            final String text = "GO TO MENU";
            @Override
            public void onCreate() {}

            @Override
            public void onUpdate() {}

            @Override
            public void onDraw(ScenicGraphics sg) {
                if (this.isHovered()) {
                    sg.setColor(Color.gray);
                } else {
                    sg.setColor(Color.lightGray);
                }

                if (this.isClicked()) {
                    sg.setColor(Color.lightGray);
                }

                sg.displayRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), true, true);

                sg.setColor(Color.darkGray);
                sg.displayText(this.text, this.getX(), this.getY(), true);
            }
            @Override
            public void onAction() {
                if (show) {
                    this.getSceneManager().switchScene(new Menu(this.getSceneManager()));
                    show=false;
                }
            }
        };
    }

    public void show() {
        show=true;
    }

    public void hide() {
        show=false;
    }

    @Override
    public void onExit() {
        cancel.onExit();
        menu.onExit();
    }

    /**
     * abstract onUpdate() method, called multiple times a second in the gameThread
     */
    @Override
    public void onUpdate() {
        if (getSceneManager().getGame().getInput().isKeyClicked(KeyEvent.VK_ESCAPE)) show = !show;
        AbstractCamera cam = getSceneManager().getCurrentScene().getCamera();
        if (cam!=null) {
            long xoff = cam.getX();
            long yoff = cam.getY();

            cancel.setX(xoff);
            cancel.setY(yoff);
            cancel.move(100, 100);
            menu.setX(xoff);
            menu.setY(yoff);
            menu.move(100, 200);
            setX(xoff);
            setY(yoff);
        }
    }

    /**
     * abstract onDraw() method, called multiple times a second in the gameThread
     *
     * @param sg Scenic Graphics object, for drawing
     */
    @Override
    public void onDraw(ScenicGraphics sg) {
        if (show) {
            sg.setColor(new Color(50, 50, 50, 155));
            sg.displayRect(getX(), getY(), getWidth(), getHeight(), true, false);
            cancel.onDraw(sg);
            menu.onDraw(sg);
        }
    }
}
