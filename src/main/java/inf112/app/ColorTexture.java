package inf112.app;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class ColorTexture {

    /**
     * Applies the given color to a texture. Replaces all colors with hue 300 (purple) to new color.
     * @param t TextureRegion to color.
     * @param color Color to apply to texture.
     */
    static public TextureRegion[][] colorRobot(TextureRegion[][] t, Color color) {
        return colorRobot(t[0][0].getTexture(),color);
    }

    /**
     * Applies the given color to a texture. Replaces all colors with hue 300 (purple) to new color.
     * @param t TextureRegion to color.
     * @param color Color to apply to texture.
     */
    static public TextureRegion[][] colorRobot(TextureRegion t, Color color) {
        return colorRobot(t.getTexture(),color);
    }

    /**
     * Applies the given color to a texture. Replaces all colors with hue 300 (purple) to new color.
     * @param t Texture to color.
     * @param color Color to apply to texture.
     */
    static public TextureRegion[][] colorRobot(Texture t, Color color) {
        t.getTextureData().prepare();
        Pixmap pixmap = t.getTextureData().consumePixmap();

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                Color pixelColor = new Color();
                Color.rgba8888ToColor(pixelColor, pixmap.getPixel(x, y));

                float[] hsv = new float[3];
                pixelColor.toHsv(hsv); //Changes representation of the color from RGB to HSV, so shadows are included in original color.

                if ((int) hsv[0] == 299) {
                    pixmap.setColor(color);
                    pixmap.fillRectangle(x, y, 1, 1);
                }
            }
        }

        t.getTextureData().disposePixmap();
        return TextureRegion.split(new Texture(pixmap),300,300);
    }
}
