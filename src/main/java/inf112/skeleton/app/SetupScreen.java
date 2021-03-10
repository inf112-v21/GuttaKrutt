package inf112.skeleton.app;

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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

public class SetupScreen implements Screen {
    Stage stage;
    ArrayList<String> maps;
    String selected;

    List list;

    TmxMapLoader mapLoader;
    TiledMap tiledMap;

    OrthographicCamera camera;
    OrthogonalTiledMapRenderer renderer;
    StretchViewport mapPort;

    public SetupScreen(Game game) {
        stage = new Stage(new ScreenViewport());

        FileHandle directory = Gdx.files.internal("assets");

        maps = new ArrayList<>();

        for (FileHandle file : directory.list()) {
            if (file.extension().equals("tmx")) {
                maps.add(file.name());
                System.out.println(file.name());
            }
        }

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        list = new List(RoboRally.skin);
        list.setItems(maps.toArray());
        table.add(list);

        selected = (String) list.getSelected();

        SelectBox robotsList = new SelectBox(RoboRally.skin);
        Array<Integer> numbers = new Array();
        numbers.addAll(new Integer[]{1,2,3,4,5,6,7,8},0,8);

        robotsList.setItems(numbers);
        table.add(robotsList);


        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(selected);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        mapPort = new StretchViewport(5, 5, camera);
        mapPort.setScreenBounds(Gdx.graphics.getWidth()-500, 300, 300, 300);

        TextButton playButton = new TextButton("Play!", RoboRally.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/10);
        playButton.setPosition(Gdx.graphics.getWidth()/10-playButton.getWidth()/10,Gdx.graphics.getHeight()/10-playButton.getHeight()/10);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game, (int) robotsList.getSelected(), selected));
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
            selected = (String) list.getSelected();
            tiledMap = mapLoader.load(selected);
            renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
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
