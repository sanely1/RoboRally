package surface;


import client.Client;
import client.ClientReaderTask;
import client.Teammate;
import com.google.gson.JsonArray;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 *
 */
public class ViewModel {

    private final int TWONKY = 0;
    private final int HULK = 1;
    private final int HAMMERBOT = 2;
    private final int SMASHBOT = 3;
    private final int ZOOMBOT = 4;
    private final int SPINBOT = 5;


    protected Main main;


    protected Client player;


    GameWindow gameWindow = new GameWindow();
    public GameInformationBoxController gameInformationBoxController;

    GameWindowController gameWindowController = gameWindow.getGameWindowController();
    SelectProgrammingCard selectProgrammingCard = new SelectProgrammingCard();
    private LobbyWindowController lobbyWindowController;
    public ChatBoxController chatBoxController;
    LobbyWindow lobbyWindow = new LobbyWindow();
    private Stage stage;


    public ViewModel(Stage stage) {
        this.stage = stage;
    }

    public void postMessage(String message, GameWindow gameWindow) {
        // GameInformationBoxController gameInformationBoxController = gameWindow.getGameWindowController().getGameInformationBoxController();

    }







    public void handleHelloClient(String protocol, GameWindow gameWindow) {
        Platform.runLater(() -> lobbyWindowController.getGameInformationBoxController().setGameInformationField("Hello Client! Your protocol version is: " + protocol + "\n"));
    }

    public void handleWelcome(Integer clientID) {
        Platform.runLater(() -> lobbyWindowController.getGameInformationBoxController().setGameInformationField("***\nWelcome! Please choose a name and a figure to log in. " + "\n" + "If you are ready to play, click the ready button\n"));
    }

    public void handleMyTextMessage(Message message, List<Teammate> teammates) {
        if (message.isPrivate()) {
            String receiver = "";
            for (Teammate teammate : teammates) {
                receiver = receiver + teammate.getName() + "  ";
            }
            postChatMessage("Me", receiver, message.getMessage());
        } else
            postChatMessage("Me", "all", message.getMessage());
    }

