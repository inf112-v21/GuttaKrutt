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

public class Game extends InputAdapter implements ApplicationListener {
    private SpriteBatch batch;
    private BitmapFont font;

    TiledMap tiledMap;
    TiledMapTileLayer boardLayer;
    TiledMapTileLayer holeLayer;
    TiledMapTileLayer flagLayer;
    TiledMapTileLayer playerLayer;

    int mapWidth;
    int mapHeight;

    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    TiledMapTileLayer.Cell playerCell;
    TiledMapTileLayer.Cell playerDiedCell;
    TiledMapTileLayer.Cell playerWonCell;
    Vector2 playerPos;

    boolean playerAlive;
    boolean playerWon;

    @Override
    public void create() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("TiledTest.tmx");

        //Saving map dimensions for easier checks to see if out of bounds.
        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);

        //Saving the individual layers as variables for easier access.
        // a lot of the logic uses the layers so this saves a lot of code.
        boardLayer = (TiledMapTileLayer) tiledMap.getLayers().get("board");
        holeLayer = (TiledMapTileLayer) tiledMap.getLayers().get("hole");
        flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("flag");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("player");

        //Sets the camera position for Libgdx to render
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 5, 5);
        camera.position.x = 2.5F;

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1F/300);
        renderer.setView(camera);

        Texture playerTexture = new Texture("player.png");

        TextureRegion[][] playerTextures = TextureRegion.split(playerTexture,300,300);

        //Sets all the different expressions of the players as individual TiledMap cells.
        playerCell = new TiledMapTileLayer.Cell();
        playerCell.setTile(new StaticTiledMapTile(playerTextures[0][0]));

        playerDiedCell = new TiledMapTileLayer.Cell();
        playerDiedCell.setTile(new StaticTiledMapTile(playerTextures[0][1]));

        playerWonCell = new TiledMapTileLayer.Cell();
        playerWonCell.setTile(new StaticTiledMapTile(playerTextures[0][2]));

        //Initialises the position of the player to 0,0
        playerPos = new Vector2(0,0);

        //This class doubles as an input processor, sets the input processor to this class.
        Gdx.input.setInputProcessor(this);

        //Initialises the current status of the player
        playerAlive = true;
        playerWon = false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        //Selects the right playerCell depending on what the status of the player is.
        TiledMapTileLayer.Cell currentPlayerCell = playerCell;
        if (!playerAlive) {currentPlayerCell = playerDiedCell;}
        if (playerWon) {currentPlayerCell = playerWonCell;}

        //Sets the playerCell to show in its position.
        // Note: Removing any previous iterations of the playerCell is done in movePlayer().
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
        //Moves the player in any direction using either the arrow keys or WASD.
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

        //Can reset the game by pressing R.
        if (keyCode == Input.Keys.R) {
            create();
        }

        return false;
    }

    /*
    Changes the players position based on the values passed as arguments.
    For example, if x is 1 then the player will be moved one value up in the x-direction.
     */
    public void movePlayer(int x, int y) {
        //Finds new values by adding the old to the new. Values are encouraged to be negative
        // if necessary so the values can easily be decreased as well.
        float newX = playerPos.x + x;
        float newY = playerPos.y + y;

        //Will not move player if the game is over.
        if (!playerAlive || playerWon) {
            System.out.println("Player cannot move, game is over.");
            return;
        }

        //Checks if the player is now out of bounds.
        if (!(newX<0||newY<0||newX>=mapWidth||newY>=mapHeight)) {
            //Sets the current position of the player to a null-cell. This removes the player from the
            // board so there is only one player when the new position is drawn.
            playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);

            //Changes the positions in the position variable for further storage.
            playerPos.x = newX;
            playerPos.y = newY;

            System.out.println("Player moved to " + newX + ", " + newY);

            //This command checks if the new tile does anything to the player.
            checkTile((int)newX, (int)newY);
        } else {
            System.out.println("Player tried to move to " + newX + ", " + newY + ", but it is outside of the map.");
        }
    }
    /*
    Checks if a new tile does anything to the player.
     */
    public void checkTile(int x, int y) {
        //Checks what is in the hole and flag layers.
        TiledMapTileLayer.Cell hole = holeLayer.getCell(x, y);
        TiledMapTileLayer.Cell flag = flagLayer.getCell(x, y);

        //If the cell at the position in the flag layer is anything
        // other than nothing, the player will fall through the hole.
        if (hole != null) {
            playerAlive = false;
            System.out.println("Player has died.");
            System.out.println(hole);
        }

        //If the cell at the position in the flag layer is anything
        // other than nothing, the player will have considered the flag
        // to be found and will win.
        if (flag != null) {
            playerWon = true;
            System.out.println("Player has won.");
            System.out.println(flag);
        }
    }
}
