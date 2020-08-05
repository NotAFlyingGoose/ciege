package com.runningmanstudios.caffeineGameEngine.checks.annotations;

import java.lang.annotation.*;

/**
 * EventBusSubscriber is added to methods combined with an Event parameter to get Events from the EventBus
 * @see com.runningmanstudios.caffeineGameEngine.checks.event.EventBus
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventBusSubscriber {

}
