package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.RoboRally;
import inf112.app.networking.GameClient;

import java.io.IOException;

public class MenuScreen implements Screen {
    Stage stage;
    Game game;

    public MenuScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Image title = new Image(new Texture(Gdx.files.internal("RoboRallyLogo.png")));
        title.setX(Gdx.graphics.getWidth()/2-(305/2));
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(305);
        stage.addActor(title);

        TextButton hostButton = new TextButton("Host", RoboRally.skin);
        hostButton.setWidth(Gdx.graphics.getWidth()/2);
        hostButton.setPosition(Gdx.graphics.getWidth()/2-hostButton.getWidth()/2,Gdx.graphics.getHeight()/2-hostButton.getHeight()/2);
        hostButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SetupScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(hostButton);

        TextButton joinButton = new TextButton("Join", RoboRally.skin);
        joinButton.setWidth(Gdx.graphics.getWidth()/2);
        joinButton.setPosition(Gdx.graphics.getWidth()/2-hostButton.getWidth()/2,Gdx.graphics.getHeight()/2-hostButton.getHeight()/2-100);
        joinButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new JoinScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(joinButton);
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
        stage.dispose();
    }
}
