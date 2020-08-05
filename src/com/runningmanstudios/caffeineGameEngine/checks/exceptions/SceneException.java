/**
 * com.runningmanstudios.caffeineGameEngine.checks contains custom annotations and Exceptions
 */
package com.runningmanstudios.caffeineGameEngine.checks.exceptions;

/**
 * The EntityRegistryException produces an error whenever something is done wrong in the EntityRegistry
 * it is usually produced when trying to get an EntityType that is not in the EntityRegistry
 * however it can also be triggered when trying to add an EntityType to the EntityRegistry that already exists
 *
 * @author NotAFlyingGoose
 */
public class SceneException extends Exception{

    public SceneException(String message) {
        super(message);
    }

    public SceneException(Throwable cause) {
        super(cause);
    }

    public SceneException(String message, Throwable cause) {
        super(message, cause);
    }

}
