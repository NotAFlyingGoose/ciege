package com.runningmanstudios.caffeineGameEngine.checks.exceptions;

/**
 * The EntityRegistryException produces an error whenever something is done wrong in the EntityRegistry
 * it is usually produced when trying to get an EntityType that is not in the EntityRegistry
 * however it can also be triggered when trying to add an EntityType to the EntityRegistry that already exists
 *
 * @author NotAFlyingGoose
 */
public class EntityRegistryException extends Exception{

    public EntityRegistryException(String message) {
        super(message);
    }

    public EntityRegistryException(Throwable cause) {
        super(cause);
    }

    public EntityRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

}
