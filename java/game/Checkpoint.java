package game;

import model.TypeName;

import java.io.IOException;
import java.util.List;

public class Checkpoint extends FieldType {

    private int count;
    private String isOnBoard;

    public Checkpoint() {
        super(TypeName.CHECK_POINT);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBoard() {
        return this.isOnBoard;
    }

    public void setBoard(String board) {
        this.isOnBoard = board;
    }

    private PlayerMat playerMat;

    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }

    public void checkPointAction(int playerNum) throws IOException {
        List<Integer> points = playerMat.getListPoints();
        int point = points.get(playerNum);
        points.set(playerNum, point + 1);
        playerMat.setPoints(points);
    }
}
