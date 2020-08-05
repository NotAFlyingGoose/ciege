package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.entities.basic.SimplePlayerController;
import com.runningmanstudios.caffeineGameEngine.entities.camera.SmoothFollowCamera;
import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.rendering.tiles.TileMap;
import com.runningmanstudios.caffeineGameEngine.rendering.tiles.TileSet;
import com.runningmanstudios.ciegedemo.Cohar;
import com.runningmanstudios.ciegedemo.ObjectIntheRoom;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Forest extends Scene {
    TileSet forest;
    private final int mapSize = 100;
    private final TileMap mapLevel1 = new TileMap(this.getSceneManager().getGame(), this, this.mapSize);
    private final TileMap mapLevel2 = new TileMap(this.getSceneManager().getGame(), this, this.mapSize);
    private final TileMap mapLevel3 = new TileMap(this.getSceneManager().getGame(), this, this.mapSize *2, this.mapSize *4);
    private SimplePlayerController player;
    public ObjectIntheRoom o;
    public Cohar c;
    private SmoothFollowCamera cam1;

    public Forest(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        this.player = new SimplePlayerController(50,50,3.0, this.getSceneManager());
        this.o = new ObjectIntheRoom(145,250,25,25, this.getSceneManager());
        this.c = new Cohar(100,250,50,200, this.getSceneManager());
        this.addEntityToScene(this.player);
        this.addEntityToScene(this.o);
        this.addEntityToScene(this.c);
        this.cam1 = new SmoothFollowCamera(this.getSceneManager().getGame(), 1f, this.player);
        this.setCamera(this.cam1);
        this.player.setZIndex(1);

        Random random = new Random();
        BufferedImage tileset;
        try {
            tileset = ImageIO.read(this.getClass().getResource("/resources/images/forest_tiles.png"));
            this.forest = new TileSet(tileset);
            this.forest.replaceTransparent(Color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.forest.seperateTile("grass1", 0,0,16,16);
        this.forest.seperateTile("grass2", 16,16,32,32);

        this.forest.seperateTile("stump1", 0,32,16,48);
        this.forest.seperateTile("stump2", 32,32,48,48);
        this.forest.seperateTile("stump3", 16,48,32,64);

        this.forest.seperateTile("rock1", 0,64,16,80);
        this.forest.seperateTile("rock2", 32,64,48,80);
        this.forest.seperateTile("rock3", 16,80,32,96);
        this.forest.seperateTile("rock4", 48,80,64,96);

        this.forest.seperateTile("bush1", 0,96,16,112);
        this.forest.seperateTile("bush2", 32,96,48,112);
        this.forest.seperateTile("bush3", 64,96,80,112);
        this.forest.seperateTile("bush4", 16,112,32,128);
        this.forest.seperateTile("bush5", 48,112,64,128);
        this.forest.seperateTile("bush6", 80,112,96,128);

        this.forest.seperateTile("tree1", 64,0,96,32);
        this.forest.seperateTile("tree2", 128,0,160,64);
        this.forest.seperateTile("tree3", 192,0,224,64);

        //floor
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                int i = random.nextInt(2);
                if (i == 1) {
                    this.mapLevel1.addTileToMap(x, y, this.forest.getTile("grass1"));
                } else {
                    this.mapLevel1.addTileToMap(x, y, this.forest.getTile("grass2"));
                }
            }
        }
        //folliage
        this.mapLevel3.placeEvery(this.mapSize *2, this.mapSize *2);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                int i = random.nextInt(20);
                if (i == 0) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("stump1"));
                } else if (i == 1) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("stump2"));
                } else if (i == 2) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("stump3"));
                } else if (i == 3) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush1"));
                } else if (i == 4) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush2"));
                } else if (i == 5) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush3"));
                } else if (i == 6) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush4"));
                } else if (i == 7) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush5"));
                } else if (i == 8) {
                    this.mapLevel2.addTileToMap(x, y, this.forest.getTile("bush6"));
                }
            }
        }
        for (int x = 0; x < 16/4; x++) {
            for (int y = 0; y < 16/4; y++) {
                int i = random.nextInt(20);
                if (i == 1) {
                    this.mapLevel3.addTileToMap(x, y, this.forest.getTile("tree1"));
                } else if (i == 2) {
                    this.mapLevel3.addTileToMap(x, y, this.forest.getTile("tree2"));
                } else if (i == 3) {
                    this.mapLevel3.addTileToMap(x, y, this.forest.getTile("tree3"));
                }
            }
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        this.mapLevel1.DrawMap(sg);
        this.mapLevel2.DrawMap(sg);
        this.mapLevel3.DrawMap(sg);
    }
}
