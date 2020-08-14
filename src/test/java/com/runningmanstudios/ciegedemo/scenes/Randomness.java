package com.runningmanstudios.ciegedemo.scenes;

import com.runningmanstudios.caffeineGameEngine.rendering.Scene;
import com.runningmanstudios.caffeineGameEngine.rendering.SceneManager;
import com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics;
import com.runningmanstudios.caffeineGameEngine.rendering.tiles.AnimatedSprite;
import com.runningmanstudios.caffeineGameEngine.util.FancyMath;
import com.runningmanstudios.caffeineGameEngine.util.log.GameLogger;
import com.runningmanstudios.caffeineGameEngine.util.noise.SimplexNoise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Randomness extends Scene {
    AnimatedSprite animation;
    final float C = -5.0f;
    float k = 0.1f;
    float j = 0.1f;
    /**
     * Creates a Scene. requires a Scene Manager
     * @param sceneManager Scene Manager
     */
    public Randomness(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onCreate() {
        try {
            BufferedImage tile1 = ImageIO.read(this.getClass().getResource("/resources/images/forest_tiles.png"));
            BufferedImage tile2 = ImageIO.read(this.getClass().getResource("/resources/images/dk_wall.png"));
            BufferedImage tile3 = ImageIO.read(this.getClass().getResource("/resources/images/shaggy.png"));
            this.animation = new AnimatedSprite(500, tile1, tile2, tile3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        this.animation.updateSprites();
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_UP)){
            this.k +=0.01f;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_DOWN)){
            this.k -=0.01f;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_LEFT)){
            this.j +=0.01f;
        }
        if (this.getSceneManager().getGame().getInput().isKeyPressed(KeyEvent.VK_RIGHT)){
            this.j -=0.01f;
        }
        if (this.getSceneManager().getGame().getInput().isKeyClicked(KeyEvent.VK_SPACE)){
            GameLogger.info(java.util.Optional.of(this.k));
            GameLogger.info(java.util.Optional.of(this.j));
        }
    }

    @Override
    public void onDraw(ScenicGraphics sg) {
        sg.displayImage(this.animation.getImage(), 50, 50, 50, 50);
        for (float x = 0f; x < 10f; x+=0.01f) {
            float a = (float)Math.sqrt(x*2);
            float b = (float)Math.pow((4 - x),2);
            float sm1 = FancyMath.smin(a, b, this.k);
            float sm2 = FancyMath.smax(a, b, this.k);
            int halfHeight = this.getSceneManager().getGame().getHeight() / 2;
            sg.lineThickness(1);
            sg.setColor(Color.red);
            sg.displayPoint((long)(x/0.01), (long)(a* this.C) + halfHeight);
            sg.setColor(Color.green);
            sg.displayPoint((long)(x/0.01), (long)(b* this.C) + halfHeight);
            sg.lineThickness(3);
            sg.setColor(Color.blue);
            sg.displayPoint((long)(x/0.01), (long)(sm1* this.C) + halfHeight);
            sg.lineThickness(2);
            sg.setColor(Color.orange);
            sg.displayPoint((long)(x/0.01), (long)(sm2* this.C) + halfHeight);
        }
        sg.resetLineThickness();
        SimplexNoise noise = new SimplexNoise((int) (this.k *100), this.j, "cookies");
        for (int x = 0; x < 400; x+=5) {
            for (int y = 0; y < 400; y+=5) {
                double v = noise.getNoise(x, y);
                v = FancyMath.clamp((int)FancyMath.range(-1, 1, 0, 225, v), 0, 225);
                sg.setColor(new Color((int)v, (int)v, (int)v));
                sg.displayRect(x, y, 5, 5, true, false);
            }
        }
    }

    /**
     * abstract onExit() method, called when Scene Manager unloads this scene
     */
    @Override
    public void onExit() {

    }
}
