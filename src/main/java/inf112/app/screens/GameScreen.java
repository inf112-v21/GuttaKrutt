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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.*;
import inf112.app.logic.*;
import inf112.app.networking.GameClient;

import java.util.Iterator;
import java.util.Map;
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

    TextureRegion[][] playerTextures;

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

    Table cardSelectTable;

    DragAndDrop dragAndDrop;

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

        Texture playerTexture = new Texture("Robot1.png");
        playerTextures = TextureRegion.split(playerTexture,300,300);

        robotsTable = new Table();
        stage.addActor(robotsTable);
        robotsTable.setSize(uiWidth,Gdx.graphics.getHeight()-uiWidth);
        robotsTable.setPosition(Gdx.graphics.getWidth()-uiWidth, uiHeight);

        robotsTable.setDebug(true);

        for (Player player : players.values()) {
            Robot robot = player.getRobot();
            Image img = new Image(playerTextures[0][0]);
            img.addListener(new TextTooltip(robot.getDamage() + "", RoboRally.skin));
            robotsTable.add(img).height(uiWidth);
            robotsTable.row();
            robotsTable.add(new Table());

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

        dragAndDrop = new DragAndDrop();

        cardSelectTable = new Table();
        cardSelectTable.setSize(260,Gdx.graphics.getHeight()-uiHeight-20);
        cardSelectTable.setPosition(Gdx.graphics.getWidth()-uiWidth-270,uiHeight+10);
        stage.addActor(cardSelectTable);

        controlsTable = new Table();
        stage.addActor(controlsTable);
        controlsTable.setSize(Gdx.graphics.getWidth(),uiHeight);

        controlsTable.setDebug(true);

        PRTable = new Table();

        controlsTable.add(PRTable);

        TextButton button = new TextButton("Edit",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //game.setScreen(new CardSelectScreen(game, players.get(clientUUID), thisScreen));
                makeCardsTable();
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

        PRTable.reset();
        PRTable.add(drawRegister(players.get(clientUUID),true));

        int i = 0;
        for (Player player : players.values()) {
            ((TextTooltip) robotsTable.getChildren().get(i*2).getListeners().get(0)).getActor().setText(player.getRobot().getDamage() + "\n" + player.getName());
            robotsTable.getChildren().set(i*2+1, drawRegister(player));
            i++;
        }

        stage.act();
        stage.draw();
    }

    public void drawRobots() {
        clearLayer(playerLayer);
        for (Player player : players.values()) {
            Robot robot = player.getRobot();
            TiledMapTileLayer.Cell currentPlayerCell = new TiledMapTileLayer.Cell();;
            int[] index = robot.getTexture();
            currentPlayerCell.setTile(new StaticTiledMapTile(playerTextures[index[0]][index[1]]));

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

    public Table drawRegister(Player player) {
        return drawRegister(player,false);
    }

    public Table drawRegister(Player player, boolean main) {
        Table table = new Table();
        for (int i=0; i<5; i++) {
            Card card = player.getRobot().getProgramRegister()[i];
            Image image;
            if (card != null) {
                image = new Image(card.draw());
            } else {
                image = new Image(new Texture(Gdx.files.internal("Card_Base.png")));
                image.setColor(Color.GRAY);
            }
            if (i >= 9 - player.getRobot().getDamage()) {
                image.setColor(Color.GRAY);
            }
            if (card == gameLogic.getCurrentCard() && card != null) {
                image.setColor(Color.YELLOW);
            }
            table.add(image);
            if (main) {
                dragAndDrop.addTarget(new CustomTarget(image, players.get(clientUUID), i));
            }
        }
        return table;
    }

    public class CustomTarget extends DragAndDrop.Target {
        int pos;
        Player player;

        public CustomTarget(Image image, Player player, int pos) {
            super(image);
            this.player = player;
            this.pos = pos;
        }

        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            getActor().setColor(Color.GREEN);
            return true;
        }

        public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
            if (player.getRobot().getProgramRegister()[pos] == null) {
                getActor().setColor(Color.GRAY);
            } else {
                getActor().setColor(Color.WHITE);
            }
        }

        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            if (pos < 9 - player.getRobot().getDamage()) {
                Card card = ((Card) payload.getObject());
                ((Image) getActor()).setDrawable(new Image(card.draw()).getDrawable());
                int i = player.getCards().indexOf(card);
                player.getCards().remove(card);

                if (player.getRobot().getProgramRegister()[pos] != null) {
                    player.getCards().add(player.getRobot().getProgramRegister()[pos]);
                }

                makeCardsTable();

                Card[] cards = player.getRobot().getProgramRegister();
                cards[pos] = card;

                player.getRobot().setProgramRegister(cards);
            }
        }
    }

    public void makeCardsTable() {
        cardSelectTable.reset();

        Table cardsTable = new Table();
        cardsTable.left();
        ScrollPane sc = new ScrollPane(cardsTable,RoboRally.skin);
        sc.setScrollbarsVisible(true);

        cardSelectTable.add(sc);
        cardSelectTable.row();

        TextButton button = new TextButton("Done",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                cardSelectTable.reset();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        cardSelectTable.add(button);

        int i = 0;
        for (Card card : players.get(clientUUID).getCards()) {
            if (i % 3 == 0) {
                cardsTable.row();
            }

            Image image = new Image(card.draw());
            cardsTable.add(image).width(82).height(131).pad(2);

            dragAndDrop.addSource(new DragAndDrop.Source(image) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(card);

                    payload.setDragActor(new Image(card.draw()));
                    return payload;
                }
            });
            i++;
        }
    }
}
