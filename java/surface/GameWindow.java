package surface;

import client.Client;
import client.ClientReaderTask;
import client.Teammate;
import com.google.gson.JsonArray;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GameWindow {

    public GameWindowController gameWindowController;
    private Stage stage;
    private JsonArray map;
    private Client player;
    private ClientReaderTask clientReaderTask;
    private List<Teammate> teammates;

    public void start(Client player, Stage oldStage) throws IOException {
        this.stage = oldStage;

        FXMLLoader fxmlLoaderGameWindow = new FXMLLoader(this.getClass().getResource("/GameWindow.fxml"));

        stage.setTitle("Robo Rally: Window of " + player.getName());
        stage.setScene(new Scene(fxmlLoaderGameWindow.load()));
        stage.setMaximized(true);

        gameWindowController = fxmlLoaderGameWindow.getController();
        gameWindowController.initialize(map, player, clientReaderTask, teammates);

        stage.show();
    }

    public ChatBoxController getChatBoxController() {
        return gameWindowController.getChatBoxController();
    }

    public GameInformationBoxController getGameInformationBoxController() {
        return this.gameWindowController.getGameInformationBoxController();
    }

    public GameWindowController getGameWindowController() {
        return gameWindowController;
    }

    public GameController getGameController() {
        return this.gameWindowController.getGameController();
    }

    public Stage getStage() {
        return this.stage;
    }

    public void addConnectedPlayer(String name, int clientID) {
        gameWindowController.getGameInformationBoxController().setGameInformationField(clientID + ": " + name);
    }

    public void setParameter(JsonArray map, Client player, ClientReaderTask clientReaderTask, List<Teammate> teammates) {
        this.map = map;
        this.player = player;
        this.clientReaderTask = clientReaderTask;
        this.teammates = teammates;
    }
}
