package com.runningmanstudios.caffeineGameEngine.checks.exceptions;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBusSubscriber;

/**
 * The EventException produces an error when you add a class to the EventBus without a method with the annotation "@EventBusSubscriber",
 * or when a method with the annotation "@EventBusSubscriber" does not have a Event as a parameter
 * @author NotAFlyingGoose
 * @see com.runningmanstudios.caffeineGameEngine.checks.event.Event
 * @see com.runningmanstudios.caffeineGameEngine.checks.event.EventBus
 * @see EventBusSubscriber
 */
public class EventException extends Exception{

    public EventException(String message) {
        super(message);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

}
