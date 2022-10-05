package client;

import model.TypeName;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


/**
 * This class contains the methods for the communication between the client and the server
 */

public class ClientReaderTask extends Thread {

    private final Logger logger = Logger.getLogger(ClientReaderTask.class);
    private final Client client;
    private PrintWriter printOut;
    private BufferedReader readerIn;

    /**
     * the constructor stores the client as well as the readerIn and printOut channel for the communication
     */
    public ClientReaderTask(Client client) {
        this.client = client;
    }

    /**
     * methode run() do:
     * --Waiting for server messages
     * --sends the messages to the client for processing
     * --if the connection is quit, close is called
     */
    public void run() {
        String msg;

        try {
            Socket socket = new Socket(TypeName.ip, TypeName.port);
            readerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printOut = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
            while ((msg = readerIn.readLine()) != null) {
                client.processServerMessage(msg);
            }
            close();
        } catch (SocketException e) {
            if (!e.getMessage().equals("Socket closed")) {
                logger.error("Server is not available, Try again!!");
                logger.error("There was an exception in client.ClientReaderTask.run(): ", e);
            }
        } catch (Exception e) {
            logger.error("There was an exception in client.ClientReaderTask.run(): ", e);
        }

    }

    /**
     * A Message from Client sent to the server
     */
    public void send(JSONObject jsonObject) {
        if (printOut != null) {
            printOut.println(jsonObject.toString());
            logger.info(jsonObject.toString());
            printOut.flush();
        }
    }

    public void send(String msg) {
        if (printOut != null) {
            printOut.println(msg);
            printOut.flush();
        }
    }

    /**
     * Closes the input and output channel
     */
    protected void close() {
        try {
            send("bye");
            printOut.close();
            readerIn.close();
        } catch (Exception e) {
            logger.error("There was an exception in client.ClientReaderTask.close() " + e);
        }
    }

}