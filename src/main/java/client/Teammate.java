package client;

import model.ConnectionUpdate;

/**
 * This class contains relevant information of a teammate.
 * These are the required properties of the game.
 */

public class Teammate {

    private boolean registered = false;
    private String group;
    private String name;
    private Integer figure;
    private int clientID;
    private boolean rebooted = false;
    private ConnectionUpdate connectionUpdate;
    private Integer checkpointReached = 0;
    private Integer cardsInPile = 0;
    private boolean activePlayer = false;
    private Integer startingPoint;
    private Integer positionX;
    private Integer positionY;
    private String orientation;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFigure() {
        return figure;
    }

    public void setFigure(Integer figure) {
        this.figure = figure;
    }

    public int getClientID() {
        return clientID;
    }

    public void setPlayerID(int clientID) {
        this.clientID = clientID;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public ConnectionUpdate getConnectionUpdate() {
        return connectionUpdate;
    }

    public void setConnectionUpdate(ConnectionUpdate connectionUpdate) {
        this.connectionUpdate = connectionUpdate;
    }

    public boolean isActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Integer getCheckpointReached() {
        return checkpointReached;
    }

    public void setCheckpointReached(Integer checkpointReached) {
        this.checkpointReached = checkpointReached;
    }

    public Integer getCardsInPile() {
        return cardsInPile;
    }

    public void setCardsInPile(Integer cardsInPile) {
        this.cardsInPile = cardsInPile;
    }

    public boolean isRebooted() {
        return rebooted;
    }

    public void setRebooted(boolean rebooted) {
        this.rebooted = rebooted;
    }

    public Integer getStartingPoint() {
        return this.startingPoint;
    }

    public void setStartingPoint(Integer startingPoint) {
        this.startingPoint = startingPoint;
    }
}

