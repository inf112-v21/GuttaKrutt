package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import inf112.app.networking.GameClient;
import inf112.app.networking.GameServer;
import inf112.app.*;

import java.io.IOException;

public class SetupScreen implements Screen {
    Stage stage;

    TextField name;
    TextButton playButton;

    public SetupScreen(Game game) {
        stage = new Stage(new ScreenViewport());

        Preferences prefs = Gdx.app.getPreferences("RoboRally");

        Table nameTable = new Table();
        nameTable.setFillParent(true);
        stage.addActor(nameTable);
        nameTable.bottom();

        Label nameField = new Label("Name:", RoboRally.skin);
        nameTable.add(nameField).padLeft(50).padBottom(15).left();
        nameTable.row();

        String defaultName = prefs.getString("lastUsedName");
        if (defaultName == "") defaultName = "name";

        name = new TextField(defaultName, RoboRally.skin);
        nameTable.add(name).prefWidth(99999).padLeft(50).padBottom(15).padRight(20).left();

        playButton = new TextButton("Play!", RoboRally.skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                prefs.putString("lastUsedName", name.getText());

                prefs.flush();

                GameClient client = null;
                try {
                    new GameServer();
                    client = new GameClient();
                    client.name = name.getText();
                    client.getClient().sendTCP(name.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.setScreen(new LobbyScreen(game, client));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        nameTable.add(playButton).right().padRight(15).padBottom(15).width(100);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i,i1,true);
        stage.getViewport().getCamera().update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
