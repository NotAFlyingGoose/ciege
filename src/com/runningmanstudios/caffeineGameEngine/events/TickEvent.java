package com.runningmanstudios.caffeineGameEngine.events;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBuilder;
import com.runningmanstudios.caffeineGameEngine.checks.event.Event;
import com.runningmanstudios.caffeineGameEngine.window.Game;

@EventBuilder
public class TickEvent extends Event {
    private final Game game;

    public TickEvent(Game game) {
        this.game = game;
    }

    @Override
    public Game getTarget() {
        return this.game;
    }
}
