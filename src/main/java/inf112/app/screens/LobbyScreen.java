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
import java.util.UUID;

public class LobbyScreen implements Screen {
    Game game;
    GameClient client;
    Stage stage;
    Table names;
    ButtonGroup robots;

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

    public LobbyScreen(Game game, GameClient client) {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        Preferences prefs = Gdx.app.getPreferences("RoboRally");

        names = new Table();
        names.setBounds(50,300,100,500);
        stage.addActor(names);

        Texture robotTexture = new Texture("Robots.png");

        robot = TextureRegion.split(robotTexture,300,300);

        robots = new ButtonGroup();

        Table table = new Table();
        stage.addActor(table);

        selection = new Table();
        table.add(selection);

        drawRobotSelection(prefs.getInteger("lastRed"),prefs.getInteger("lastGreen"),prefs.getInteger("lastBlue"));

        table.row();
        Label redValue = new Label(prefs.getString("lastRed"),RoboRally.skin);

        Slider red = new Slider(0,255,1,false,RoboRally.skin);
        red.setValue(prefs.getInteger("lastRed"));

        table.add(red);
        table.add(redValue);

        table.row();
        Label greenValue = new Label(prefs.getString("lastGreen"),RoboRally.skin);

        Slider green = new Slider(0,255,1,false,RoboRally.skin);
        green.setValue(prefs.getInteger("lastGreen"));

        table.add(green);
        table.add(greenValue);

        table.row();
        Label blueValue = new Label(prefs.getString("lastRed"),RoboRally.skin);

        Slider blue = new Slider(0,255,1,false,RoboRally.skin);
        blue.setValue(prefs.getInteger("lastBlue"));

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

        table.setPosition(1000,500);

        TextButton playButton = new TextButton("Ready", RoboRally.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/10);
        playButton.setPosition(Gdx.graphics.getWidth()/10-playButton.getWidth()/10,Gdx.graphics.getHeight()/10-playButton.getHeight()/10);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                client.readyForGame();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);

        Robot playerRobot = client.getPlayerList().get(client.clientUUID).getRobot();
        playerRobot.setRed(prefs.getInteger("lastRed"));
        playerRobot.setGreen(prefs.getInteger("lastGreen"));
        playerRobot.setBlue(prefs.getInteger("lastBlue"));
        client.updatePlayer();

        FileHandle directory = Gdx.files.internal("assets");
        maps = new Array<>();

        for (FileHandle file : directory.list()) {
            if (file.extension().equals("tmx")) {
                maps.add(file.name());
            }
        }

        Table mapSelection = new Table();
        mapSelection.setFillParent(true);
        stage.addActor(mapSelection);

        list = new List<>(RoboRally.skin);
        list.setItems(maps);

        voteTallies = new Table();
        for (Object ignored : list.getItems()) {
            Label label = new Label("0",RoboRally.skin);
            voteTallies.add(label);
            voteTallies.row();
        }

        mapSelection.add(voteTallies);
        mapSelection.add(list).pad(5);

        selected = list.getSelected();

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(selected);

        camera = new OrthographicCamera();
        int size = Math.max((int) tiledMap.getProperties().get("width"),(int) tiledMap.getProperties().get("height"));
        camera.setToOrtho(false, size, size);

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        mapPort = new StretchViewport(5, 5, camera);
        mapSelection.add(new ViewportWidget(mapPort)).height(300).prefWidth(300);

        mapSelection.row();

        TextButton voteButton = new TextButton("Vote", RoboRally.skin);
        voteButton.setWidth(Gdx.graphics.getWidth()/10);
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
        mapSelection.add(voteButton);
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
            if (tally == null) {
                ((Label) voteTallies.getChildren().get(i)).setText(0);
            } else {
                ((Label) voteTallies.getChildren().get(i)).setText(tally);
            }
            i++;
        }

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

        if(client.run) {
            game.setScreen(new GameScreen(game, client));
        }
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

    public void drawRobotSelection(int r, int g, int b) {
        TextureRegion[][] robot = ColorTexture.colorRobot(this.robot,new Color(r/256F,g/256F,b/256F,1));
        Table table = new Table();
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
            table.add(button).width(150);
        }

        selection.reset();
        selection.add(table);
    }
}
