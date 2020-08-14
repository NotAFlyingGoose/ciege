package com.runningmanstudios.caffeineGameEngine.events.input;

import java.awt.event.KeyEvent;

public class KeyboardInputEvent extends InputEvent{
    private final KeyEvent input;

    public KeyboardInputEvent(KeyEvent keyEvent) {
        super(keyEvent);
        this.input = keyEvent;
    }

    @Override
    public KeyEvent getTarget() {
        return this.input;
    }
}
