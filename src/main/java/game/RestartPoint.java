package game;

import model.TypeName;

/**
 * This class contains relevant information of a restart point:
 * - restart point itself and its orientation
 */

public class RestartPoint extends FieldType {

    private String[] orientations;
    private String isOnBoard;

    public RestartPoint() {
        super(TypeName.RESTART_POINT);
    }

    public String[] getOrientation() {
        return orientations;
    }

    public void setOrientation(String[] orientations) {
        this.orientations = orientations;
    }

    public String getBoard() {
        return this.isOnBoard;
    }

    public void setBoard(String board) {
        this.isOnBoard = board;
    }
}
