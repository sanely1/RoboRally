package surface;

import client.Client;
import javafx.fxml.FXML;

import java.io.IOException;


public class LobbyWindowController {


    @FXML
    private ChatBoxController chatBoxController;
    @FXML
    private GameInformationBoxController gameInformationBoxController;
    @FXML
    private LobbyController lobbyController;


    public void initialize(Client client) throws IOException {
        lobbyController = this.getLobbyController();
        lobbyController.initialize(client);

        chatBoxController = this.getChatBoxController();
        chatBoxController.initialize(client);

        gameInformationBoxController = this.getGameInformationBoxController();
        gameInformationBoxController.initialize(client);

    }


    public ChatBoxController getChatBoxController() {
        return chatBoxController;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public GameInformationBoxController getGameInformationBoxController() {
        return gameInformationBoxController;
    }
}
