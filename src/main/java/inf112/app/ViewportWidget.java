package inf112.app;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Class that resizes a viewport as a widget would.
 * Useful for adding to tables, etc. to provide a specific layout.
 */
public class ViewportWidget extends Widget {
    private Viewport vp;

    public ViewportWidget(Viewport vp) { this.vp = vp; }

    public Viewport getViewport() { return vp; }

    public void setViewport(Viewport vp) { this.vp = vp; }

    public void layout() {
        vp.setScreenBounds((int) getX(),(int) getY(),(int) getWidth(),(int) getHeight());
        vp.setWorldSize(getWidth()/200,getHeight()/200);
    }
}
