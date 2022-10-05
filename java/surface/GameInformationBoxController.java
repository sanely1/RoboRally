package surface;

import client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class GameInformationBoxController {
    @FXML
    private TextArea gameInformationField;
    @FXML
    private GameWindowController gameWindowController;

    public void initialize(Client player) {
        gameInformationField.setWrapText(true);
    }

    public void setGameInformationField(String msg) {
        gameInformationField.appendText(msg);
    }

   /*
   public void handlePlayerAdded(Client player, GameWindow gameWindow) {
        Platform.runLater(() -> {
           // gameInformationField.appendText("***///n"+player.getName() + " with ID: " + player.getClientID() + " and figure " + player.getFigure() + " was added.\n");
        //});
    //}


    public void handleSetStartingPoint() {
        Platform.runLater(() -> {
            gameInformationField.appendText("***\nPlease select a starting point by clicking on a possible starting point on the map.\n***\n");
        });
    }

}
