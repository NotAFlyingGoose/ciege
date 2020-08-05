package com.runningmanstudios.caffeineGameEngine.checks.annotations;

import java.lang.annotation.*;

/**
 * GameBuilder is applied to classes that create Game instances
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GameBuilder {
    /**
     * The id of the game that you are creating, used for the game folder
     */
    String id();
    /**
     * The current version of the game your making
     */
    String version() default "1.0.0";
    /**
     * Whether or not to make a game folder located in {@code (your home directory)\.ciege\games\(your game id)\}
     */
    boolean makeFolder();
    /**
     * Whether or not to make a custom folder in your game folder with your version number (useful for cross compatibility) {@code (your home directory)\.ciege\games\(your game id)\(your game version)\}
     */
    boolean versionFolder() default false;
    /**
     * the author of the game being created
     */
    String author() default "";
}
