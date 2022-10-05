package game;

import java.util.List;

/**
 * This class represents a field position of the games map. A field position specifies
 * - the position on the map (can be transformed in x/y coordinates)
 * - 1 or more field types (e.g. wall, laser, energy...)
 */
public class FieldPosition {
    Integer position;
    String boardName;
    List<FieldType> field;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String name) {
        this.boardName = boardName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<FieldType> getField() {
        return field;
    }

    public void setField(List<FieldType> field) {
        this.field = field;
    }
}
