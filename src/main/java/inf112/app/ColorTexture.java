package inf112.app;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ColorTexture {
    static public TextureRegion[][] colorRobot(TextureRegion[][] t, Color color) {
        return colorRobot(t[0][0].getTexture(),color);
    }
    static public TextureRegion[][] colorRobot(TextureRegion t, Color color) {
        return colorRobot(t.getTexture(),color);
    }
    static public TextureRegion[][] colorRobot(Texture t, Color color) {
        t.getTextureData().prepare();
        Pixmap pixmap = t.getTextureData().consumePixmap();

        Color purpleColor = new Color(0.99607843F, 0.0F, 1.0F, 1.0F);

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                Color pixelColor = new Color();
                Color.rgba8888ToColor(pixelColor, pixmap.getPixel(x, y));

                if (pixelColor.equals(purpleColor)) {
                    pixmap.setColor(color);
                    pixmap.fillRectangle(x, y, 1, 1);
                }
            }
        }
        t.getTextureData().disposePixmap();
        return TextureRegion.split(new Texture(pixmap),300,300);
    }
}