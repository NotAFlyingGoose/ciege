/**
 * com.runningmanstudios.caffeineGameEngine.Entities.basic contains classes that extend com.runningmanstudios.caffeineGameEngine.Entities.AbstractEntity
 * it is what it sounds like, it contains some default classes in case you don't want to code it yourself
 */
package com.runningmanstudios.caffeineGameEngine.entities.basic;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBusSubscriber;
import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;
import com.runningmanstudios.caffeineGameEngine.events.input.mouse.MouseClickedEvent;
import com.runningmanstudios.caffeineGameEngine.events.input.mouse.MouseInputEvent;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

/**
 * Button detects clicks and hovers
 */
public abstract class Button extends AbstractEntity {
    boolean hover = false;
    boolean click = false;
    boolean centerx = false;

    /**
     * creates a new Button
     * @param x button x
     * @param y button y
     * @param width button width
     * @param height button height
     * @param sceneManager scene manager
     */
    public Button(int x, int y, int width, int height, SceneManager sceneManager, boolean centerx) {
        super(x, y, width, height, sceneManager);
        this.centerx = centerx;
        try {
            this.setType(EntityRegistry.get("BUTTON"));
        } catch (EntityRegistryException entityRegistryException) {
            entityRegistryException.printStackTrace();
        }
        this.getSceneManager().getGame().getEventBus().subscribe(this);
    }

    /**
     * detects clicks
     */
    @EventBusSubscriber
    public void onMouseInputEvent(MouseInputEvent event) {
        MouseEvent me = event.getTarget();
        int mouseX = me.getX();
        int mouseY = me.getY();
        if (this.getSceneManager().getCurrentScene().getCamera() != null) {
            mouseX += this.getSceneManager().getCurrentScene().getCamera().getX();
            mouseY += this.getSceneManager().getCurrentScene().getCamera().getY();
        }

        if (new Rectangle(centerx?(int)(this.getX() - this.getHeight()*2):(int)(this.getX() - this.getHeight()), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight()).contains(new Point(mouseX, mouseY))) {
            this.hover = true;
            this.click = event instanceof MouseClickedEvent;
        } else {
            this.hover = false;
            this.click = false;
        }

        if (this.click) {
            this.onAction();
        }
    }

    /**
     * called when clicked
     */
    public abstract void onAction();

    /**
     * is the button being hovered or not
     * @return whether the button being hovered or not
     */
    public boolean isHovered() {
        return this.hover;
    }

    /**
     * is the button being clicked or not
     * @return whether the button being clicked or not
     */
    public boolean isClicked() {
        return this.click;
    }

    @Override
    public void onExit() {
        this.getSceneManager().getGame().getEventBus().subscribe(this);
    }
}
