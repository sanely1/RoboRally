package server;

/**
 * This class represents the players of the game.
 */
public class PlayerInformation {

    private ServerReaderTask serverReaderTask;


    private boolean added = false;
    private boolean ready = false;
    private boolean active = false;
    private String name = "";
    private Integer clientID = 0;
    private Integer figure = -1;
    private boolean isAI = false;
    private boolean selectMap = false;


    public PlayerInformation() {
    }


    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public Integer getFigure() {
        return figure;
    }

    public void setFigure(Integer figure) {
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerReaderTask getServerThread() {
        return serverReaderTask;
    }

    public void setServerThread(ServerReaderTask serverReaderTask) {
        this.serverReaderTask = serverReaderTask;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    public boolean isSelectMap() {
        return selectMap;
    }

    public void setSelectMap(boolean selectMap) {
        this.selectMap = selectMap;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}

