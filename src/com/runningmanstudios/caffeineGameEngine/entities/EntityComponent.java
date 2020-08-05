package com.runningmanstudios.caffeineGameEngine.entities;

import java.io.Serializable;

/**
 * EntityComponent is used for creating custom components that can be added to entities
 */
public abstract class EntityComponent implements Serializable {
    /**
     * get the current component
     */
    public abstract EntityComponent getComponent();
}
