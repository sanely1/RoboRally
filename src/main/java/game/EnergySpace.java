package game;

import model.TypeName;

/**
 * This class contains relevant information of an energy space:
 * - energy space itself and its count
 */


public class EnergySpace extends FieldType {
    private String isOnBoard;

    private int count;

    public EnergySpace() {
        super(TypeName.ENERGY_SPACE);
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
