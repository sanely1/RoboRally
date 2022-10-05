package game;

/**
 * This class contains player and card of register that will be played.
 */
public class ActiveCard {

    private Integer clientID;
    private String card;

    public ActiveCard(Integer clientID, String card) {
        this.clientID = clientID;
        this.card = card;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
