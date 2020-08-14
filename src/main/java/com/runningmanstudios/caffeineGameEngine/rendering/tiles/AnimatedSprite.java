package com.runningmanstudios.caffeineGameEngine.rendering.tiles;

import com.runningmanstudios.caffeineGameEngine.util.RepeatingGameTimer;

import java.awt.image.BufferedImage;

/**
 * AnimatedSprite contains an array of BufferedImages that can be drawn to the screen.
 * the images are changed automatically using a RepeatingGameTimer.
 * @see com.runningmanstudios.caffeineGameEngine.rendering.tiles.Sprite
 * @see com.runningmanstudios.caffeineGameEngine.util.RepeatingGameTimer
 */
public class AnimatedSprite extends Sprite {
    private int currentFrame = 0;
    private final BufferedImage[] images;
    RepeatingGameTimer imageUpdateTimer;

    /**
     * creates an AnimatedSprite
     * AnimatedSprite will change sprites every [insert amount here] milliseconds
     * @param images Buffered Image that this sprite represents
     */
    public AnimatedSprite(int milliseconds, BufferedImage... images) {
        super(images[0]);
        this.images = images;
        this.imageUpdateTimer = new RepeatingGameTimer(milliseconds);
        this.imageUpdateTimer.resume();
    }

    /**
     * updates the AnimatedSprite, changing the current image if time has passed
     */
    public void updateSprites() {
        if (this.imageUpdateTimer.isDone()){
            if (this.currentFrame == this.images.length-1) this.currentFrame = 0;
            else this.currentFrame++;
            this.setImage(this.images[this.currentFrame]);
        }
    }
}
