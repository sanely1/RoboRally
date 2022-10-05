package game;

import model.TypeName;

/**
 * This class contains relevant information of a laser:
 * - laser itself
 * - its orientation
 * - its count
 */
public class Laser extends FieldType {

    private String[] orientation;
    private int count;
    private String isOnBoard;

    public Laser() {
        super(TypeName.LASER);
    }


    public String[] getOrientation() {
        return orientation;
    }

    public void setOrientation(String[] orientation) {
        this.orientation = orientation;
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
}
