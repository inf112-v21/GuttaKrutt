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

import java.util.HashMap;
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
    Map<UUID,TextureRegion[][]> colorTextures;


    MapLayers layers;

    Map<UUID,Player> players;
    UUID clientUUID;

    public BoardLogic boardLogic;
    GameLogic gameLogic;

    GameClient client;

    FillViewport gamePort;

    int uiWidth;
    int uiHeight;

    Table rootTable;

    Table robotsTable;
    Table controlsTable;
    Table PRTable;

    Window cardSelectTable;

    DragAndDrop cardSelectDnD;
    DragAndDrop cardSwitchDnD;

    Thread roundThread;
    boolean roundRunning;



    public GameScreen(Game game, GameClient client) {
        this.game = game;
        stage = new Stage(new ScreenViewport()) {
            @Override
            public boolean keyUp(int keyCode) {
                return boardLogic.keyUp(keyCode);
            }
        };

        rootTable = new Table();
        stage.addActor(rootTable);
        rootTable.setFillParent(true);

        this.client = client;

        String map = client.mapName;

        System.out.println(map);

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

        players = client.getPlayerList();
        clientUUID = client.clientUUID;

        boardLogic = new BoardLogic(tiledMap, client.getPlayerList());

        gameLogic = new GameLogic(boardLogic, client);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        uiWidth = 100;
        uiHeight = 200;

        gamePort = new FillViewport((Gdx.graphics.getWidth()- uiWidth)/200F,
                (Gdx.graphics.getHeight()- uiHeight)/200F,camera);
        ViewportWidget vpw = new ViewportWidget(gamePort);
        rootTable.add(vpw).prefWidth(10000).prefHeight(10000);//.width(Gdx.graphics.getWidth()-uiWidth).height(Gdx.graphics.getHeight()-uiHeight);
        vpw.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                camera.translate(-getDeltaX() / 150F * camera.zoom, -getDeltaY() / 150F * camera.zoom);
            }
        });
        stage.addListener(new DragListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                camera.zoom += amount/5F;
                return true;
            }
        });

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        Texture playerTexture = new Texture("Robots.png");
        playerTextures = TextureRegion.split(playerTexture,300,300);

        colorTextures = new HashMap<>();

        for (UUID uuid : players.keySet()) {
            Robot robot = players.get(uuid).getRobot();
            colorTextures.put(uuid,ColorTexture.colorRobot(playerTexture,new Color(robot.getRed()/256F,robot.getGreen()/256F,robot.getBlue()/256F,1)));
        }

        robotsTable = new Table();
        rootTable.add(robotsTable).width(uiWidth).top();

        robotsTable.setDebug(true);

        for (Map.Entry<UUID,Player> entry : players.entrySet()) {
            Robot robot = entry.getValue().getRobot();
            Image img = new Image(colorTextures.get(entry.getKey())[0][robot.getTexture()]);
            img.addListener(new TextTooltip(robot.getDamage() + "", RoboRally.skin));
            Table table = new Table();
            //noinspection SuspiciousNameCombination
            table.add(img).height(uiWidth);
            table.row();
            table.add(drawRegister(entry.getValue()));
            robotsTable.add(table);
            robotsTable.row();

            table.setDebug(true);
        }

        cardSelectDnD = new DragAndDrop();
        cardSwitchDnD = new DragAndDrop();

        controlsTable = new Table();
        rootTable.row();
        rootTable.add(controlsTable).height(uiHeight);

        controlsTable.setDebug(true);

        PRTable = new Table();

        controlsTable.add(PRTable);

        TextButton button = new TextButton("Edit",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                makeCardsTable();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);

        button = new TextButton("Power Down",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Thread thread = new Thread(() -> gameLogic.powerDown());
                thread.start();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);

        button = new TextButton("Ready",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                roundThread = new Thread(() -> gameLogic.ready());
                roundThread.start();
                System.out.println(roundThread.getName());
                roundRunning = true;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        controlsTable.add(button);

        cardSelectTable = new Window("Cards", RoboRally.skin);
        cardSelectTable.setSize(450,300);
        cardSelectTable.setPosition(Gdx.graphics.getWidth()/2F-225,uiHeight+1);
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

        if (roundRunning && !roundThread.isAlive()) {
            roundRunning = false;
            if (stage.getActors().contains(cardSelectTable,true)) {
                makeCardsTable();
            }
        }

        drawRobots();
        drawLasers();

        renderer.setView(camera);
        renderer.render();

        //UI
        stage.getViewport().apply();

        PRTable.reset();
        PRTable.add(drawRegister(players.get(clientUUID),true,82,129));

        int i = 0;
        for (Player player : players.values()) {
            Table table = (Table) robotsTable.getChildren().get(i);
            updatePlayerTable(table,player);
            i++;
        }

        stage.act();
        stage.draw();

        //End game if necessary
        if (!client.run) {
            game.setScreen(new EndScreen(game,players.get(client.winner),client));
        }
    }

    public void drawRobots() {
        clearLayer(playerLayer);
        for (Map.Entry<UUID,Player> entry : players.entrySet()) {
            Robot robot = entry.getValue().getRobot();
            TiledMapTileLayer.Cell currentPlayerCell = new TiledMapTileLayer.Cell();
            int index = robot.getTexture();
            currentPlayerCell.setTile(new StaticTiledMapTile(colorTextures.get(entry.getKey())[robot.getDamage()/3][index]));

            currentPlayerCell.setRotation(robot.getRotation());

            playerLayer.setCell((int) robot.getPos().x,(int) robot.getPos().y,currentPlayerCell);
        }
    }

    public void drawLasers() {
        clearLayer(laserLayer);

        for(int i = 0; i<laserLayer.getWidth(); i++) {
            for (int j = 0; j < laserLayer.getHeight(); j++) {
                if(boardLogic.getMap().get("laser")[i][j]!=0)
                    drawLaser(i,j);
            }
        }
    }

    public void drawLaser(int x,int y) {
        TiledMapTileLayer.Cell laser = new TiledMapTileLayer.Cell();
        int typeOfLaser = boardLogic.getMap().get("laser")[x][y];
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
        return drawRegister(player,false,uiWidth/5, (int) (uiWidth/5*1.6));
    }

    public Table drawRegister(Player player, boolean main,int width, int height) {
        Table table = new Table();
        for (int i=0; i<5; i++) {
            Card card = player.getRobot().getProgramRegister()[i];
            Image image;
            if (card == null) {
                image = new Image(new Texture(Gdx.files.internal("Card_Base.png")));
                image.setColor(Color.GRAY);
            }
            else if (!main && gameLogic.getCurrentCard() == null) {
                image = new Image(new Texture(Gdx.files.internal("Card_Base.png")));
            } else {
                image = new Image(card.draw());
            }
            if (i >= 9 - player.getRobot().getDamage()) {
                image.setColor(Color.GRAY);
            }
            if (card == gameLogic.getCurrentCard() && card != null) {
                image.setColor(Color.YELLOW);
            }
            table.add(image).width(width).height(height);
            if (main) {
                cardSelectDnD.addTarget(new CustomTarget(image, players.get(clientUUID), i));
                int finalI = i;
                if (finalI < 9 - player.getRobot().getDamage()) {
                    cardSwitchDnD.addTarget(new DragAndDrop.Target(image) {
                        @Override
                        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float v, float v1, int i) {
                            return true;
                        }

                        @Override
                        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float v, float v1, int i) {
                            Card[] programRegister = player.getRobot().getProgramRegister();
                            Card oldCard = programRegister[finalI];
                            CardAndPlace newCard = (CardAndPlace) payload.getObject();

                            programRegister[finalI] = newCard.card;
                            programRegister[newCard.pos] = oldCard;
                            player.getRobot().setProgramRegister(programRegister);
                        }
                    });
                    if (card != null) {
                        cardSwitchDnD.addSource(new DragAndDrop.Source(image) {
                            @Override
                            public DragAndDrop.Payload dragStart(InputEvent inputEvent, float v, float v1, int i) {
                                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                                payload.setObject(new CardAndPlace(card, finalI));

                                payload.setDragActor(new Image(card.draw()));
                                return payload;
                            }
                        });
                    }
                }
            }
        }
        return table;
    }

    public static class CardAndPlace {
        public Card card;
        public int pos;

        public CardAndPlace(Card card, int pos) {
            this.card = card;
            this.pos = pos;
        }
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
                player.getCards().remove(card);

                if (player.getRobot().getProgramRegister()[pos] != null) {
                    player.getCards().add(player.getRobot().getProgramRegister()[pos]);
                }

                cardSelectTable.remove();
                makeCardsTable();

                Card[] cards = player.getRobot().getProgramRegister();
                cards[pos] = card;

                player.getRobot().setProgramRegister(cards);
            }
        }
    }

    public void makeCardsTable() {
        stage.addActor(cardSelectTable);
        cardSelectTable.reset();

        Stack stack = new Stack();
        Table cardsTable = new Table();
        cardsTable.left();
        stack.add(cardsTable);
        ScrollPane sc = new ScrollPane(stack,RoboRally.skin);
        sc.setScrollbarsVisible(true);

        cardSelectTable.add(sc);

        TextButton button = new TextButton("Close",RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                cardSelectTable.remove();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        Table buttonTable = new Table();
        stack.add(buttonTable);
        buttonTable.top().right();
        buttonTable.add(button);

        Player player = players.get(clientUUID);

        int i = 0;
        for (Card card : player.getCards()) {
            if (i % 5 == 0) {
                cardsTable.row();
            }

            Image image = new Image(card.draw());
            cardsTable.add(image).width(82).height(131).pad(2);

            cardSelectDnD.addSource(new DragAndDrop.Source(image) {
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

        cardSwitchDnD.addTarget(new DragAndDrop.Target(cardSelectTable) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float v, float v1, int i) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float v, float v1, int i) {
                CardAndPlace card = (CardAndPlace) payload.getObject();
                player.getCards().insert(card.card);
                Card[] programRegister = player.getRobot().getProgramRegister();
                programRegister[card.pos] = null;
                player.getRobot().setProgramRegister(programRegister);
                makeCardsTable();
            }
        });
    }

    private void updatePlayerTable(Table table, Player player) {
        TextTooltip tooltip = (TextTooltip) table.getChildren().get(0).getListeners().get(0);
        tooltip.getActor().setText(player.getRobot().getDamage() + "\n" + player.getName());
        table.getChildren().get(1).remove();
        table.row();
        table.add(drawRegister(player));
    }
}
