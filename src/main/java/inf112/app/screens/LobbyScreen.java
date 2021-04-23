package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import inf112.app.*;
import inf112.app.networking.GameClient;
import inf112.app.networking.Network;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LobbyScreen implements Screen {
    Game game;
    GameClient client;
    Stage stage;
    Table names;
    ButtonGroup<ImageButton> robots;

    TextureRegion[][] robot;
    Map<UUID,TextureRegion[][]> colorTextures;
    Table selection;

    Array<String> maps;
    List<String> list;
    String selected;
    Table voteTallies;

    TmxMapLoader mapLoader;
    TiledMap tiledMap;

    OrthographicCamera camera;
    OrthogonalTiledMapRenderer renderer;
    StretchViewport mapPort;

    ViewportWidget vpw;

    public LobbyScreen(Game game, GameClient client) {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        Preferences prefs = Gdx.app.getPreferences("RoboRally");

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        names = new Table();
        rootTable.add(names).padRight(50);

        Texture robotTexture = new Texture("Robots.png");

        robot = TextureRegion.split(robotTexture,300,300);

        robots = new ButtonGroup<>();

        Table robotCustom = new Table();
        rootTable.add(robotCustom);

        selection = new Table();
        robotCustom.add(selection);
        Table table = new Table();
        robotCustom.row();
        robotCustom.add(table);

        drawRobotSelection(prefs.getInteger("lastRed"),prefs.getInteger("lastGreen"),prefs.getInteger("lastBlue"));

        Label redValue = new Label(prefs.getString("lastRed"),RoboRally.skin);

        Slider red = new Slider(0,255,1,false,RoboRally.skin);
        red.setValue(prefs.getInteger("lastRed"));

        table.add(new Label("Red",RoboRally.skin)).padRight(10);
        table.add(red);
        table.add(redValue);

        table.row();
        Label greenValue = new Label(prefs.getString("lastGreen"),RoboRally.skin);

        Slider green = new Slider(0,255,1,false,RoboRally.skin);
        green.setValue(prefs.getInteger("lastGreen"));


        table.add(new Label("Green",RoboRally.skin)).padRight(10);
        table.add(green);
        table.add(greenValue);

        table.row();
        Label blueValue = new Label(prefs.getString("lastRed"),RoboRally.skin);

        Slider blue = new Slider(0,255,1,false,RoboRally.skin);
        blue.setValue(prefs.getInteger("lastBlue"));

        table.add(new Label("Blue",RoboRally.skin)).padRight(10);
        table.add(blue);
        table.add(blueValue);

        colorTextures = new HashMap<>();

        red.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                client.updatePlayer();
                prefs.putInteger("lastRed",(int) red.getValue());
                prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                client.getPlayerList().get(client.clientUUID).getRobot().setRed((int) red.getValue());
                redValue.setText((int) red.getValue());
                drawRobotSelection((int) red.getValue(),(int) green.getValue(),(int) blue.getValue());
            }
        });

        green.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                client.updatePlayer();
                prefs.putInteger("lastGreen",(int) green.getValue());
                prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                client.getPlayerList().get(client.clientUUID).getRobot().setGreen((int) green.getValue());
                greenValue.setText((int) green.getValue());
                drawRobotSelection((int) red.getValue(),(int) green.getValue(),(int) blue.getValue());
            }
        });

        blue.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                client.updatePlayer();
                prefs.putInteger("lastBlue",(int) blue.getValue());
                prefs.flush();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                client.getPlayerList().get(client.clientUUID).getRobot().setBlue((int) blue.getValue());
                blueValue.setText((int) blue.getValue());
                drawRobotSelection((int) red.getValue(),(int) green.getValue(),(int) blue.getValue());
            }
        });

        robots.setMinCheckCount(1);
        robots.setMaxCheckCount(1);

        Robot playerRobot = client.getPlayerList().get(client.clientUUID).getRobot();
        playerRobot.setRed(prefs.getInteger("lastRed"));
        playerRobot.setGreen(prefs.getInteger("lastGreen"));
        playerRobot.setBlue(prefs.getInteger("lastBlue"));
        client.updatePlayer();

        TextButton readyButton = new TextButton("Ready", RoboRally.skin);
        readyButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                client.readyForGame();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        rootTable.row();
        rootTable.add(readyButton).bottom();

        FileHandle directory = Gdx.files.internal("assets");
        maps = new Array<>();

        for (FileHandle file : directory.list()) {
            if (file.extension().equals("tmx")) {
                maps.add(file.name());
            }
        }

        Table mapSelection = new Table();
        rootTable.add(mapSelection);

        list = new List<>(RoboRally.skin);
        list.setItems(maps);

        voteTallies = new Table();
        for (Object ignored : list.getItems()) {
            Label label = new Label("0",RoboRally.skin);
            voteTallies.add(label);
            voteTallies.row();
        }

        Table mapSelectionName = new Table();

        mapSelectionName.add(voteTallies).padRight(5);
        mapSelectionName.add(list);
        ScrollPane sc = new ScrollPane(mapSelectionName,RoboRally.skin);

        Table left = new Table();
        left.add(sc);
        mapSelection.add(left).pad(5);

        selected = list.getSelected();

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(selected);

        camera = new OrthographicCamera();
        int size = Math.max((int) tiledMap.getProperties().get("width"),(int) tiledMap.getProperties().get("height"));
        camera.setToOrtho(false, size, size);

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        mapPort = new StretchViewport(5, 5, camera);
        vpw = new ViewportWidget(mapPort);
        mapSelection.add(vpw).prefHeight(300).prefWidth(300);

        TextButton voteButton = new TextButton("Vote", RoboRally.skin);
        voteButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Network.MapVote vote = new Network.MapVote();
                vote.mapName = list.getSelected();
                client.getClient().sendTCP(vote);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        left.row();
        left.add(voteButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        stage.act();
        stage.draw();

        for (UUID uuid : client.remove) {
            client.getPlayerList().remove(uuid);
            client.remove.remove(uuid);
        }

        names.reset();
        for (Map.Entry<UUID,Player> entry : client.getPlayerList().entrySet()) {
            Player player = entry.getValue();
            names.add(new Label(player.getName(),RoboRally.skin));
            Robot rob = player.getRobot();
            int tr = rob.getTexture();
            Image image = new Image(ColorTexture.colorRobot(robot,new Color(rob.getRed()/256F,rob.getGreen()/256F,rob.getBlue()/256F,1))[0][tr]);
            names.add(image).width(50).height(50);
            if (client.ready.contains(entry.getKey())) {
                names.add(new Image(new Texture(Gdx.files.internal("default/raw/check-on.png"))));
            }
            names.row();
        }

        int i = 0;
        for (String map : maps) {
            Integer tally = client.getMapVotes().get(map);
            ((Label) voteTallies.getChildren().get(i)).setText(Objects.requireNonNullElse(tally, 0));
            i++;
        }

        if(!selected.equals(list.getSelected())) {
            selected = list.getSelected();
            tiledMap = mapLoader.load(selected);
            renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
            int size = Math.max((int) tiledMap.getProperties().get("width"),(int) tiledMap.getProperties().get("height"));
            camera.setToOrtho(false, size, size);
            renderer.setView(camera);
        }

        mapPort.apply();
        renderer.render();

        if(client.run) {
            game.setScreen(new GameScreen(game, client));
        }
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i,i1,true);
        stage.getViewport().getCamera().update();

        vpw.setWidth(vpw.getHeight());
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

    public void drawRobotSelection(int r, int g, int b) {
        TextureRegion[][] robot = ColorTexture.colorRobot(this.robot,new Color(r/256F,g/256F,b/256F,1));
        Table table = new Table();

        robots = new ButtonGroup<>();
        robots.setMinCheckCount(1);
        robots.setMaxCheckCount(1);

        for (TextureRegion tr : robot[0]) {
            ImageButton button = new ImageButton(new Image(tr).getDrawable());
            button.addListener(new InputListener(){
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    client.getPlayerList().get(client.clientUUID).getRobot().setTexture(robots.getCheckedIndex());
                    client.updatePlayer();
                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });

            robots.add(button);
            table.add(button).width(150).height(150);
        }

        selection.reset();
        selection.add(table);
    }
}
