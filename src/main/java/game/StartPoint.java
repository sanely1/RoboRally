package game;

import model.TypeName;

/**
 * This class contains relevant information of a start point:
 * - start point itself
 */

public class StartPoint extends FieldType {
    private String isOnBoard;

    public StartPoint() {
        super(TypeName.START_POINT);
    }

    public String getBoard() {
        return this.isOnBoard;
    }

    public void setBoard(String board) {
        this.isOnBoard = board;
    }

}
