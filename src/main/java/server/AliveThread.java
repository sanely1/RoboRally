package server;

import model.JsonCommands;

public class AliveThread extends Thread {
    ServerReaderTask serverReaderTask;


    public AliveThread(ServerReaderTask serverReaderTask) {
        this.serverReaderTask = serverReaderTask;
    }

    public void run() {

        while (true) {
            serverReaderTask.send(JsonCommands.Alive());
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
