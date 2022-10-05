package surface;

import client.Client;
import client.ClientReaderTask;
import client.Teammate;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class LobbyWindow {

    private LobbyWindowController lobbyWindowController;
    private Stage stage;

    public LobbyWindow(){}


    public void start(Client client, Stage oldStage) throws Exception {

        this.stage = oldStage;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/LobbyWindow.fxml"));
        stage.setTitle("Lobby of RoboRally ");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setMaximized(true);
        lobbyWindowController = fxmlLoader.getController();
        lobbyWindowController.initialize(client);


    }

    public LobbyWindowController getLobbyWindowController() {
        return lobbyWindowController;
    }




}
