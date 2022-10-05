package model;

/**
 * This class implements the connection update.
 */
public class ConnectionUpdate {
    public final static String REMOVE = "Remove";

    private boolean connected = true;
    private String action = "";

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
