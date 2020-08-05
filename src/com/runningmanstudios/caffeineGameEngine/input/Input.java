/**
 * com.runningmanstudios.caffeineGameEngine.input controls user input
 * @see com.runningmanstudios.caffeineGameEngine.input.Input
 */
package com.runningmanstudios.caffeineGameEngine.input;

import com.runningmanstudios.caffeineGameEngine.events.input.KeyboardInputEvent;
import com.runningmanstudios.caffeineGameEngine.events.input.mouse.MouseClickedEvent;
import com.runningmanstudios.caffeineGameEngine.events.input.mouse.MouseMovedEvent;
import com.runningmanstudios.caffeineGameEngine.events.input.mouse.MousePressedEvent;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.awt.event.*;
import java.io.Serializable;

/**
 * input handles keyboard input and mouse input
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener, Serializable {
    private final Game game;
    private int mouseX, mouseY;
    private boolean mouseclicked;
    private boolean turnoffclick;
    private boolean mouseheld;
    private final boolean[] keys = new boolean[256];
    private final boolean[] turnedoffkeys = new boolean[256];

    /**
     * creates an Input object
     * @param game Game object, used for getting the EventBus
     */
    public Input(Game game){
        this.game = game;
    }

    //Detecting Key input

    /**
     * detecting key presses
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if (!this.game.getEventBus().emit(new KeyboardInputEvent(event))) {
            this.keys[event.getKeyCode()] = true;
        }
    }

    /**
     * detecting key releases
     */
    @Override
    public void keyReleased(KeyEvent event) {
        this.keys[event.getKeyCode()] = false;
        this.turnedoffkeys[event.getKeyCode()] = false;
    }

    /**
     * detecting key typed
     */
    @Override
    public void keyTyped(KeyEvent event) {

    }

    /**
     * detect if a key is pressed
     * @param key key number, can use KEY_EVENT.VK_(KEY HERE)
     * @return if the key is pressed or not
     */
    public boolean isKeyPressed(int key) {
        return this.keys[key];
    }

    /**
     * detect if a key is pressed, if so, only return true ONCE
     * @param key key number, can use KEY_EVENT.VK_(KEY HERE)
     * @return if the key is pressed or not
     */
    public boolean isKeyClicked(int key) {
        if (this.keys[key] && !this.turnedoffkeys[key]){
            this.turnedoffkeys[key] = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * detect if a key is released
     * @param key key number, can use KEY_EVENT.VK_(KEY HERE)
     * @return if the key is released or not
     */
    public boolean isKeyReleased(int key) {
        return !this.keys[key];
    }

    //Detecting Mouse input

    /**
     * detecting mouse clicked
     */
    @Override
    public void mouseClicked(MouseEvent event) {
    }

    /**
     * detecting mouse entered
     */
    @Override
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * detecting mouse exited
     */
    @Override
    public void mouseExited(MouseEvent event) {
    }

    /**
     * detecting mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent event) {
        try {
            boolean cancel = false;
            if (!this.turnoffclick) {
                if (this.game.getEventBus().emit(new MouseClickedEvent(event))) {
                    cancel = true;
                }
            }
            if (this.game.getEventBus().emit(new MousePressedEvent(event))) {
                cancel = true;
            }
            if (!cancel) {
                this.mouseclicked = true;
                this.mouseheld = true;
                this.turnoffclick = true;
            }
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * detecting mouse released
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        this.mouseclicked = false;
        this.turnoffclick = false;
        this.mouseheld = false;
    }

    /**
     * detecting mouse dragged
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        try {
            if (!this.game.getEventBus().emit(new MousePressedEvent(event))) {
                this.mouseX = event.getX();
                this.mouseY = event.getY();
            }
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * detecting mouse moved
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        if (!this.game.getEventBus().emit(new MouseMovedEvent(event))) {
            this.mouseX = event.getX();
            this.mouseY = event.getY();
        }
    }

    /**
     * is mouse pressed, if so, only return true ONCE
     * @return if the mouse is pressed or not
     */
    public boolean isMouseClicked() {
        if (this.mouseclicked && !this.turnoffclick){
            this.turnoffclick = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * is mouse pressed within an area, if so, only return true ONCE
     * @param x1 area x1
     * @param y1 area y1
     * @param x2 area x2
     * @param y2 area y2
     * @return if the mouse is pressed inside the area or not
     */
    public boolean isMouseClicked(int x1, int y1, int x2, int y2) {
        if (this.mouseclicked && !this.turnoffclick) {
            if (this.mouseX > x1 && this.mouseY > y1 && this.mouseX < x2 && this.mouseY < y2) {
                this.turnoffclick = true;
                return true;
            }
        }
        return false;
    }

    /**
     * is mouse pressed
     * @return if the mouse is pressed or not
     */
    public boolean isMouseDragged() {
        return this.mouseheld;
    }

    /**
     * is mouse pressed within an area
     * @param x1 area x1
     * @param y1 area y1
     * @param x2 area x2
     * @param y2 area y2
     * @return if the mouse is pressed inside the area or not
     */
    public boolean isMouseDragged(int x1, int y1, int x2, int y2) {
        if (this.mouseX > x1 && this.mouseY > y1 && this.mouseX < x2 && this.mouseY < y2) {
            return this.mouseheld;
        }
        return false;
    }

    /**
     * get the x position of the mouse
     * @return the x position of the mouse
     */
    public int getMouseX() {
        return this.mouseX;
    }

    /**
     * get the y position of the mouse
     * @return the y position of the mouse
     */
    public int getMouseY() {
        return this.mouseY;
    }
}
