package game;

import model.TypeName;

/**
 * This class contains relevant information of an Antenna:
 * - Antenna itself and its orientation
 */
public class Antenna extends FieldType {

    private String[] orientation;

    private String isOnBoard;

    public Antenna() {
        super(TypeName.ANTENNA);
    }

    public String getBoard() {
        return this.isOnBoard;
    }

    public void setBoard(String board) {
        this.isOnBoard = board;
    }

    public String[] getOrientation() {
        return orientation;
    }

    public void setOrientation(String[] orientation) {
        this.orientation = orientation;
    }
}
