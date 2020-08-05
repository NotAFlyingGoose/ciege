package com.runningmanstudios.caffeineGameEngine.events.input.mouse;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;

import java.awt.event.MouseEvent;

@EventBuilder
public class MousePressedEvent extends MouseInputEvent {
    public MousePressedEvent(MouseEvent mouseEvent) {
        super(mouseEvent);
    }
}
