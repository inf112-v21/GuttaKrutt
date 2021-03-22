package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import inf112.app.RoboRally;

import java.io.IOException;

public class SetupScreen implements Screen {
    Stage stage;
    Array<String> maps;
    String selected;

    List<String> list;

    TmxMapLoader mapLoader;
    TiledMap tiledMap;

    OrthographicCamera camera;
    OrthogonalTiledMapRenderer renderer;
    StretchViewport mapPort;

    TextField name;
    Label nameField;
    TextButton playButton;

    public SetupScreen(Game game) {
        stage = new Stage(new ScreenViewport());

        FileHandle directory = Gdx.files.internal("assets");

        maps = new Array<>();

        for (FileHandle file : directory.list()) {
            if (file.extension().equals("tmx")) {
                maps.add(file.name());
                System.out.println(file.name());
            }
        }

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        list = new List<>(RoboRally.skin);
        list.setItems(maps);
        table.add(list);

        selected = list.getSelected();

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(selected);

        camera = new OrthographicCamera();
        int size = Math.max((int) tiledMap.getProperties().get("width"),(int) tiledMap.getProperties().get("height"));
        camera.setToOrtho(false, size, size);

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        mapPort = new StretchViewport(5, 5, camera);
        mapPort.setScreenBounds(Gdx.graphics.getWidth()-500, 300, 300, 300);

        name = new TextField("", RoboRally.skin);
        name.setWidth(Gdx.graphics.getWidth()/2F);
        name.setPosition(Gdx.graphics.getWidth()/2F-name.getWidth()/2-40,40);
        stage.addActor(name);

        nameField = new Label("Name:", RoboRally.skin);
        nameField.setPosition(Gdx.graphics.getWidth()/2F-name.getWidth()/2F-40,name.getHeight()+50);
        stage.addActor(nameField);

        playButton = new TextButton("Play!", RoboRally.skin);
        playButton.setWidth(100);
        playButton.setPosition(Gdx.graphics.getWidth()-40-playButton.getWidth(),40);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameServer server = null;
                GameClient client = null;
                try {
                    server = new GameServer();
                    server.setMap(selected);
                    client = new GameClient("localhost",true);
                    client.getClient().sendTCP(name.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.setScreen(new LobbyScreen(game, client, server));
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

        if(!selected.equals(list.getSelected())) {
            System.out.println(selected + ", " + list.getSelected());
            selected = list.getSelected();
            tiledMap = mapLoader.load(selected);
            renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
            int size = Math.max((int) tiledMap.getProperties().get("width"),(int) tiledMap.getProperties().get("height"));
            camera.setToOrtho(false, size, size);
            renderer.setView(camera);
        }

        mapPort.apply();
        renderer.render();

        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i,i1,true);
        stage.getViewport().getCamera().update();

        mapPort.setScreenBounds(Math.min(Gdx.graphics.getWidth()/4*3,Gdx.graphics.getWidth()-300), Gdx.graphics.getHeight()/2-150, 300, 300);

        name.setWidth(Gdx.graphics.getWidth()/2F);
        name.setPosition(Gdx.graphics.getWidth()/2F-name.getWidth()/2-40,40);

        nameField.setPosition(Gdx.graphics.getWidth()/2F-name.getWidth()/2-40,name.getHeight()+50);

        playButton.setPosition(Gdx.graphics.getWidth()-40-playButton.getWidth(),40);
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
