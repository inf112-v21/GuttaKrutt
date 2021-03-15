package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class RoboRally extends Game {
    static public Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
        setScreen(new MenuScreen(this));
    }
}
