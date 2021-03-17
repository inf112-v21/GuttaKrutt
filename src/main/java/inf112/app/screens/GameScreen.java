package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.*;
import inf112.app.logic.*;
import inf112.app.networking.GameClient;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class GameScreen implements Screen {
    Game game;
    Stage stage;

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

    Map<UUID,Player> players;
    UUID clientUUID;

    public BoardLogic boardLogic;
    GameLogic gameLogic;

    GameClient client;

    FillViewport gamePort;

    int uiWidth;
    int uiHeight;

    Table robotsTable;
    Table controlsTable;
    Table PRTable;

    public GameScreen(Game game, GameClient client) {
        this.game = game;
        stage = new Stage(new ScreenViewport()) {
            @Override
            public boolean keyUp(int keyCode) {
                return boardLogic.keyUp(keyCode);
            }
        };

        this.client = client;

        String map = client.mapName;

        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(map);

        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);

        layers = tiledMap.getLayers();

        boardLayer = getTileLayer("board");
        holeLayer = getTileLayer("hole");
        flagLayer = getTileLayer("flag");
        playerLayer = getTileLayer("player");
        wallLayer = getTileLayer("wall");
        laserLayer = getTileLayer("laser");

        gameLogic = new GameLogic(this, client);

        players = gameLogic.getPlayers();
        clientUUID = client.clientUUID;

        boardLogic = new BoardLogic(new MatrixMapGenerator().fromTiledMap(tiledMap).getMap(), client.getPlayerList());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        gamePort = new FillViewport((Gdx.graphics.getWidth()- uiWidth)/200F,
                (Gdx.graphics.getHeight()- uiHeight)/200F,camera);

        uiWidth = 100;
        uiHeight = 200;

        gamePort.setScreenBounds(0, uiHeight,Gdx.graphics.getWidth()- uiWidth,Gdx.graphics.getHeight()- uiHeight);

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

        robotsTable = new Table();
        stage.addActor(robotsTable);
        robotsTable.setSize(uiWidth,Gdx.graphics.getHeight()-uiWidth);
        robotsTable.setPosition(Gdx.graphics.getWidth()-uiWidth, uiHeight);

        robotsTable.setDebug(true);

        //for (Robot robot : robots) {
        //    Image img = new Image(playerTextures[0][0]);
        //    img.addListener(new TextTooltip(robot.getDamage() + "", RoboRally.skin));
        //    robotsTable.add(img).height(uiWidth);
        //    robotsTable.row();
        //}
        stage.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                camera.translate(-getDeltaX() / 150F, -getDeltaY() / 150F);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                camera.zoom += amount/5F;
                return true;
            }
        });

        controlsTable = new Table();
        stage.addActor(controlsTable);
        controlsTable.setSize(Gdx.graphics.getWidth(),uiHeight);

        controlsTable.setDebug(true);

        Screen thisScreen = this;

        PRTable = new Table();

        controlsTable.add(PRTable);

        TextButton button = new TextButton("Edit",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new CardSelectScreen(game, players.get(clientUUID), thisScreen));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);

        button = new TextButton("Do Turn",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Thread thread = new Thread(() -> gameLogic.doTurn());
                thread.start();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);

        button = new TextButton("Submit",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                gameLogic.submit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        PRTable.reset();
        for (Card card : players.get(clientUUID).getRobot().getProgramRegister()) {
            Image image = new Image(CardSelectScreen.drawCard(card));
            if (card == null) {
                image.setColor(Color.GRAY);
            }
            PRTable.add(image);
        }
    }

    public TiledMapTileLayer getTileLayer(String s) {
        return (TiledMapTileLayer) layers.get(s);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        //Board
        gamePort.apply();

        drawRobots();
        drawLasers();

        renderer.setView(camera);
        renderer.render();

        //UI
        stage.getViewport().apply();

        //int i = 0;
        //for (Actor robot : robotsTable.getChildren()) {
        //    ((TextTooltip) robot.getListeners().get(0)).getActor().setText(robots[i].getDamage());
        //    i++;
        //}

        stage.act();
        stage.draw();
    }


    public void drawRobots() {
        clearLayer(playerLayer);
        for (Player player : players.values()) {
            Robot robot = player.getRobot();
            TiledMapTileLayer.Cell currentPlayerCell = playerCell;
            if (!robot.getAlive()) {currentPlayerCell = playerDiedCell;}
            if (robot.getWon()) {currentPlayerCell = playerWonCell;}

            currentPlayerCell.setRotation(robot.getRotation());

            playerLayer.setCell((int) robot.getPos().x,(int) robot.getPos().y,currentPlayerCell);
        }
    }

    public void drawLasers() {
        clearLayer(laserLayer);

        for(int i = 0; i<laserLayer.getWidth(); i++) {
            for (int j = 0; j < laserLayer.getHeight(); j++) {
                if(boardLogic.getMap()[4][i][j]!=0)
                    drawLaser(i,j);
            }
        }
    }

    public void drawLaser(int x,int y) {
        TiledMapTileLayer.Cell laser = new TiledMapTileLayer.Cell();
        int typeOfLaser = boardLogic.getMap()[4][x][y];
        switch (typeOfLaser) {
            case 1:
                laser.setTile(tiledMap.getTileSets().getTile(39));
                break;
            case 2:
                laser.setTile(tiledMap.getTileSets().getTile(47));
                break;
            case 3:
                laser.setTile(tiledMap.getTileSets().getTile(40));
                break;
        }
        laserLayer.setCell(x, y, laser);
    }

    public void clearLayer(TiledMapTileLayer layer) {
        for (int i=0;i<mapWidth;i++) {
            for (int j=0;j<mapHeight;j++) {
                layer.setCell(i,j,null);
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
        gamePort.update(i- uiWidth,i1- uiHeight);
        gamePort.setScreenY(uiHeight);

        stage.getViewport().update(i,i1,true);
        stage.getViewport().getCamera().update();

        robotsTable.setSize(uiWidth,i1-uiWidth);
        robotsTable.setX(i-uiWidth);

        controlsTable.setSize(i,uiHeight);
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
