package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class GUI extends InputAdapter implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;

    TiledMap tiledMap;
    TiledMapTileLayer boardLayer;
    TiledMapTileLayer holeLayer;
    TiledMapTileLayer flagLayer;
    TiledMapTileLayer playerLayer;

    public int mapWidth;
    public int mapHeight;

    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    TiledMapTileLayer.Cell playerCell;
    TiledMapTileLayer.Cell playerDiedCell;
    TiledMapTileLayer.Cell playerWonCell;
    Vector2 playerPos;

    boolean playerAlive;
    boolean playerWon;

    Robot[] robots;

    public GUI(Robot[] robots) {
        this.robots = robots;
    }

    @Override
    public void create() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap tiledMap = mapLoader.load("TiledTest.tmx");

        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);

        boardLayer = (TiledMapTileLayer) tiledMap.getLayers().get("board");
        holeLayer = (TiledMapTileLayer) tiledMap.getLayers().get("hole");
        flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("flag");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("player");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        Texture playerTexture = new Texture("player.png");

        TextureRegion[][] playerTextures = TextureRegion.split(playerTexture,300,300);

        playerCell = new TiledMapTileLayer.Cell();
        playerCell.setTile(new StaticTiledMapTile(playerTextures[0][0]));

        playerDiedCell = new TiledMapTileLayer.Cell();
        playerDiedCell.setTile(new StaticTiledMapTile(playerTextures[0][1]));

        playerWonCell = new TiledMapTileLayer.Cell();
        playerWonCell.setTile(new StaticTiledMapTile(playerTextures[0][2]));

        playerPos = new Vector2(0,0);

        Gdx.input.setInputProcessor(this);

        playerAlive = true;
        playerWon = false;

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

        TiledMapTileLayer.Cell currentPlayerCell = playerCell;
        if (!playerAlive) {currentPlayerCell = playerDiedCell;}
        if (playerWon) {currentPlayerCell = playerWonCell;}

        playerLayer.setCell((int) playerPos.x,(int) playerPos.y,currentPlayerCell);
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

    @Override
    public boolean keyUp(int keyCode) {
        if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) {
            movePlayer(-1,0);
            return true;
        }
        if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) {
            movePlayer(1,0);
            return true;
        }
        if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
            movePlayer(0,-1);
            return true;
        }
        if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
            movePlayer(0,1);
            return true;
        }

        if (keyCode == Input.Keys.R) {
            create();
        }

        return false;
    }

    public void movePlayer(int x, int y) {
        float newX = playerPos.x + x;
        float newY = playerPos.y + y;
        if (!playerAlive || playerWon) {
            System.out.println("inf112.skeleton.app.Player cannot move, game is over.");
            return;
        }
        if (!(newX<0||newY<0||newX>=mapWidth||newY>=mapHeight)) {
            playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
            playerPos.x = newX;
            playerPos.y = newY;

            System.out.println("inf112.skeleton.app.Player moved to " + newX + ", " + newY);

            checkTile((int)newX, (int)newY);
        } else {
            System.out.println("inf112.skeleton.app.Player tried to move to " + newX + ", " + newY + ", but it is outside of the map.");
        }
    }

    public void checkTile(int x, int y) {
        TiledMapTileLayer.Cell hole = holeLayer.getCell(x, y);
        TiledMapTileLayer.Cell flag = flagLayer.getCell(x, y);

        if (hole != null) {
            playerAlive = false;
            System.out.println("inf112.skeleton.app.Player has died.");
            System.out.println(hole);
        }
        if (flag != null) {
            playerWon = true;
            System.out.println("inf112.skeleton.app.Player has won.");
            System.out.println(flag);
        }
    }
}
