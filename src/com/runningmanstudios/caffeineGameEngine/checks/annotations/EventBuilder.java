package com.runningmanstudios.caffeineGameEngine.checks.annotations;

import java.lang.annotation.*;

/**
 * EventBuilder must be added to a class to make it an Event.
 * Classes that don't extend Event and EventBuilder are not Events.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBuilder {
    String usages() default "";
}
