package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class HelloWorld implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;
    private TiledMap map;
    private TiledMapTileLayer board;
    private TiledMapTileLayer flag;
    private TiledMapTileLayer hole;
    private TiledMapTileLayer player;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private TiledMapTileLayer.Cell normal_player;
    private TiledMapTileLayer.Cell winning_player;
    private TiledMapTileLayer.Cell losing_player;
    private Vector2 vector_player;

    @Override
    public void create() {
        map = new TmxMapLoader().load("C:\\Users\\Simon\\IdeaProjects\\G9-1\\assets\\tiledtest.tmx");
        board = (TiledMapTileLayer) map.getLayers().get("Board");
        hole = (TiledMapTileLayer) map.getLayers().get("Hole");
        flag = (TiledMapTileLayer) map.getLayers().get("Flag");
        player = (TiledMapTileLayer) map.getLayers().get("Player");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = (float) 2.25;
        camera.update();
        renderer = new OrthogonalTiledMapRenderer(map, (float) 0.003);
        renderer.setView(camera);

        normal_player = Texture("player.png");

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        renderer.render();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
