package com.runningmanstudios.caffeineGameEngine.entities.components;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;

import java.io.Serializable;

/**
 * EntityComponent is used for creating custom components that can be added to entities
 */
public abstract class EntityComponent implements Serializable {
    protected AbstractEntity entity;
    public EntityComponent(AbstractEntity entity) {
        this.entity = entity;
        this.onCreate();
    }

    public abstract void onCreate();

    public abstract void onExit();

    public abstract void onUpdate();
}
