package com.runningmanstudios.caffeineGameEngine.events;

import com.runningmanstudios.caffeineGameEngine.checks.event.Event;

import java.awt.*;

public class DrawEvent extends Event {
    private final transient Graphics2D g2d;

    public DrawEvent(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public Graphics2D getTarget() {
        return this.g2d;
    }
}
