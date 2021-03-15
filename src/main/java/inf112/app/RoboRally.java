package inf112.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.app.screens.MenuScreen;


public class RoboRally extends Game {
    static public Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
        setScreen(new MenuScreen(this));
    }
}
