package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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

        String defaultName = prefs.getString("lastUsedName");
        if (defaultName == "") defaultName = "name";

        Table rootTable = new Table();
        stage.addActor(rootTable);
        rootTable.setFillParent(true);

        rootTable.add(new Label("IP: ", RoboRally.skin));
        TextField host = new TextField("localhost", RoboRally.skin);
        host.setWidth(Gdx.graphics.getWidth()/2);
        host.setPosition(Gdx.graphics.getWidth()/2-host.getWidth()/2,Gdx.graphics.getHeight()/2);
        rootTable.add(host).prefWidth(500).padBottom(10);
        host.setDisabled(true);
        host.setColor(Color.LIGHT_GRAY);

        rootTable.row();
        rootTable.add(new Label("Name: ", RoboRally.skin));
        TextField name = new TextField(defaultName, RoboRally.skin);
        rootTable.add(name).prefWidth(500);

        playButton = new TextButton("Play!", RoboRally.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/10);
        playButton.setPosition(Gdx.graphics.getWidth()/10-playButton.getWidth()/10,Gdx.graphics.getHeight()/10-playButton.getHeight()/10);
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
        stage.addActor(playButton);
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
