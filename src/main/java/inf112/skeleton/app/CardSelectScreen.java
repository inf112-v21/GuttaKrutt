package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

public class CardSelectScreen implements Screen {
    Game game;
    Stage stage;

    List<Card> cardList;

    Table table;
    DragAndDrop dragAndDrop;

    public CardSelectScreen(Game game, Player[] players, Screen prevScreen) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        cardList = players[0].getCardList();

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        final Skin skin = new Skin();
        skin.add("validTargetImage", new Texture(Gdx.files.internal("Card_Base.png")));

        dragAndDrop = new DragAndDrop();

        for (Card card : cardList) {
            Image image = new Image(drawCard(card));
            table.add(image).width(164).height(258);

            dragAndDrop.addSource(new DragAndDrop.Source(image) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(card);

                    payload.setDragActor(new Image(drawCard(card)));
                    return payload;
                }
            });
        }

        Array<CustomTarget> targets = new Array<>();
        int x = 200;
        for (int i=0;i<5;i++) {
            Card card = players[0].getRobot().programRegister[i];
            Image img = new Image(drawCard(card));
            if (card == null) {
                img.setColor(Color.GRAY);
            }
            img.setBounds(x, 0, 164, 258);
            stage.addActor(img);
            CustomTarget target = new CustomTarget(img, card);
            dragAndDrop.addTarget(target);
            targets.add(target);
            x += 170;
        }

        TextButton button = new TextButton("Done", RoboRally.skin);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Card[] cards = new Card[5];
                for (int i = 0; i < 5; i++) {
                    cards[i] = targets.get(i).currentCard;
                    players[0].getRobot().programRegister = cards;
                }
                game.setScreen(prevScreen);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        button.setBounds(5,5,100,50);
        stage.addActor(button);
    }

    public class CustomTarget extends DragAndDrop.Target {
        Card currentCard;
        public CustomTarget(Image image) {
            super(image);
        }

        public CustomTarget(Image image, Card card) {
            super(image);
            this.currentCard = card;
        }

        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            getActor().setColor(Color.GREEN);
            return true;
        }

        public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
            if (currentCard == null) {
                getActor().setColor(Color.GRAY);
            } else {
                getActor().setColor(Color.WHITE);
            }
        }

        public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
            Card card = ((Card) payload.getObject());
            ((Image) getActor()).setDrawable(new Image(drawCard(card)).getDrawable());
            int i = cardList.indexOf(card);
            cardList.remove(card);

            Cell cell = table.getCell(table.getChild(i));
            cell.getActor().remove();
            cell.reset();

            if (currentCard != null) {
                Card card2 = currentCard;
                Image image = new Image(drawCard(card2));
                table.add(image).width(164).height(258);
                cardList.add(card2);

                dragAndDrop.addSource(new DragAndDrop.Source(image) {
                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        DragAndDrop.Payload payload = new DragAndDrop.Payload();
                        payload.setObject(card2);

                        payload.setDragActor(new Image(drawCard(card2)));
                        return payload;
                    }
                });
            }

            currentCard = card;
        }
    }

    public static Texture drawCard(Card card) {
        Pixmap pixmap;
        if (card != null) {
            pixmap = new Pixmap(Gdx.files.internal("Card_" + card.type + ".png"));

            pixmap.drawPixmap(drawNumber(card.priority / 100), 44, 14);
            pixmap.drawPixmap(drawNumber((card.priority / 10) % 100), 52, 14);
            pixmap.drawPixmap(drawNumber((card.priority) % 10), 60, 14);
        } else {
            pixmap = new Pixmap(Gdx.files.internal("Card_Base.png"));
        }
        return new Texture(pixmap);
    }

    public static Pixmap drawNumber(int i) {
        i = i % 10;

        Pixmap pixmap = new Pixmap(6,9,Pixmap.Format.RGBA4444);
        pixmap.setColor(0,1,0,1);

        switch(i) {
            case(1):
                pixmap.drawLine(5,0,5,8);
                break;
            case(2):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,4);
                pixmap.drawLine(5,4,0,4);
                pixmap.drawLine(0,4,0,8);
                pixmap.drawLine(0,8,5,8);
                break;
            case(3):
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(1,4,5,4);
                pixmap.drawLine(0,8,5,8);
                break;
            case(4):
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,4,5,4);
                break;
            case(5):
                pixmap.drawLine(5,0,0,0);
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(5,4,5,8);
                pixmap.drawLine(5,8,0,8);
                break;
            case(6):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(0,8,5,8);
                pixmap.drawLine(5,4,5,8);
                break;
            case(7):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,8);
                break;
            case(8):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(5,4,0,4);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(0,8,5,8);
                break;
            case(9):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(5,0,5,8);
                break;
            default:
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,8,5,8);
                break;
        }

        return pixmap;
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
