package server;


import model.JsonCommands;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * This class contains the methods for the communication between one client and the server
 */

class ServerReaderTask extends Thread {
    private final Logger logger = Logger.getLogger(ServerReaderTask.class);
    boolean isRunning = true;
    private Socket socket;
    private Server server;
    private PrintWriter printOut;
    private BufferedReader readerIn;
    private PlayerInformation playerInformation;

    /**
     * the constructor stores the socket as well as the server
     */
    public ServerReaderTask(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        readerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printOut = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
    }

    /**
     * Waiting for client messages
     * sends the messages to the server for processing
     * if the connection is quit, close is called
     */
    public void run() {
        try {

            send(JsonCommands.HelloClient());
            for (String msg; (msg = readerIn.readLine()) != null && !"bye".equalsIgnoreCase(msg); ) {
                //todo: Queu from Jsonobjects
                server.processMessage(this, msg);
            }
            isRunning = false;
            close();
        } catch (Exception e) {
            logger.error("Error from ServerReaderTask.run() ", e);
        }
    }

    /**
     * A server message is send to the client
     */
    public void send(JSONObject jsonObject) {
        printOut.println(jsonObject.toString());
        printOut.flush();
    }

    protected void close() {
        server.closeConnection(this);
        try {
            readerIn.close();
            printOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PlayerInformation getPlayerInformation() {
        return playerInformation;
    }

    public void setPlayerInformation(PlayerInformation playerInformation) {
        this.playerInformation = playerInformation;
    }

    public Server getServer() {
        return server;
    }
}