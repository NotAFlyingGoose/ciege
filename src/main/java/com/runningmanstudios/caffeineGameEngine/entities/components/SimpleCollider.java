package com.runningmanstudios.caffeineGameEngine.entities.components;

import com.runningmanstudios.caffeineGameEngine.entities.AbstractEntity;
import com.runningmanstudios.caffeineGameEngine.events.entity.EntityCollisionEvent;

import java.awt.*;

public class SimpleCollider extends EntityComponent {

    public SimpleCollider(AbstractEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void onUpdate() {
        MoveWithCollisions();
    }

    public void MoveWithCollisions() {
        for (AbstractEntity otherEntity : this.entity.getSceneManager().getCurrentScene().getEntitiesInScene()) {
            if (otherEntity == this.entity) continue;
            SimpleCollider sc = (SimpleCollider) otherEntity.getComponent(this.getClass());
            if (sc != null) {
                if (this.getVelXBounds().intersects(sc.getLiteralBounds())) {
                    if (!this.entity.getSceneManager().getGame().getEventBus().emit(new EntityCollisionEvent(this.entity, otherEntity))) {
                        if (otherEntity.getVelX() < 0) {//box left
                            if (this.entity.getX() < otherEntity.getX() + otherEntity.getWidth() / 2)
                                this.entity.setX(otherEntity.getX() - otherEntity.getWidth());
                        } else if (otherEntity.getVelX() > 0) {//box right
                            if (this.entity.getX() > otherEntity.getX() + otherEntity.getWidth() / 2)
                                this.entity.setX(otherEntity.getX() + otherEntity.getWidth());
                        }

                        if (this.entity.getVelX() > 0) {//right

                            this.entity.setVelX(0);
                            this.entity.setX(otherEntity.getX() - this.entity.getWidth());

                        } else if (this.entity.getVelX() < 0) {//left

                            this.entity.setVelX(0);
                            this.entity.setX(otherEntity.getX() + otherEntity.getWidth());

                        }
                    } else continue;
                }
                if (this.getVelYBounds().intersects(sc.getLiteralBounds())) {
                    if (!this.entity.getSceneManager().getGame().getEventBus().emit(new EntityCollisionEvent(this.entity, otherEntity))) {
                        if (otherEntity.getVelY() < 0) {//box up
                            if (this.entity.getY() < otherEntity.getY() + otherEntity.getHeight() / 2)
                                this.entity.setY(otherEntity.getY() - this.entity.getHeight());
                        } else if (otherEntity.getVelY() > 0) {//box down
                            if (this.entity.getY() > otherEntity.getY() + otherEntity.getHeight() / 2)
                                this.entity.setY(otherEntity.getY() + otherEntity.getHeight());
                        }

                        if (this.entity.getVelY() > 0) {//down

                            this.entity.setVelY(0);
                            this.entity.setY(otherEntity.getY() - this.entity.getHeight());

                        } else if (this.entity.getVelY() < 0) {//up

                            this.entity.setVelY(0);
                            this.entity.setY(otherEntity.getY() + otherEntity.getHeight());

                        }
                    } else continue;
                }
            }
        }
    }

    public boolean isColliding() {
        for (AbstractEntity otherEntity : this.entity.getSceneManager().getCurrentScene().getEntitiesInScene()) {
            if (otherEntity == this.entity) continue;
            SimpleCollider sc = (SimpleCollider) otherEntity.getComponent(this.getClass());
            if (sc != null) {
                if (this.getVelXBounds().intersects(sc.getLiteralBounds())) {
                    if (!this.entity.getSceneManager().getGame().getEventBus().emit(new EntityCollisionEvent(this.entity, otherEntity))) {
                        return true;
                    }
                }
                if (this.getVelYBounds().intersects(sc.getLiteralBounds())) {
                    if (!this.entity.getSceneManager().getGame().getEventBus().emit(new EntityCollisionEvent(this.entity, otherEntity))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Rectangle getLiteralBounds() {
        return new Rectangle((int)this.entity.getX(), (int)this.entity.getY(), (int)this.entity.getWidth(), (int)this.entity.getHeight());
    }

    public Rectangle getVelXBounds() {
        float bx = this.entity.getX()+this.entity.getVelX();
        float by = this.entity.getY() + 2;
        float bw = this.entity.getWidth()+this.entity.getVelX()/2;
        float bh = this.entity.getHeight() - 4;

        return new Rectangle((int)bx, (int)by, (int)bw, (int)bh);
    }

    public Rectangle getVelYBounds() {
        float bx = this.entity.getX() + 2;
        float by = this.entity.getY()+this.entity.getVelY();
        float bw = this.entity.getWidth() - 4;
        float bh = this.entity.getHeight()+this.entity.getVelY()/2;

        return new Rectangle((int)bx, (int)by, (int)bw, (int)bh);
    }
}
