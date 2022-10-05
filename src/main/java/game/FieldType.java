package game;


/**
 * This class represents a field type within a field position, e.g. wall, laser, energy...
 */
public abstract class FieldType {

    protected String type;

    public FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
