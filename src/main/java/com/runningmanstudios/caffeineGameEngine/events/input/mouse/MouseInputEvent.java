package com.runningmanstudios.caffeineGameEngine.events.input.mouse;

import com.runningmanstudios.caffeineGameEngine.events.input.InputEvent;

import java.awt.event.MouseEvent;

public class MouseInputEvent extends InputEvent {
    private final MouseEvent input;

    public MouseInputEvent(MouseEvent mouseEvent) {
        super(mouseEvent);
        this.input = mouseEvent;
    }

    @Override
    public MouseEvent getTarget() {
        return this.input;
    }
}
