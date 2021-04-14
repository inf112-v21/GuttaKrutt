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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.app.Player;
import inf112.app.RoboRally;
import inf112.app.networking.GameClient;
import inf112.app.networking.GameServer;
import inf112.app.networking.Network;
import org.lwjgl.system.CallbackI;

import java.util.Map;
import java.util.UUID;

public class LobbyScreen implements Screen {
    Game game;
    GameClient client;
    Stage stage;
    Table names;
    ButtonGroup robots;
    Label label;

    TextureRegion[][] robot;

    public LobbyScreen(Game game, GameClient client) {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        names = new Table();
        names.setBounds(50,300,100,500);
        stage.addActor(names);

        Texture robotTexture = new Texture("Robot1.png");

        robot = TextureRegion.split(robotTexture,300,300);

        robots = new ButtonGroup();

        Table table = new Table();
        stage.addActor(table);

        for (TextureRegion[] tr : robot) {
            ImageButton button = new ImageButton(new Image(tr[0]).getDrawable());
            button.addListener(new InputListener(){
               @Override
               public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                   client.getPlayerList().get(client.clientUUID).getRobot().setTexture(new int[]{robots.getCheckedIndex(),0});
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

        robots.setMinCheckCount(1);
        robots.setMaxCheckCount(1);

        table.setPosition(1000,500);

        label = new Label("",RoboRally.skin);
        table.add(label);

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

        names.reset();
        for (Map.Entry<UUID,Player> entry : client.getPlayerList().entrySet()) {
            Player player = entry.getValue();
            names.add(new Label(player.getName(),RoboRally.skin));
            int[] tr = player.getRobot().getTexture();
            if (tr != null) {
                Image image = new Image(robot[tr[0]][tr[1]]);
                names.add(image).width(50).height(50);
                if (client.ready.contains(entry.getKey())) {
                    names.add(new Image(new Texture(Gdx.files.internal("default/raw/check-on.png"))));
                }
            }
            names.row();
        }

        label.setText(robots.getCheckedIndex());

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
}
