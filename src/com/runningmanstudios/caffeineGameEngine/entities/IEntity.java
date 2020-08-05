/**
 * com.runningmanstudios.caffeineGameEngine.Entities contains classes for creating Entities
 * @see com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity
 */
package com.runningmanstudios.caffeineGameEngine.entities;

import com.runningmanstudios.caffeineGameEngine.entities.registry.EntityRegistry;

/**
 * Basic Entity Interface
 */
public interface IEntity {
    long getX();
    long getY();
    void setX(long x);
    void setY(long y);
    EntityRegistry.EntityType getType();
    void setType(EntityRegistry.EntityType e);
    void onCreate();
    void onUpdate();

}
