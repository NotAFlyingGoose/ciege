package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBusSubscriber;
import com.runningmanstudios.caffeineGameEngine.entities.basic.Button;
import com.runningmanstudios.caffeineGameEngine.events.TickEvent;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityMovedEvent;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.rendering.text.RichTextFormatter;
import com.runningmanstudios.caffeineGameEngine.util.GameTimer;
import com.runningmanstudios.caffeineGameEngine.util.RepeatingGameTimer;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import java.awt.*;

public class Menu extends Scene {
    RichTextFormatter title = new RichTextFormatter("<b>The Caffeine Game Engine</>");
    RichTextFormatter subtitle = new RichTextFormatter("<b>CIEGE</>");
    Button playbtn;
    Button settingbtn;
    Button aboutbtn;
    transient GameTimer timer;
    transient RepeatingGameTimer reptimer;
    public Menu(SceneManager sceneManager) {
        super(sceneManager);
    }

    @EventBusSubscriber
    public void onTick(EntityMovedEvent t) {

    }

    @Override
    public void onCreate() {
        getSceneManager().getGame().getEventBus().subscribe(this);
        this.playbtn = new Button(this.getSceneManager().getGame().getWidth()/2, this.getSceneManager().getGame().getHeight()/2, 200, 50, this.getSceneManager(), true) {
            final String text = "DEMO";
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
                this.getSceneManager().switchScene(new Demo(this.getSceneManager()));
            }
        };
        this.settingbtn = new Button(this.getSceneManager().getGame().getWidth()/2, this.getSceneManager().getGame().getHeight()/2, 200, 50, this.getSceneManager(), true) {
            final String text = "SETTINGS";
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

            }
        };
        this.aboutbtn = new Button(this.getSceneManager().getGame().getWidth()/2, this.getSceneManager().getGame().getHeight()/2, 200, 50, this.getSceneManager(), true) {
            final String text = "ABOUT";
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
                this.getSceneManager().switchScene(new About(this.getSceneManager()));
            }
        };

        this.settingbtn.move(0, 60);
        this.aboutbtn.move(0, 120);

        this.addEntityToScene(this.playbtn);
        this.addEntityToScene(this.settingbtn);
        this.addEntityToScene(this.aboutbtn);
        this.timer = new GameTimer(7*1000);
        this.timer.resume();
        this.reptimer = new RepeatingGameTimer(5*1000);
        this.reptimer.resume();
    }

    @Override
    public void onUpdate() {
        synchronized (this) {
            if (this.reptimer.isDone()) {
                GameLogger.info("repeating timer example");
            }
            if (this.timer.isDone()) {
                this.timer.kill();
                GameLogger.info("timer example");
            }
        }
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.setColor(Color.white);
        sg.displayRect(0,0, this.getSceneManager().getGame().getWidth(), this.getSceneManager().getGame().getHeight(),true,false);

        sg.setColor(Color.darkGray);
        sg.setFontSize(50);
        sg.displayText(this.title, this.getSceneManager().getGame().getWidth()/2, 100, true);
        sg.setFontSize(35);
        sg.displayText(this.subtitle, this.getSceneManager().getGame().getWidth()/2, 150, true);
        sg.resetFont();
    }

    /**
     * abstract onExit() method, called when Scene Manager unloads this scene
     */
    @Override
    public void onExit() {
        getSceneManager().getGame().getEventBus().unsubscribe(this);
    }
}
