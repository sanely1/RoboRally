package game;

import model.TypeName;

/**
 * This is the class of an empty field
 */


public class Empty extends FieldType {
    private String isOnBoard;

    public Empty() {
        super(TypeName.EMPTY);
    }

    public String getBoard() {
        return this.isOnBoard;
    }

    public void setBoard(String board) {
        this.isOnBoard = board;
    }

}
