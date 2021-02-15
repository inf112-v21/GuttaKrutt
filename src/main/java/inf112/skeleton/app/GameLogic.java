package inf112.skeleton.app;

public class GameLogic {

    int turn;

    public GameLogic() {
        turn=0;
    }

    public void doTurn() {
        turn++;
    }

    public int getTurn() {
        return turn;
    }
}
