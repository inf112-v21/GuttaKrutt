package inf112.skeleton.app;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
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

import java.util.ArrayList;

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

    Robot[] robots;
    Player[] players;
    Controls controls;

    FillViewport gamePort;

    int uiWidth;
    int uiHeight;

    Table robotsTable;
    Table controlsTable;
    Table PRTable;

    public GameScreen(Game game, Robot[] robots, String map) {
        this.game = game;
        stage = new Stage(new ScreenViewport()) {
            @Override
            public boolean keyUp(int keyCode) {
                return controls.keyUp(keyCode);
            }
        };

        players = new Player[robots.length];
        for (int i=0;i<players.length;i++) {
            players[i] = new Player();
            players[i].cardList = new ArrayList<Card>();
            players[i].cardList.add(new Card(Card.CardType.MOVE1,123));
            players[i].cardList.add(new Card(Card.CardType.MOVE2,456));
            players[i].cardList.add(new Card(Card.CardType.MOVE3,789));
            players[i].cardList.add(new Card(Card.CardType.UTURN,1));
            players[i].cardList.add(new Card(Card.CardType.ROTLEFT,123));
            players[i].cardList.add(new Card(Card.CardType.ROTRIGHT,123));
            players[i].cardList.add(new Card(Card.CardType.BACKUP,123));

        }

        for (int i=0;i<robots.length;i++) {
            robots[i] = players[i].getRobot();
        }

        this.robots = robots;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        gamePort = new FillViewport((Gdx.graphics.getWidth()- uiWidth)/200,
                (Gdx.graphics.getHeight()- uiHeight)/200,camera);

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

        controls = new Controls(new MatrixMapGenerator().fromTiledMap(tiledMap).getMap(), robots);

        robotsTable = new Table();
        stage.addActor(robotsTable);
        robotsTable.setSize(uiWidth,Gdx.graphics.getHeight()-uiWidth);
        robotsTable.setPosition(Gdx.graphics.getWidth()-uiWidth, uiHeight);

        robotsTable.setDebug(true);

        for (Robot robot : robots) {
            Image img = new Image(playerTextures[0][0]);
            img.addListener(new TextTooltip(robot.getDamage() + "", RoboRally.skin));
            robotsTable.add(img).height(uiWidth);
            robotsTable.row();
        }
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
                game.setScreen(new CardSelectScreen(game, players, thisScreen));
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
        for (Card card : players[0].getRobot().programRegister) {
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

        int i = 0;
        for (Actor robot : robotsTable.getChildren()) {
            ((TextTooltip) robot.getListeners().get(0)).getActor().setText(robots[i].getDamage());
            i++;
        }

        stage.act();
        stage.draw();
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

        for(int i = 0; i<laserLayer.getWidth(); i++) {
            for (int j = 0; j < laserLayer.getHeight(); j++) {
                if(controls.map[4][i][j]!=0)
                    drawLaser(i,j);
            }
        }
    }

    public void drawLaser(int x,int y) {
        TiledMapTileLayer.Cell laser = new TiledMapTileLayer.Cell();
        int typeOfLaser = controls.map[4][x][y];
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
        for (int i=0;i<5;i++) {
            for (int j=0;j<5;j++) {
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