    public void handleErrorMessage(String errorMessage) {
        Platform.runLater(() -> {
            lobbyWindowController.getLobbyController().setErrorMsgArea("Error " + errorMessage + "\n");
            lobbyWindowController.getLobbyController().getErrorMsgArea().setVisible(true);
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));
            pauseTransition.setOnFinished(event -> lobbyWindowController.getLobbyController().getErrorMsgArea().setVisible(false));
            pauseTransition.play();
        });

        postMessage("Error: " + errorMessage, gameWindow);
    }

    /**
     * send messages to chatBox
     *
     * @param sender
     * @param receiver
     * @param message
     * @author Kishan
     */

    public void postChatMessage(String sender, String receiver, String message) {
        Platform.runLater(() -> {
            if (receiver != null) {
                chatBoxController.setChatArea(sender + " to " + receiver + ": " + message + " \n");
            } else {
                chatBoxController.setChatArea(sender + ": " + message + "\n");
            }

        });
    }



    protected String getTeammateNameFromId(String teammateId) {
        for (Teammate mate : this.player.getTeamMates()) {
            if (mate.getClientID() == Integer.parseInt(teammateId)) {
                return mate.getName();
            }
        }
        return "[no Name]";
    }

    public void handleReceivedChat(String message, String senderId, boolean isPrivate, GameWindow gameWindow) {
        String sender = getTeammateNameFromId(senderId);
        if (isPrivate) {
            postChatMessage(sender, "me", message);
        } else {
            postChatMessage(sender, "all", message);
        }
    }


    public void handleConnectionUpdate(Teammate teammate) {
        Platform.runLater(() -> {
            lobbyWindowController.getLobbyController().getConnectedPlayer().getChildren().remove(lobbyWindowController.getLobbyController().getConnectedPlayerHBox().get(teammate.getClientID()));
            lobbyWindowController.getLobbyController().getConnectedPlayerHBox().remove(teammate.getClientID());
        });
        postMessage("Connection update for Player: " + teammate.getName() + " (" + teammate.getClientID() + " / " + teammate.getFigure() +
                ")  connected: " + teammate.getConnectionUpdate().isConnected() + ", action: " + teammate.getConnectionUpdate().getAction(), gameWindow);
    }

    public void handlePlayerAdded(Client client) {
        if (!client.isIsAI()) {
            Platform.runLater(() -> {
                lobbyWindowController.getGameInformationBoxController().setGameInformationField("***\nYou were added with the name " + client.getName() + " and ID " + client.getClientID() + "\n");
                lobbyWindowController.getLobbyController().disableRobo(client.getFigure(), true);
                lobbyWindowController.getLobbyController().getLoginButton().setDisable(true);
                lobbyWindowController.getLobbyController().getReadyButton().setDisable(false);
                addConnectedClient(client.getFigure(), client.getName(), client.getClientID(), client.isReady());
            });
        }

    }


    public void handleTeammateAdded(Teammate teammate) {
        if (!player.isIsAI()) {
            Platform.runLater(() -> {
                lobbyWindowController.getGameInformationBoxController().setGameInformationField("***\nClient was added with the name " + teammate.getName() + " and ID " + teammate.getClientID() + "\n");
                lobbyWindowController.getLobbyController().disableRobo(teammate.getFigure(), false);
                if (lobbyWindowController.getLobbyController().getFigure() == teammate.getFigure()) {
                    lobbyWindowController.getLobbyController().setFigure(-1);
                }
                addConnectedClient(teammate.getFigure(), teammate.getName(), teammate.getClientID(), teammate.isRegistered());
            });
        }

    }

    public void addConnectedClient(int figure, String clientName, int clientID, boolean isReady) {
        Platform.runLater(() -> {
            HBox hBox = new HBox(40);
            switch (figure) {
                case TWONKY:
                    lobbyWindowController.getLobbyController().figure0.setOpacity(0.3);
                    break;
                case HULK:
                    lobbyWindowController.getLobbyController().figure1.setOpacity(0.3);
                    break;
                case HAMMERBOT:
                    lobbyWindowController.getLobbyController().figure2.setOpacity(0.3);
                    break;
                case SMASHBOT:
                    lobbyWindowController.getLobbyController().figure3.setOpacity(0.3);
                    break;
                case ZOOMBOT:
                    lobbyWindowController.getLobbyController().figure4.setOpacity(0.3);
                    break;
                case SPINBOT:
                    lobbyWindowController.getLobbyController().figure5.setOpacity(0.3);
                    break;
            }

            Label clientNameLabel = new Label(clientID + " "+clientName + "\n");
            clientNameLabel.setPrefWidth(215);
            clientNameLabel.setFont(Font.font("Comic Sans MS"));
            clientNameLabel.autosize();
            clientNameLabel.setTextAlignment(TextAlignment.CENTER);


            Circle readyStatus = new Circle(8);
            readyStatus.setFill(Color.RED);

            readyStatus.setTranslateX(readyStatus.getCenterX() - 12);
            readyStatus.setTranslateY(readyStatus.getCenterY() + 7);

            hBox.getChildren().addAll(clientNameLabel, readyStatus);
            lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().put(clientID, readyStatus);
            lobbyWindowController.getLobbyController().getConnectedPlayerHBox().put(clientID, hBox);
            lobbyWindowController.getLobbyController().getConnectedPlayer().getChildren().add(hBox);

        });
    }

    public void handlePlayerStatus(Client client, boolean status) {
        Platform.runLater(() -> {
            if (lobbyWindowController.getLobbyController() != null && client != null) {
                if (status) {
                    if (lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(client.getClientID()) != null) {
                        lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(client.getClientID()).setFill(Color.GREEN);
                    } else {
                        if (lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(client.getClientID()) != null) {
                            lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(client.getClientID()).setFill(Color.RED);
                        }

                    }
                }
            }
        });
        postMessage("Player status has changed to: " + status, gameWindow);
    }

    public void handleTeammateStatus(Teammate teammate, boolean isStatus) {
        Platform.runLater(() -> {
            if (lobbyWindowController.getLobbyController() != null && teammate != null) {
                if (isStatus) {
                    if (lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(teammate.getClientID()) != null) {
                        lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(teammate.getClientID()).setFill(Color.GREEN);
                    } else {
                        if (lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(teammate.getClientID()) != null) {
                            lobbyWindowController.getLobbyController().getConnectedPlayerReadyCircles().get(teammate.getClientID()).setFill(Color.RED);
                        }
                    }
                }
            }
        });
        postMessage("Teammate status has changed to: " + teammate.getName() + ": " + isStatus, gameWindow);
    }



    public void handleSetStartingPoint() throws InterruptedException {
        sleep(500);
        gameInformationBoxController.handleSetStartingPoint();

        // postMessage("Please set your StartingPoint", gameWindow);
    }

    public void handleSetStartingPointByPlayer(Teammate teammate) {
        postMessage("Teammate " + teammate.getName() + " is required to set the StartingPoint", gameWindow);
    }

    public void handleMove(int player, int to) {
        postMessage("Player " + player + " moved to position: " + to, gameWindow);
        //braucht man nicht, da man die Bewegungen auf der Map nachverfolgen kann?
    }


    public void handleTeammateSelectionFinished(Teammate teammate) {
        postMessage("Teammate " + teammate.getName() + " has finished selection", gameWindow);
        //braucht man nicht mehr
    }

    public void handleSelectionFinished(String playerName) {
        gameInformationBoxController.setGameInformationField(playerName + " has filled all five registers.\n");
    }

    public void handleTeammateShuffleCoding(Teammate teammate) {
        postMessage("Teammate " + teammate.getName() + " has to shuffle", gameWindow);
    }


    public void handleDrawDamage(List<String> cardNames) {
        String cards = "";
        for (String cardName : cardNames) {
            cards = cards + "   " + cardName;
        }
        gameInformationBoxController.setGameInformationField("You got damage cards: " + cards + ".");
    }

    public void handlePickDamage(Integer count) {
        postMessage("Please draw " + count + " damage cards of type TrojanHorse or Worm or Virus", gameWindow);
    }

    public void handlePlayerShooting() {
        postMessage("Player shooting", gameWindow);
    }

    public void handleDrawDamage(Client client, List<String> cardNames) {
        Platform.runLater(()->{
            String cards = "";
            for (String cardName : cardNames) {
                cards = cards + "   " + cardName;
            }
            gameInformationBoxController.setGameInformationField(client.getName() + " has got damage cards: " + cards);
        });

    }


    public void handleAvailableMaps(List<String> maps) {
        lobbyWindowController.getLobbyController().setMaps(maps);
        lobbyWindowController.getLobbyController().setMapsVisible(true);
        gameInformationBoxController.setGameInformationField("***\nPlease choose a map to start the game. The game will start as soon as at least one other player is ready.\n");

    }

    public void handleMapSelected(String racingCourseName) {
        gameInformationBoxController.setGameInformationField("***\n" + racingCourseName + " was selected.");
    }


    public void hidePlayerOnMap(Integer oldPosX, Integer oldPosY) {
        for (Teammate teammate : player.getTeamMates()) {
            Platform.runLater(() -> {
                gameWindow.getGameController().removePlayerFromOldField(oldPosX, oldPosY);
            });
        }
    }

    public void showPlayerOnMap(Integer x, Integer y, String orientation, Integer figure) {

        Platform.runLater(() -> {
            gameWindow.getGameController().showPlayer(x, y, orientation, figure);
        });

    }

    public void removePlayerFromOldField(Integer posX, Integer posY) {

        Platform.runLater(() -> {
            gameWindow.getGameController().removePlayerFromOldField(posX, posY);
        });

    }

    public void showMyPlayer(Integer figureNumber) {

        Platform.runLater(() -> {
            gameWindow.getGameWindowController().showMyPlayer(figureNumber);
        });

    }

    public void showPlayerName(String nameOfPlayer) {

        Platform.runLater(() -> {
            gameWindow.getGameWindowController().showPlayerName(nameOfPlayer);
        });

    }


    public void showEnergy(int energyNumber) {

        Platform.runLater(() -> {
            gameWindow.getGameWindowController().showEnergy(energyNumber);
        });

    }

    public void handleGameStarted(JsonArray map, ClientReaderTask clientReaderTask, List<Teammate> teammates) throws IOException {

        Platform.runLater(() -> {
            gameWindow.setParameter(map, player, clientReaderTask, teammates);

            try {
                gameWindow.start(player, this.stage);
                chatBoxController = gameWindow.getChatBoxController();
                gameInformationBoxController = gameWindow.getGameWindowController().getGameInformationBoxController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * returns the player
     */
    public Client getPlayer() {
        return player;
    }

    public void setPlayer(Client player) {
        this.player = player;
    }


    public void handleYourProgrammingCards(List programmingCards, ClientReaderTask clientReaderTask) throws Exception {
        Platform.runLater(() -> {
            try {
                selectProgrammingCard.start(programmingCards, clientReaderTask, gameWindow.gameWindowController, player);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleCardSelected(String playerName, int register, boolean filled) {
        String action = "";
        if (filled) {
            action = "filled";
        } else {
            action = "removed";
        }
        gameInformationBoxController.setGameInformationField(playerName + " " + action + " register " + register + ".\n");
    }

    public void handleCardPlayed(String playerName, String card) {
        Platform.runLater(() -> {
            gameInformationBoxController.setGameInformationField(playerName + " played Card " + card + ".\n");
        });
    }

    public void ShowPlayer(int posX, int posY, int playerID, String orientation, int figure) {
        //  gameController.showPlayer(posX, posY, orientation, figure);
    }


    public GameWindow getGameWindow() {
        return this.gameWindow;
    }

    public void handleTurnToPlayCard(List<String> currentCard) {
        Platform.runLater(() -> {
            GameController gameController = gameWindow.getGameWindowController().getGameController();
            gameController.showLabelText(currentCard);
            gameController.enablePlayButton();
        });
    }

    public void showTimerEnded(List<Integer> clientIds) {
        gameInformationBoxController.setGameInformationField("Timer has ended.\n");
        ArrayList selectedCards = selectProgrammingCard.getController().getSelectedCards();
        gameWindow.getGameController().setCardNames(selectedCards);
        Platform.runLater(() -> {
            selectProgrammingCard.getStage().close();
        });

    }

    public void showTimer() {
        Platform.runLater(()->{
            gameInformationBoxController.setGameInformationField("Timer has started.\n");
            gameWindow.getGameWindowController().handleTimer();
        });
    }

    public void handleGameFinished(String winnerName, int winnerId, int clientID) {
        gameInformationBoxController.setGameInformationField(winnerName + " won the game!\n");
        boolean isWinner = false;
        if (clientID == winnerId) {
            isWinner = true;
        }
        gameWindow.getGameController().setGameFinishedImage(isWinner);
    }

    public void handleCheckpoint(String winnerName, Integer number) {
        gameInformationBoxController.setGameInformationField(winnerName + "reached checkpoint " + number + "!\n");
    }

    public void handleCardsYouGotNow(List<String> currentCard) {
        gameWindow.getGameController().setCardsYouGotNow(currentCard);
        gameWindow.getGameController().setCardImages();
        //gameWindow.getGameController().setCards(currentCard, selectProgrammingCard.getController());
    }

    public void setLobbyWindowController(LobbyWindowController lobbyWindowController) {
        this.lobbyWindowController = lobbyWindowController;
    }

    public void setGameInformationBoxController(GameInformationBoxController gameInformationBoxController) {
        this.gameInformationBoxController = gameInformationBoxController;
    }

    public void setChatBoxController(ChatBoxController chatBoxController) {
        this.chatBoxController = chatBoxController;
    }

    public void stopTimer() {
        gameWindow.getGameWindowController().stopTimer();
    }

    public void handleCurrentPlayer(String currentPlayer) {
        Platform.runLater(() -> {
            if (currentPlayer.equals(player.getName())) {
                gameWindow.getGameWindowController().getCurrentPlayerLabel().setText("It's your turn!");
            } else {
                gameWindow.getGameWindowController().getCurrentPlayerLabel().setText("It's " + currentPlayer + "'s turn!");
            }
        });
    }

    public void showNotYourCards(String playerName, Integer cards) {
        Platform.runLater(() -> {
            gameInformationBoxController.setGameInformationField(playerName+" has "+cards+" in hand.");
                }
        );
    }

    public void handleReplaceCard(int register, String card) {
        Platform.runLater(()->{
            gameWindowController.getGameController().replaceCard(register, card);
        });
    }

    public void handleReboot(String clientName) {
        Platform.runLater(()->{
            gameInformationBoxController.setGameInformationField(clientName);
        });
    }
}
