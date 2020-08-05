/**
 * com.runningmanstudios.caffeineGameEngine.rendering.tiles has classes related to drawing images to screen in a map
 * @see com.runningmanstudios.caffeineGameEngine.rendering.tiles.TileMap
 */
package com.runningmanstudios.caffeineGameEngine.rendering.tiles;

import java.awt.*;
import java.awt.image.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * if you large image made up of smaller tiles
 * TileSet allows you to separate that image into the respective tiles
 */
public class TileSet implements Serializable {
    private BufferedImage setImage;
    private final HashMap<String, Sprite> tiles = new HashMap<String, Sprite>();

    /**
     * creates a Tileset
     * @param setImage tileset image to be separated into smaller tiles
     */
    public TileSet(BufferedImage setImage) {
        this.setImage = setImage;
    }

    /**
     * get the image that this tileset represents
     * @return tileset image to be separated into smaller tiles
     */
    public BufferedImage getSetImage() {
        return this.setImage;
    }

    /**
     * replaces all solid colors in tile with another solid color
     * @param originalColor original color in the tile set image
     * @param newColor new color to replace old color
     */
    public void replaceSolidColor(Color originalColor, Color newColor) {
        changeColor(this.setImage, originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(),
                newColor.getRed(), newColor.getGreen(), newColor.getBlue());
    }

    /**
     * replaces a solid color with transparency
     * @param colorToReplace original color in the tile set image to be replaced with transparency
     */
    public void replaceTransparent(Color colorToReplace) {
        final Image imageWithTransparency = makeColorTransparent(this.setImage, colorToReplace);

        this.setImage = imageToBufferedImage(imageWithTransparency);
    }

    /**
     * convert an Image to a Buffered Image
     * @param image old Image
     * @return new BufferedImage
     */
    private static BufferedImage imageToBufferedImage(final Image image)
    {
        final BufferedImage bufferedImage =
                new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * convert a color to transparency
     * @param im original BufferedImage
     * @param color old color to replace
     * @return new Image
     */
    private static Image makeColorTransparent(final BufferedImage im, final Color color)
    {
        final ImageFilter filter = new RGBImageFilter()
        {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public final int markerRGB = color.getRGB() | 0xFFFFFFFF;

            public final int filterRGB(final int x, final int y, final int rgb)
            {
                if ((rgb | 0xFF000000) == this.markerRGB)
                {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * change color with another color
     * @param imgBuf Buffered Image to change
     * @param oldRed old red value
     * @param oldGreen old green value
     * @param oldBlue old blue value
     * @param newRed new red value
     * @param newGreen new green value
     * @param newBlue new blue value
     */
    private static void changeColor(
            BufferedImage imgBuf,
            int oldRed, int oldGreen, int oldBlue,
            int newRed, int newGreen, int newBlue) {

        int RGB_MASK = 0x00ffffff;
        int ALPHA_MASK = 0xff000000;

        int oldRGB = oldRed << 16 | oldGreen << 8 | oldBlue;
        int toggleRGB = oldRGB ^ (newRed << 16 | newGreen << 8 | newBlue);

        int w = imgBuf.getWidth();
        int h = imgBuf.getHeight();

        int[] rgb = imgBuf.getRGB(0, 0, w, h, null, 0, w);
        for (int i = 0; i < rgb.length; i++) {
            if ((rgb[i] & RGB_MASK) == oldRGB) {
                rgb[i] ^= toggleRGB;
            }
        }
        imgBuf.setRGB(0, 0, w, h, rgb, 0, w);
    }

    /**
     * create a new Tile from the Tileset's buffered image
     * @param tileName new name of the time
     * @param startX get image from startX
     * @param startY get image from startY
     * @param endX get image to endX
     * @param endY get image to endY
     */
    public void seperateTile(String tileName, long startX, long startY, long endX, long endY) {
        BufferedImage newTileImage = this.setImage.getSubimage((int) startX, (int) startY, (int) (endX - startX), (int) (endY - startY));
        Sprite newSprite = new Sprite(newTileImage);

        this.tiles.put(tileName, newSprite);
    }

    /**
     * get a tile from the tileset
     * @param tileName name of the tile
     * @return tile; null if tile does not exist
     */
    public Sprite getTile(String tileName) {
        return this.tiles.get(tileName);
    }
}
