/**
 * com.runningmanstudios.caffeineGameEngine.rendering.tiles has classes related to drawing images to screen in a map
 * @see com.runningmanstudios.caffeineGameEngine.rendering.tiles.TileMap
 */
package com.runningmanstudios.caffeineGameEngine.rendering.tiles;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Sprite contains a BufferedImage that can be drawn to the screen
 */
public class Sprite implements Serializable {
    private BufferedImage images;

    /**
     * creates a Sprite
     * @param images Buffered Image that this sprite represents
     */
    public Sprite(BufferedImage images) {
        this.images = images;
    }

    /**
     * gets the Sprite's BufferedImage
     * @return the sprite's BufferedImage
     */
    public BufferedImage getImage() {
        return this.images;
    }

    /**
     * sets the Sprite's BufferedImage
     * @param images the new sprite's BufferedImage
     */
    public void setImage(BufferedImage images) {
        this.images = images;
    }
}
