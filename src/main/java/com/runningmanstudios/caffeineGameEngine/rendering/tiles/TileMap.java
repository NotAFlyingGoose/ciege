/**
 * com.runningmanstudios.caffeineGameEngine.rendering.tiles has classes related to drawing images to screen in a map
 * @see com.runningmanstudios.caffeineGameEngine.rendering.tiles.TileMap
 */
package com.runningmanstudios.caffeineGameEngine.rendering.tiles;

import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.window.Game;

import java.io.Serializable;
import java.util.HashMap;

/**
 * TileMap creates a map of tiles that are drawn to the screen.
 */
public class TileMap implements Serializable {
    HashMap<String, Sprite> map = new HashMap<String, Sprite>();
    private final Game game;
    private final Scene scene;
    private final int width, height;
    private int placeX, placeY;

    /**
     * create a TileMap
     * @param game game object
     * @param scene scene to create map in
     * @param size size of images to be drawn
     */
    public TileMap(Game game, Scene scene, int size) {
        this.game = game;
        this.scene = scene;
        this.width = size;
        this.height = size;
        this.placeX = this.width;
        this.placeY = this.height;
    }

    /**
     * create a TileMap
     * @param game game object
     * @param scene scene to create map in
     * @param width width of images to be drawn
     * @param height height of images to be drawn
     */
    public TileMap(Game game, Scene scene, int width, int height) {
        this.game = game;
        this.scene = scene;
        this.width = width;
        this.height = height;
        this.placeX = this.width;
        this.placeY = this.height;
    }

    /**
     * draw a tile every x, y
     * @param x x offset
     * @param y y offset
     */
    public void placeEvery(int x, int y) {
        this.placeX = x;
        this.placeY = y;
    }

    /**
     * adds a sprite to the map
     * @param x where to place the tile's x
     * @param y where to place the tile's y
     * @param sprite tile to draw
     */
    public void addTileToMap(long x, long y, Sprite sprite) {
        this.map.put(x+":"+y, sprite);
    }

    /**
     * draws the map and all the sprites inside
     * @param sg ScenicGraphics object to draw the map with
     */
    public void DrawMap(ScenicGraphics sg) {
        for (String tileLocation : this.map.keySet()) {
            Sprite sprite = this.map.get(tileLocation);
            long[] realLocations = this.getLocation(tileLocation);

            sg.displayImage(sprite.getImage(), (realLocations[0] * this.placeX), (realLocations[1] * this.placeY), this.width, this.height);
        }
    }

    /**
     * convert tile key into long[] position
     * @param key tile key
     * @return long[] position
     */
    public long[] getLocation(String key) {
        long[] location = new long[2];
        String[] newString = key.split(":");

        location[0] = Integer.parseInt(newString[0]);
        location[1] = Integer.parseInt(newString[1]);

        return location;
    }

    /**
     * get the tile at position
     * @param x x position of tile to find
     * @param y y position of tile to find
     * @return tile; will be null if no tile at that position
     */
    public Sprite getTileAt(int x, int y) {
        return this.map.get(x + ":" + y);
    }
}
