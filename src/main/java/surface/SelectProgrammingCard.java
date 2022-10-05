package surface;

import client.Client;
import client.ClientReaderTask;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.List;


public class SelectProgrammingCard extends Pane {
    GameWindowController gameWindowController;
    SelectProgrammingCardController selectProgrammingCardsController;
    Stage stage;

    public void start(List<String> programmingCards, ClientReaderTask clientReaderTask, GameWindowController gameWindowController, Client player) throws Exception {
        stage = new Stage();
        this.gameWindowController = gameWindowController;
        gameWindowController.getGameController().clearCardName();
        gameWindowController.getGameController().resetNClicks();

        FXMLLoader fxmlLoader = new FXMLLoader(SelectProgrammingCard.class.getResource("/SelectCards.fxml"));

        stage.setTitle(player.getName() + ", select cards to place them on registers");
        final Popup popup = new Popup();
        popup.setX(300);
        popup.setY(200);


        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setHeight(550);
        stage.setWidth(550);
        selectProgrammingCardsController = fxmlLoader.getController();
        selectProgrammingCardsController.initialize(programmingCards, clientReaderTask, stage, gameWindowController, player);
        stage.show();
    }

    public SelectProgrammingCardController getController() {
        return selectProgrammingCardsController;
    }

    public Stage getStage() {
        return stage;
    }

}
