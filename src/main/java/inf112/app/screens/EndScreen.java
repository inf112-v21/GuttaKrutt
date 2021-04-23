package inf112.app.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.ColorTexture;
import inf112.app.Player;
import inf112.app.RoboRally;
import inf112.app.Robot;
import inf112.app.networking.GameClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EndScreen implements Screen {
    Game game;
    GameClient client;
    Stage stage;

    Map<UUID, TextureRegion[][]> colorTextures;
    Table names;

    public EndScreen(Game game, Player player, GameClient client) {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        colorTextures = new HashMap<>();

        Map<UUID, Player> players = client.getPlayerList();

        for (UUID uuid : players.keySet()) {
            Robot robot = players.get(uuid).getRobot();
            colorTextures.put(uuid, ColorTexture.colorRobot(new Texture("Robots.png"),new Color(robot.getRed()/256F,robot.getGreen()/256F,robot.getBlue()/256F,1)));
        }

        names = new Table();
        rootTable.add(names).padRight(50);

        Label winText = new Label(player.getName() + " has won!",RoboRally.skin);
        winText.setBounds(50,50,300,300);
        rootTable.add(winText);

        TextButton playButton = new TextButton("New Game", RoboRally.skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { client.readyForGame(); }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        rootTable.row();
        rootTable.add(playButton).width(100).padTop(20);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2F, 0.2F, 0.2F, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

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
            Image image = new Image(colorTextures.get(entry.getKey())[0][tr]);
            names.add(image).width(50).height(50);
            if (client.ready.contains(entry.getKey())) {
                names.add(new Image(new Texture(Gdx.files.internal("default/raw/check-on.png"))));
            }
            names.row();
        }

        if(client.winner == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.setScreen(new LobbyScreen(game, client));
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
}
