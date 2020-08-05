package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.entities.basic.Button;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.ciegedemo.scenes.platform.Platform;

import java.awt.*;

public class Demo extends Scene {
    Button backbtn;
    Button grassworldbtn;
    Button platformbtn;
    Button otherbtn;
    public Demo(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        this.backbtn = new Button(this.getSceneManager().getGame().getWidth()/2, 50, 200, 50, this.getSceneManager()) {
            final String text = "BACK";
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
                this.getSceneManager().switchScene(new Menu(this.getSceneManager()));
            }
        };
        this.grassworldbtn = new Button(this.getSceneManager().getGame().getWidth()/2, 50, 200, 50, this.getSceneManager()) {
            final String text = "GRASS WORLD";
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
                this.getSceneManager().switchScene(new Forest(this.getSceneManager()));
            }
        };
        this.platformbtn = new Button(this.getSceneManager().getGame().getWidth()/2, 50, 200, 50, this.getSceneManager()) {
            final String text = "PLATFORMING";
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
                this.getSceneManager().switchScene(new Platform(this.getSceneManager()));
            }
        };
        this.otherbtn = new Button(this.getSceneManager().getGame().getWidth()/2, 50, 200, 50, this.getSceneManager()) {
            final String text = "OTHER";
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
                this.getSceneManager().switchScene(new Randomness(this.getSceneManager()));
            }
        };
        this.grassworldbtn.move(0, 100);
        this.platformbtn.move(0, 175);
        this.otherbtn.move(0, 250);
        this.addEntityToScene(this.backbtn);
        this.addEntityToScene(this.grassworldbtn);
        this.addEntityToScene(this.platformbtn);
        this.addEntityToScene(this.otherbtn);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.white);
        sg.displayRect(0,0, this.getSceneManager().getGame().getWidth(), this.getSceneManager().getGame().getHeight(),true,false);
    }
}
