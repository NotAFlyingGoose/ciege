package com.runningmanstudios.caffeineGameEngine.events.input.mouse;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;

import java.awt.event.MouseEvent;

@EventBuilder
public class MouseClickedEvent extends MouseInputEvent {
    public MouseClickedEvent(MouseEvent mouseEvent) {
        super(mouseEvent);
    }
}
