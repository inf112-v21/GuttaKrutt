package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import java.util.concurrent.TimeUnit;

public class GUI implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;

    TiledMap tiledMap;
    TiledMapTileLayer boardLayer;
    TiledMapTileLayer holeLayer;
    TiledMapTileLayer flagLayer;
    TiledMapTileLayer playerLayer;
    TiledMapTileLayer wallLayer;
    TiledMapTileLayer laserLayer;

    public int mapWidth;
    public int mapHeight;

    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    TiledMapTileLayer.Cell playerCell;
    TiledMapTileLayer.Cell playerDiedCell;
    TiledMapTileLayer.Cell playerWonCell;

    MapLayers layers;

    Robot[] robots;
    Controls controls;

    public GUI(Robot[] robots) {
        this.robots = robots;
    }

    @Override
    public void create() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("TiledTest.tmx");

        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);

        layers = tiledMap.getLayers();

        boardLayer = getTileLayer("board");
        holeLayer = getTileLayer("hole");
        flagLayer = getTileLayer("flag");
        playerLayer = getTileLayer("player");
        wallLayer = getTileLayer("wall");
        laserLayer = getTileLayer("laser");

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

        int[][][] matrixMap = new int[3][5][5];

        matrixMap[1][2][2] = 6;
        matrixMap[2][4][4] = 55;

        controls = new Controls(tiledMap, robots, this);

        Gdx.input.setInputProcessor(controls);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    public TiledMapTileLayer getTileLayer(String s) {
        return (TiledMapTileLayer) layers.get(s);
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

        drawRobots();
        drawLasers();

        renderer.render();
    }

    public void drawRobots() {
        clearLayer(playerLayer);
        for (Robot robot : robots) {
            TiledMapTileLayer.Cell currentPlayerCell = playerCell;
            if (!robot.getAlive()) {currentPlayerCell = playerDiedCell;}
            if (robot.getWon()) {currentPlayerCell = playerWonCell;}

            currentPlayerCell.setRotation(robot.getRotation());

            playerLayer.setCell((int) robot.getPos().x,(int) robot.getPos().y,currentPlayerCell);
        }
    }

    public void drawLasers() {
        clearLayer(laserLayer);
        if (robots[0].fireLaser > 0) {
            int x = (int) robots[0].getPos().x;
            int y = (int) robots[0].getPos().y;
            switch (robots[0].getRotation()){
                case 0: y++; break;
                case 1: x--; break;
                case 2: y--; break;
                case 3: x++; break;
                default: y += 10; break;
            }

            drawLaser(x,y,robots[0].getRotation());

            robots[0].fireLaser--;
        }
    }

    public void drawLaser(int x,int y, int dir) {
        TiledMapTileLayer.Cell laser = new TiledMapTileLayer.Cell();
        laser.setTile(tiledMap.getTileSets().getTile(47));
        if (dir == 1 || dir == 3) {
            laser.setTile(tiledMap.getTileSets().getTile(39));
        }
        boolean[] test = controls.getWall(x,y);
        if (test[(dir + 2) % 4]) {
            return;
        }

        laserLayer.setCell(x, y, laser);

        switch (dir){
            case 0: y++; break;
            case 1: x--; break;
            case 2: y--; break;
            case 3: x++; break;
            default: y += 10; break;
        }

        if (!test[dir] && y < 5 && y >= 0 && x < 5 && x >= 0) {
            drawLaser(x,y,dir);
        }
    }

    public void clearLayer(TiledMapTileLayer layer) {
        for (int i=0;i<5;i++) {
            for (int j=0;j<5;j++) {
                layer.setCell(i,j,null);
            }
        }
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
