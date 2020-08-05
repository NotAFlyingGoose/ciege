/**
 * com.runningmanstudios.caffeineGameEngine.Entities.registry is in charge of the EntityRegistry
 * @see com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry
 */
package com.runningmanstudios.caffeineGameEngine.entities.registry;

import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EntityRegistryException;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EntityRegistry contains EntityTypes which can be applied to entities
 */
public class EntityRegistry implements Serializable {
    private static final Set<EntityType> entityRegistry = ConcurrentHashMap.newKeySet();

    /**
     * creates default registry values
     */
    static {
        entityRegistry.add(new EntityType("BASIC", EntityBehavior.SOLID));
        entityRegistry.add(new EntityType("BUTTON", EntityBehavior.SOLID));
        entityRegistry.add(new EntityType("PLAYER", EntityBehavior.CONTROLLABLE));
        entityRegistry.add(new EntityType("CHARACTER", EntityBehavior.NPC));
        entityRegistry.add(new EntityType("ENEMY", EntityBehavior.ENEMY));
        entityRegistry.add(new EntityType("WALL", EntityBehavior.SOLID));
    }

    /**
     * registers a new EntityType to be added to the registry
     * @param key new Key
     * @param behavior new Behavior
     * @throws EntityRegistryException if key exists
     */
    public static void register(String key, EntityBehavior behavior) throws EntityRegistryException {
        EntityType entityType = new EntityType(key, behavior);
        if (getItem(entityType.getKey()) == null) {
            entityRegistry.add(entityType);
        } else {
            throw new EntityRegistryException("Key  ->  \"" + key + "\" <-\nKey exists already in the EntityRegistry");
        }
    }

    /**
     * gets an item from the registry
     * @param key EntityType key
     * @return EntityType from registry
     * @throws EntityRegistryException if key does not exist
     */
    public static EntityType get(String key) throws EntityRegistryException {
        EntityType r = getItem(key);
        if (r == null) {
            throw new EntityRegistryException("Key  ->  \"" + key + "\"\nKey does not exist in the EntityRegistry");
        } else {
            return r;
        }
    }

    /**
     * gets an item from the registry
     * @param key EntityType key
     * @return EntityType from registry
     */
    private static EntityType getItem(String key) {
        for (EntityType entityType : entityRegistry) {
            if (entityType.getKey().equals(key)) {
                return entityType;
            }
        }
        return null;
    }

    /**
     * gets a copy of the registry
     * @return a copy of the registry
     */
    public static Set<EntityType> getCopyOfRegistry() {
        return entityRegistry;
    }

    /**
     * gets a copy of the registry keys as a string
     * @return a copy of the registry keys as a string
     */
    public static String getCopyOfRegistryAsString() {
        StringBuilder keys = new StringBuilder();
        Set<EntityType> regcopy = getCopyOfRegistry();
        for (EntityType entityType : regcopy) {
            keys.append(entityType.getKey() + ", ");
        }
        keys.delete(keys.length() - 2, keys.length());

        return "[" + keys + "]";
    }

    /**
     * prints the registry using the Game Logger
     */
    public static void printRegistry() {
        GameLogger.info(getCopyOfRegistryAsString());
    }

    /**
     * EntityType contains a key and a behavior, it is generic so you can do with it what you want
     * but some of things it can be used for is detecting specific entity collisions in the BoxCollider
     * it does not actually affect Entities unless you program it to
     */
    public static class EntityType implements Serializable {
        private final String key;
        private final EntityBehavior behavior;
        private EntityType(String key, EntityBehavior behavior) {
            this.key = key;
            this.behavior = behavior;
        }

        public String getKey() { return this.key; }

        public EntityBehavior getBehavior() { return this.behavior; }
    }

    /**
     * EntityBehavior is a way of showing what this Entity does, it is generic so you can do with it what you want
     * it does not actually affect Entities unless you program it to
     */
    public enum EntityBehavior {
        SOLID, CONTROLLABLE, BASIC, ENEMY, MONSTER, NPC
    }
}


