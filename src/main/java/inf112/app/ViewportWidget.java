package inf112.app;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Class that resizes a viewport as a widget would.
 * Useful for adding to tables, etc. to provide a specific layout.
 */
public class ViewportWidget extends Widget {
    private final Viewport vp;

    /** Class that resizes a viewport as a widget would.
     * Useful for adding to tables, etc. to provide a specific layout.
     * @param vp Viewport to control with widget.
     */
    public ViewportWidget(Viewport vp) { this.vp = vp; }

    public Viewport getViewport() { return vp; }

    public void layout() {
        vp.setScreenSize((int) getWidth(),(int) getHeight());
        vp.setWorldSize(getWidth()/200,getHeight()/200);
    }

    public void draw(Batch batch, float parentAlpha) {
        validate();
        vp.setScreenPosition((int) getX(),(int) getY());
    }
}
