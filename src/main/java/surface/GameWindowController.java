package surface;

import client.Client;
import client.ClientReaderTask;
import client.Teammate;
import com.google.gson.JsonArray;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.TypeName;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static java.lang.String.valueOf;


public class GameWindowController {


    @FXML
    private ChatBoxController chatBoxController;
    @FXML
    private GameInformationBoxController gameInformationBoxController;
    @FXML
    private GameController gameController;
    @FXML
    private Label secondsLabel;
    private Timeline timeline;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(TypeName.TIMER);
    @FXML
    private Label currentPlayer;
    @FXML
    private Pane pane;

    @FXML
    private ImageView myPlayer;

    @FXML
    private Label nameOfPlayer;

    @FXML
    private Label myEnergy;
    @FXML
    private Label secondsPlaceHolder;


    public void initialize(JsonArray map, Client player, ClientReaderTask clientReaderTask, List<Teammate> teammates) throws IOException {

        gameController = this.getGameController();
        gameController.initialize(map, clientReaderTask, teammates);

        chatBoxController = this.getChatBoxController();
        chatBoxController.initialize(player);

        gameInformationBoxController = this.getGameInformationBoxController();
        gameInformationBoxController.initialize(player);

        secondsLabel.textProperty().bind(timeSeconds.asString());
    }


    /**
     * Method to display which figure a player is.
     *
     * @param figureNumber Contains the figure of a player that will be displayed all the time.
     * @author Florian
     */
    public void showMyPlayer(int figureNumber){
        String imagePath = "/player" +figureNumber+ "_right.png";
        Image figure = new Image(valueOf(getClass().getResource(imagePath)));

        myPlayer.setImage(figure);
    }

    /**
     * Method to display which name a player has.
     *
     * @param playerName Contains the name of a player that will be displayed all the time.
     * @author Florian
     */
    public void showPlayerName(String playerName) {
        nameOfPlayer.setText(playerName);
    }


    /**
     * Method to display how much energy a player currently has.
     *
     * @param energNumber The number of energy a player has now.
     * @author Florian
     */
    public void showEnergy(int energNumber) {
        myEnergy.setText(valueOf(energNumber));
    }



    public GameInformationBoxController getGameInformationBoxController() {
        return gameInformationBoxController;
    }

    public ChatBoxController getChatBoxController() {
        return chatBoxController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void handleTimer() {
        if (timeline != null) {
          //  timeline.stop();
        }
        timeSeconds.set(TypeName.TIMER);
        timeline = new Timeline();
        secondsLabel.setVisible(true);

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TypeName.TIMER+1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }


    public void stopTimer() {
     //   timeline.stop();
    }

    public Label getCurrentPlayerLabel(){return currentPlayer;}
}
