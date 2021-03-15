package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.RoboRally;
import inf112.app.networking.GameClient;
import inf112.app.networking.GameServer;

import java.io.IOException;

public class JoinScreen implements Screen {
    Stage stage;
    Game game;

    public JoinScreen(Game game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());

        TextField host = new TextField("host", RoboRally.skin);
        host.setWidth(Gdx.graphics.getWidth()/2);
        host.setPosition(Gdx.graphics.getWidth()/2-host.getWidth()/2,Gdx.graphics.getHeight()/2);
        stage.addActor(host);

        TextField name = new TextField("name", RoboRally.skin);
        name.setWidth(Gdx.graphics.getWidth()/2);
        name.setPosition(Gdx.graphics.getWidth()/2-name.getWidth()/2,Gdx.graphics.getHeight()/2-100);
        stage.addActor(name);

        TextButton playButton = new TextButton("Play!", RoboRally.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/10);
        playButton.setPosition(Gdx.graphics.getWidth()/10-playButton.getWidth()/10,Gdx.graphics.getHeight()/10-playButton.getHeight()/10);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameClient client = null;
                try {
                    client = new GameClient(host.getText(),true);
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
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
