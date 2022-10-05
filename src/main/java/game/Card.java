package game;

public abstract class Card {
    protected String category;
    protected String description;
    protected String name;

    public Card(String category, String description, String name) {
        this.category = category;
        this.description = description;
        this.name = name;
    }

    public abstract void cardAction();
}