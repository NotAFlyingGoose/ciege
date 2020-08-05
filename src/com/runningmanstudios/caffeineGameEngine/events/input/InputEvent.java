package com.runningmanstudios.caffeineGameEngine.events.input;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;
import com.runningmanstudios.caffeineGameEngine.checks.event.Event;

@EventBuilder
public class InputEvent extends Event {
    private final java.awt.event.InputEvent input;

    public InputEvent(java.awt.event.InputEvent input) {
        this.input = input;
    }

    @Override
    public java.awt.event.InputEvent getTarget() {
        return this.input;
    }
}
