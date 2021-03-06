package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.RoboRally;
import inf112.app.networking.GameClient;

import java.io.IOException;

public class JoinScreen implements Screen {
    Stage stage;
    Game game;

    public JoinScreen(Game game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());

        Preferences prefs = Gdx.app.getPreferences("RoboRally");

        String defaultIP = prefs.getString("lastUsedIP");
        if (defaultIP.equals("")) defaultIP = "localhost";

        String defaultName = prefs.getString("lastUsedName");
        if (defaultName.equals("")) defaultName = "name";

        Table rootTable = new Table();
        stage.addActor(rootTable);
        rootTable.setFillParent(true);

        rootTable.add(new Label("IP: ", RoboRally.skin));
        TextField host = new TextField(defaultIP, RoboRally.skin);
        rootTable.add(host).prefWidth(500).padBottom(10);

        rootTable.row();
        rootTable.add(new Label("Name: ", RoboRally.skin));
        TextField name = new TextField(defaultName, RoboRally.skin);
        rootTable.add(name).prefWidth(500);

        TextButton playButton = new TextButton("Play!", RoboRally.skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                prefs.putString("lastUsedIP",host.getText());
                prefs.putString("lastUsedName",name.getText());

                prefs.flush();

                GameClient client = null;
                try {
                    client = new GameClient(host.getText(),true);
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
        rootTable.row();
        rootTable.add();
        rootTable.add(playButton).padTop(50).width(100);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

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
