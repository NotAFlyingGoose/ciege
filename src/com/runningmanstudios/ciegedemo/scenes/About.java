package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.entities.basic.Button;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;

import java.awt.*;

public class About extends Scene {
    Button backbtn;
    public About(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        this.backbtn = new Button(this.getSceneManager().getGame().getWidth()/2, 50, 200, 50, this.getSceneManager()) {
            final String text = "BACK";
            @Override
            public void onCreate() {}

            @Override
            public void onUpdate() {

            }

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
        this.addEntityToScene(this.backbtn);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.white);
        sg.displayRect(0,0, this.getSceneManager().getGame().getWidth(), this.getSceneManager().getGame().getHeight(),true,false);

        sg.setColor(Color.darkGray);
        sg.setFontStyle(Font.BOLD);
        sg.setFontSize(50);
        sg.displayText("The Caffeine Game Engine", this.getSceneManager().getGame().getWidth()/2, 100, true);
        sg.setFontSize(35);
        sg.displayText("About", this.getSceneManager().getGame().getWidth()/2, 150, true);
        sg.setFontSize(15);
        String content = "The Caffeine Game Engine, or ciege is my baby, my honey pie, i made it and i love it. it's really cool. it has some cul stuff in it, like this text display for example; this text right here on the screen has no line breaks, but the ScenicGraphics class that I made calculates where to put the linebreaks. I named it the caffeine game engine because it is a library for java. it is designed to make stuff easier.";
        sg.displayText(content, this.getSceneManager().getGame().getWidth()/2, 200, 500,200,true);
    }
}
