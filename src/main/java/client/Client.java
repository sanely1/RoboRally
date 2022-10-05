package client;


import com.google.gson.*;
import game.Position;
import game.StartingPoints;
import javafx.application.Platform;
import model.ConnectionUpdate;
import model.JsonCommands;
import model.Message;
import model.TypeName;
import org.apache.log4j.Logger;
import surface.GameController;
import surface.ViewModel;

import java.io.IOException;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;
import static model.Tools.getKey;
import static model.Tools.getRandomNumber;


/**
 * This Class contains the logic for handling messages from the View as well as from the server
 */
public class Client {

    private ViewModel viewModel;
    private ClientReaderTask clientReaderTask;
    private boolean isLoggingIn;
    private boolean ready = false;
    private String group = TypeName.GROUP_NAME;
    private static boolean isAI = false;
    private String name;
    private int figure;
    private Position position;
    private int positionX;
    private int positionY;
    private String orientation;
    private int clientID;
    private ConnectionUpdate connectionUpdate;
    private List<FieldPosition> racingCourseMapList = new ArrayList<>();
    private String racingCourseName;
    private List<Teammate> teamMates = new ArrayList<>();
    private final Logger logger = Logger.getLogger(Client.class);
    private List<String> programmingCards = new ArrayList<>();
    private StartingPoints board;
    private int phase;
    private String activeCard = "";
    public List<String> currentCard = new ArrayList<>();
    private String map = "";
    private GameController gameController;
    private int energy;


    public Client(boolean isAI) {
        this.isAI = isAI;
        setLoggingIn(true);
        init();
    }

    /**
     * It starts the ClientThread belonging to this client.
     */
    private void init() {
        try {
            setClientReaderTask(new ClientReaderTask(this));
            getClientReaderTask().start();
        } catch (Exception e) {
            logger.error("An error occurred while starting the client/server connection: ", e);
        }
    }

    /**
     * Will be called by GUI,
     * Method contains the Logic for interpreting the messages sent by the user and also sends them off to the server,
     * --If the message is empty than will be ignored,
     * --If a Message starts with "#", Than processCommand will be called
     * --remaining Message will sent to Server
     */
    public void processViewMessage(String message) {
        if (message != null && !message.isEmpty()) {
            if (message.trim().startsWith("#")) {
                processCommand(message);
            } else {
                Message textMessage = getTextMessage(message);
                if (textMessage.getReceiver().size() == 0) {
                    getClientReaderTask().send(JsonCommands.SendChat(textMessage.getMessage(), TypeName.TO_ALL));
                    logger.info("SEND TO ALL: " + textMessage.getMessage());
                } else {
                    for (Integer receiverID : textMessage.getReceiver()) {
                        getClientReaderTask().send(JsonCommands.SendChat(textMessage.getMessage(), receiverID));
                        logger.info("SEND PRIVATE: " + textMessage);
                    }
                }
                List<Teammate> teammates = new ArrayList<>();
                for (Teammate teammate : getTeamMates()) {
                    if (textMessage.getReceiver().contains(teammate.getClientID()))
                        teammates.add(teammate);
                }
                if (teammates.size() > 0)
                    textMessage.setPrivate(true);
                this.viewModel.handleMyTextMessage(textMessage, teammates);
            }
        }
    }

    /**
     * Method handles commands which were sent from the GUI.
     * --set status
     */
    private void processCommand(String message) {
        message = message.trim().substring(1).trim();
        logger.info("View Message: " + message);

        if (message.startsWith(TypeName.SET_STATUS)) {
            message = message.replaceAll(TypeName.SET_STATUS, "").trim();
            boolean status = true;
            if (message.contains("false"))
                status = false;
            getClientReaderTask().send(JsonCommands.SetStatus(status));
            logger.info("Send SetStatus: " + JsonCommands.SetStatus(status));
        } else if (message.startsWith((TypeName.ADD_AI))) {
            getClientReaderTask().send(JsonCommands.HelloServer(true));
        } else if (message.startsWith(TypeName.MAPSELECTED)) {
            message = message.replaceAll(TypeName.MAPSELECTED, "").trim();
            getClientReaderTask().send(JsonCommands.MapSelected(message));
            logger.info("Send MapSelected: " + JsonCommands.MapSelected(message));
        }
    }

    /**
     * Processes the server messages.
     * Calls the specific methods regarding to the message.
     */
    protected void processServerMessage(String msg) throws Exception {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
        logger.info("Server: JSON Object:" + jsonObject);

        if (jsonObject.has(TypeName.MESSAGE_TYPE)) {
            String jsonPrimitive = jsonObject.getAsJsonPrimitive(TypeName.MESSAGE_TYPE).getAsString();
            if (jsonPrimitive.equals(TypeName.RECEIVED_CHAT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processReceivedChat(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.HELLO_CLIENT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processHelloClient(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.ALIVE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processAlive(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.WELCOME)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processWelcome(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.PLAYER_ADDED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processPlayerAdded(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.PLAYER_STATUS)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processPlayerStatus(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CONNECTION_UPDATE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processConnectionUpdate(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.GAME_STARTED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processGameStarted(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SELECT_MAP)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSelectMap(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.MAP_SELECTED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processMapSelected(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.ERROR)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processError(jsonObject);
            } else if (jsonPrimitive.equals((TypeName.STARTING_POINT_TAKEN))) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processStartingPointTaken(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.YOUR_CARDS)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processYourCards(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CARDS_YOU_GOT_NOW)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCardsYouGotNow(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CURRENT_CARDS)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCurrentCards(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CURRENT_PLAYER)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCurrentPlayer(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CARD_SELECTED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCardSelected(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.ACTIVE_PHASE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processActivePhase(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CARD_PLAYED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCardPlayed(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.MOVEMENT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processMovement(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.PLAYER_TURNING)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processPlayerTurning(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.TIMER_STARTED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processTimerStarted(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.TIMER_ENDED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processTimerEnded(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CHECKPOINT_REACHED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processCheckpointReached(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.GAME_FINISHED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processGameFinished(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SELECTION_FINISHED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSelectionFinished(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.ENERGY)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processEnergy(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.REPLACE_CARD)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processReplaceCard(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.DRAW_DAMAGE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processDrawDamage(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.PICK_DAMAGE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processPickDamage(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SELECT_DAMAGE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSelectDamage(jsonObject);
            } else if (jsonPrimitive.equals(TypeName.REBOOT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processReboot(jsonObject);
            }
        }
    }

    /**
     * react to message "ReplaceCard" with changing card in register
     *
     * @param jsonObject
     * @author Johanna
     */
    private void processReplaceCard(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.REGISTER) && jsonObject.has(TypeName.New_CARD) && jsonObject.has(TypeName.CLIENT_ID)) {
            int register = jsonObject.get(TypeName.REGISTER).getAsInt();
            String newCard = jsonObject.get(TypeName.New_CARD).getAsString();

            viewModel.handleReplaceCard(register, newCard);
        }
    }

    /**
     * react to message "DrawDamage"
     *
     * @param jsonObject
     * @author Mohammad
     */


    private void processDrawDamage(JsonObject jsonObject) {

        if (jsonObject.has(TypeName.CLIENT_ID) && jsonObject.has(TypeName.CARDS)) {
            int clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            JsonArray cards = jsonObject.get(TypeName.CARDS).getAsJsonArray();
            List<String> cardName = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                cardName.add(cards.get(i).getAsString());
            }

            getViewModel().handleDrawDamage(this, cardName);
        }

    }

    /**
     * react to message "PickDamage"
     *
     * @param jsonObject
     * @author Mohammad
     */

    private void processPickDamage(JsonObject jsonObject) {

        if (jsonObject.has(TypeName.COUNT)) {
            int count = jsonObject.get(TypeName.COUNT).getAsInt();
            getViewModel().handlePickDamage(count);
        }
    }

    /**
     * react to message "SelectDamage"
     *
     * @param jsonObject
     * @author Mohammad
     */

    private void processSelectDamage(JsonObject jsonObject) {

        if (jsonObject.has(TypeName.CARDS)) {
            JsonArray cards = jsonObject.get(TypeName.CARDS).getAsJsonArray();
            List<String> cardName = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                cardName.add(cards.get(i).getAsString());
            }
        }
    }

    /**
     * react to message "Reboot"
     *
     * @param jsonObject
     * @author Mohammad
     */

    private void processReboot(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.CLIENT_ID)) {
            int clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            String clientName = this.name;
            for (Teammate teammate : getTeamMates()) {
                if (clientId == teammate.getClientID()) {
                    clientName = teammate.getName();
                    break;
                }
            }
            getViewModel().handleReboot(clientName);
        }
    }


    /**
     * react to JSON Message "CurrentPlayer" (from server)
     *
     * @param jsonObject
     * @author Johanna
     */
    private void processCurrentPlayer(JsonObject jsonObject) throws InterruptedException {

        if (jsonObject.has(TypeName.CLIENT_ID)) {
            int receivedClientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();

            String currentPlayer = this.name;
            for (Teammate teammate : getTeamMates()) {
                if (receivedClientID == teammate.getClientID()) {
                    currentPlayer = teammate.getName();
                    break;
                }
            }

            sleep(50);
            this.viewModel.handleCurrentPlayer(currentPlayer);

            if (this.clientID != receivedClientID) {
                return;
            } else {
                if (!isAI) {
                    if (phase == 0) {
                        viewModel.handleSetStartingPoint();
                    }
                    if (phase == 3) {
                        getViewModel().handleTurnToPlayCard(currentCard);
                    }
                }
                if (isAI) {
                    Platform.runLater(() -> {
                        if (phase == 0) {
                            Position position = getRandomFreeStartingPoint();
                            getClientReaderTask().send(JsonCommands.SetStartingPoint((position.getX()), position.getY()));
                            logger.info("JSON Message: SetStartingPoint ->" + JsonCommands.SetStartingPoint((position.getX()), position.getY()));
                        } else if (phase == 3) {
                            getClientReaderTask().send(JsonCommands.PlayCard(activeCard));
                            logger.info("JSON Message: PlayCard ->" + JsonCommands.PlayCard(activeCard));

                        } else {
                            getClientReaderTask().send(JsonCommands.Error("An error occurred."));
                        }
                    });
                }

            }

        }

    }


    /**
     * @param jsonObject
     */
    private void processCurrentCards(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.ACTIVE_CARDS)) {
            JsonArray activeCards = jsonObject.get(TypeName.ACTIVE_CARDS).getAsJsonArray();
            for (int i = 0; i < activeCards.size(); i++) {
                JsonObject object = activeCards.get(i).getAsJsonObject();
                int currentClientID = object.get(TypeName.CLIENT_ID).getAsInt();
                if (this.clientID == currentClientID) {
                    this.activeCard = object.get(TypeName.CARD).getAsString();
                    break;
                }
            }
        }
    }

    /**
     * Server sends the available maps for selection
     *
     * @param jsonObject
     * @author Kishan
     */
    private void processSelectMap(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.AVAILABLE_MAPS)) {
            JsonArray availableMaps = jsonObject.get(TypeName.AVAILABLE_MAPS).getAsJsonArray();
            List<String> maps = new ArrayList<>();
            for (int i = 0; i < availableMaps.size(); i++) {
                maps.add(availableMaps.get(i).getAsString());
            }
            this.viewModel.handleAvailableMaps(maps);
        }
    }

    private void processAlive(JsonObject jsonObject) {
        getClientReaderTask().send(JsonCommands.Alive());
    }

    /**
     * Server commits the selected map.
     *
     * @param jsonObject
     * @author Kishan
     */
    private void processMapSelected(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.MAP)) {
            String map = jsonObject.get(TypeName.MAP).getAsString();
            setRacingCourseName(map);
            this.viewModel.handleMapSelected(getRacingCourseName());
        }
    }

    /**
     * The Text message will be posted in the view
     */
    private void processReceivedChat(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.MESSAGE) && !isLoggingIn()) {
            String message = jsonObject.get(TypeName.MESSAGE).getAsString();
            String sender = jsonObject.get(TypeName.FROM).getAsString();
            boolean isPrivate = jsonObject.get(TypeName.IS_PRIVATE).getAsBoolean();

            if (!isLoggingIn())
                this.viewModel.handleReceivedChat(message, sender, isPrivate, this.viewModel.getGameWindow());
        }
    }

    /**
     * Server has accepted the connection request.
     */
    private void processHelloClient(JsonObject jsonObject) {
        logger.info("JSONMessage: HelloClient");
        if (jsonObject.has(TypeName.PROTOCOL)) {
            String protocol = jsonObject.get(TypeName.PROTOCOL).getAsString();
            this.viewModel.handleHelloClient(protocol, this.viewModel.getGameWindow());
            logger.info("JSONMessage: sends HelloServer");
        }
        getClientReaderTask().send(JsonCommands.HelloServer(isAI));
        logger.info("JSONObject:" + JsonCommands.HelloServer(isAI));

    }

    /**
     * Server login of player.
     * Player gets its player id.
     */
    private void processWelcome(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.CLIENT_ID)) {
            Integer clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            setClientID(clientID);
            this.viewModel.handleWelcome(clientID);

            if (isAI) {
                try {
                    this.sendAIInfo();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Server sends error message.
     */
    private void processError(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.eRROR)) {
            String errorMessage = jsonObject.get(TypeName.eRROR).getAsString();
            this.viewModel.handleErrorMessage(errorMessage);

        }
    }

    /**
     * Server will commit player name, and  figure, that the player has selected.
     */
    private void processPlayerAdded(JsonObject jsonObject) {
        Integer clientID = 0;
        String clientName = "";
        Integer figure = 0;

        if (jsonObject.has(TypeName.CLIENT_ID)) {
            clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        }
        if (jsonObject.has(TypeName.CLIENT_NAME)) {
            clientName = jsonObject.get(TypeName.CLIENT_NAME).getAsString();

        }
        if (jsonObject.has(TypeName.FIGURE)) {
            figure = jsonObject.get(TypeName.FIGURE).getAsInt();
        }
        if (clientID == getClientID()) {
            setName(clientName);
            setFigure(figure);
            setLoggingIn(false);
            this.viewModel.handlePlayerAdded(this);
            for (Teammate teammate : getTeamMates()) {
                this.viewModel.handleTeammateStatus(teammate, teammate.isRegistered());
            }
        } else {
            Teammate teammate = new Teammate();
            teammate.setPlayerID(clientID);
            teammate.setName(clientName);
            teammate.setFigure(figure);
            addTeammate(teammate);
            this.viewModel.handleTeammateAdded(teammate);
        }
    }

    /**
     * Position of client will be set to chosen position, position gets removed from the list of free starting points
     *
     * @param jsonObject
     * @author Johanna, Florian
     */
    private void processStartingPointTaken(JsonObject jsonObject) {

        if (jsonObject.has(TypeName.COORDINATE_X) && jsonObject.has(TypeName.COORDINATE_Y) && jsonObject.has(TypeName.CLIENT_ID)) {
            int x = jsonObject.get(TypeName.COORDINATE_X).getAsInt();
            int y = jsonObject.get(TypeName.COORDINATE_Y).getAsInt();
            int startingPointClientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            position = new Position(x, y);
            int startingPointIndex;
            logger.info("Starting point sent.");
            for (Teammate teammate : getTeamMates()) {
                if (teammate.getClientID() == startingPointClientID) {
                    if (getKey(board.getStartingPoints(), position) == null) {
                        logger.error(x + ", " + y + " is not a starting point!");
                    } else {
                        startingPointIndex = getKey(board.getStartingPoints(), position);
                        teammate.setStartingPoint(startingPointIndex);
                        teammate.setPositionX(x);
                        teammate.setPositionY(y);

                        if (map.equals(TypeName.DEATH_TRAP)) {
                            teammate.setOrientation("left");
                        } else {
                            teammate.setOrientation("right");
                        }

                        viewModel.showPlayerOnMap(x, y, teammate.getOrientation(), teammate.getFigure());

                    }
                }
            }
            if (this.getClientID() == startingPointClientID) {
                setPositionX(x);
                setPositionY(y);

                if (map.equals(TypeName.DEATH_TRAP)) {
                    setOrientation("left");
                } else {
                    setOrientation("right");
                }

                viewModel.showPlayerOnMap(x, y, getOrientation(), getFigure());
            }
        } else {
            logger.error("Starting point could not be sent.");
        }
    }

    /**
     * Server sends the player status.
     *
     * @param jsonObject
     * @author Kishan
     */
    private void processPlayerStatus(JsonObject jsonObject) {
        Integer clientID = 0;

        if (jsonObject.has(TypeName.CLIENT_ID)) {
            clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        }
        if (jsonObject.has(TypeName.READY)) {
            ready = jsonObject.get(TypeName.READY).getAsBoolean();
        }

        if (clientID == getClientID()) {
            setReady(ready);
            this.viewModel.handlePlayerStatus(this, ready);
        } else {
            for (Teammate teammate : getTeamMates()) {
                if (teammate.getClientID() == clientID) {
                    teammate.setRegistered(ready);
                    this.viewModel.handleTeammateStatus(teammate, ready);
                }
            }
        }
    }

    /**
     * Server sends connection updates of teammates. For the time being only remove will be considered.
     *
     * @param jsonObject
     * @author Kishan
     */
    private void processConnectionUpdate(JsonObject jsonObject) {
        logger.info("Connection update: " + jsonObject);
        Integer clientID = 0;
        ConnectionUpdate connectionUpdate = new ConnectionUpdate();

        if (jsonObject.has(TypeName.CLIENT_ID)) {
            clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        }
        if (jsonObject.has(TypeName.CONNECTED)) {
            connectionUpdate.setConnected(jsonObject.get(TypeName.CONNECTED).getAsBoolean());
        }
        if (jsonObject.has(TypeName.ACTION)) {
            connectionUpdate.setAction(jsonObject.get(TypeName.ACTION).getAsString());
        }
        for (Teammate teammate : getTeamMates()) {
            if (teammate.getClientID() == clientID) {
                teammate.setConnectionUpdate(connectionUpdate);
                this.viewModel.handleConnectionUpdate(teammate);
                return;
            }
        }
    }

    /**
     * Server sends the game map
     *
     * @param jsonObject
     * @author
     */
    private void processGameStarted(JsonObject jsonObject) throws IOException {
        String mapName = "";
        if (jsonObject.has(TypeName.GAME_MAP)) {
            JsonArray messageBody = jsonObject.get(TypeName.GAME_MAP).getAsJsonArray();

            for (int x = 0; x < messageBody.size(); x++) {
                JsonArray tempJsonArray_row = messageBody.get(x).getAsJsonArray();

                for (int y = 0; y < tempJsonArray_row.size(); y++) {

                    JsonArray tempJsonArray_row_column = tempJsonArray_row.get(y).getAsJsonArray();

                    //for (int i = 0; i < tempJsonArray_row_column.size(); i++) {
                    for (int i = tempJsonArray_row_column.size() - 1; i >= 0; i--) {

                        Object map_field = tempJsonArray_row_column.get(i);
                        String map_field_string = map_field.toString();

                        if (!map_field_string.equals("null")) {

                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(map_field_string, JsonElement.class);
                            JsonObject map_field_json = element.getAsJsonObject();


                            if (map_field_json.get("type").toString().equals("\"CheckPoint\"")) {
                                mapName = map_field_json.get("isOnBoard").getAsString();
                                map = mapName;
                                board = new StartingPoints(map);
                                viewModel.handleGameStarted(messageBody, clientReaderTask, teamMates);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void processCardSelected(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.CLIENT_ID) && jsonObject.has(TypeName.REGISTER) && jsonObject.has(TypeName.FILLED)) {
            int clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            int register = jsonObject.get(TypeName.REGISTER).getAsInt();
            boolean filled = jsonObject.get(TypeName.FILLED).getAsBoolean();

            String playerName = this.name;
            for (Teammate teammate : getTeamMates()) {
                if (clientId == teammate.getClientID()) {
                    playerName = teammate.getName();
                    break;
                }
            }

            this.viewModel.handleCardSelected(playerName, register, filled);
        }
    }

    public void processActivePhase(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.PHASE)) {
            phase = jsonObject.get(TypeName.PHASE).getAsInt();
            if (phase == 3) {
                this.viewModel.stopTimer();
            }
        }
        viewModel.showPlayerName(getName());
        viewModel.showMyPlayer(getFigure());
    }


    /**
     * Current amount of energy which is shown in game-window gets updated.
     *
     * @param jsonObject
     * @author Florian
     */
    public void processEnergy(JsonObject jsonObject) {
        int clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        int count = jsonObject.get(TypeName.COUNT).getAsInt();
        this.energy = count;
        if (clientId == getClientID()) {
            viewModel.showEnergy(this.energy);
        }
    }


    public void processCardPlayed(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.CLIENT_ID) && jsonObject.has(TypeName.CARD)) {
            int clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
            String card = jsonObject.get(TypeName.CARD).getAsString();
            String playerName = this.name;
            for (Teammate teammate : getTeamMates()) {
                if (clientId == teammate.getClientID()) {
                    playerName = teammate.getName();
                    break;
                }
            }
            this.viewModel.handleCardPlayed(playerName, card);
        }
    }

    /**
     * Player send clientName and figure to the Server.
     */
    public void sendPlayerValues(String clientName, Integer figure) {
        getClientReaderTask().send(JsonCommands.PlayerValues(clientName, figure));
        logger.info("JSON Message: PlayerValues ->" + JsonCommands.PlayerValues(clientName, figure));
        //this.viewModel.handlePlayerValues(figure);
    }

    /**
     * send name and figure of the AI to the server
     *
     * @author Johanna
     */
    public void sendAIInfo() throws IOException {
        List<String> clientNames = new ArrayList<String>(Arrays.asList("WALL-E", "R2D2", "EVE", "S.A.M", "BEN", "H.E.R.B.I.E", "R.I.C.2.0", "S.O.P.H.I.E", "D.A.V.E.", "SHROUD", "SHOCK", "Mr. R.I.N.G.", "K-9", "Lieutenant Commander Data", "AstroBoy"));
        Integer figure = getRandomFreeFigure();

        getClientReaderTask().send(JsonCommands.PlayerValues(clientNames.get(getRandomNumber(0, 14)), figure));
        logger.info("JSON Message: Playervalues ->" + JsonCommands.PlayerValues(clientNames.get(getRandomNumber(0, 14)), figure));
        getClientReaderTask().send(JsonCommands.SetStatus(true));
        logger.info("JSON Message: PlayerStatus ->" + JsonCommands.SetStatus(true));
    }


    /**
     * @return a random index while making sure that the figure at that index is not already taken
     */
    private Integer getRandomFreeFigure() {
        int rndFigure = getRandomNumber(0, 5);
        List<Integer> takenFigures = new ArrayList<>();

        for (Teammate teammate : getTeamMates()) {
            takenFigures.add(teammate.getFigure());
        }

        while (takenFigures.contains(rndFigure)) {
            rndFigure = getRandomNumber(0, 5);
        }

        return rndFigure;

    }

    /**
     * @return all free starting positions
     * @author Johanna
     */
    private Position getRandomFreeStartingPoint() {
        int rndStartingPointIndex = getRandomNumber(0, 5);
        List<Integer> takenStartingPoints = new ArrayList<>();

        for (Teammate teammate : getTeamMates()) {
            takenStartingPoints.add(teammate.getStartingPoint());
        }

        while (takenStartingPoints.contains(rndStartingPointIndex)) {
            rndStartingPointIndex = getRandomNumber(0, 5);
        }

        Position position = board.getStartingPoints().get(rndStartingPointIndex);
        return position;
    }

    /**
     * @param jsonObject
     * @throws IOException
     * @author Johanna
     */
    private void processYourCards(JsonObject jsonObject) throws IOException, InterruptedException {
        if (jsonObject.has(TypeName.CARDS_IN_HAND)) {
            JsonArray cards = jsonObject.get(TypeName.CARDS_IN_HAND).getAsJsonArray();

            if (!programmingCards.isEmpty()) {
                programmingCards.clear();
                System.out.println("Programming cards are cleared.");
            }

            for (int i = 0; i < cards.size(); i++) {
                programmingCards.add(cards.get(i).getAsString());
            }
            if (!isAI) {
                try {
                    this.viewModel.handleYourProgrammingCards(programmingCards, getClientReaderTask());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isAI) {
                List<String> cardsInRegister = new ArrayList<>();
                for (int j = 1; j < 6; j++) {
                    String card = getRandomCard(programmingCards);
                    while (j == 1 && card.equals("Again")) {
                        card = getRandomCard(programmingCards);
                    }
                    sleep(500);
                    getClientReaderTask().send(JsonCommands.SelectedCard(j, card));
                    cardsInRegister.add(card);
                    programmingCards.remove(card);
                }
            }
        }
    }

    private void processCardsYouGotNow(JsonObject jsonObject) {
        if (jsonObject.has(TypeName.CARDS)) {
            JsonArray cards = jsonObject.get(TypeName.CARDS).getAsJsonArray();

            for (int i = 0; i < cards.size(); i++) {
                String card = cards.get(i).getAsString();
                currentCard.add(card);

            }
            getViewModel().handleCardsYouGotNow(currentCard);
        }
    }

    private void processSelectionFinished(JsonObject jsonObject) {
        int clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        String playerName = this.name;
        for (Teammate teammate : getTeamMates()) {
            if (clientId == teammate.getClientID()) {
                playerName = teammate.getName();
                break;
            }
        }
        viewModel.handleSelectionFinished(playerName);
    }

    private void processTimerEnded(JsonObject jsonObject) {
        List<Integer> clientIds = new ArrayList<>();
        JsonArray tempArray = jsonObject.get(TypeName.CLIENT_IDS).getAsJsonArray();
        for (int i = 0; i < tempArray.size(); i++) {
            clientIds.add(tempArray.get(i).getAsInt());
        }
        getViewModel().showTimerEnded(clientIds);
    }

    private void processTimerStarted(JsonObject jsonObject) {
        getViewModel().showTimer();
    }

    private void processGameFinished(JsonObject jsonObject) {
        Integer winnerId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        String winnerName = this.name;
        for (Teammate teammate : getTeamMates()) {
            if (winnerId == teammate.getClientID()) {
                winnerName = teammate.getName();
                break;
            }
        }
        getViewModel().handleGameFinished(winnerName, winnerId, clientID);
    }

    private void processCheckpointReached(JsonObject jsonObject) {
        Integer clientId = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        Integer number = jsonObject.get(TypeName.NUMBER).getAsInt();
        String playerName = this.name;
        for (Teammate teammate : getTeamMates()) {
            if (clientId == teammate.getClientID()) {
                playerName = teammate.getName();
                break;
            }
        }
        getViewModel().handleCheckpoint(playerName, number);

    }

    /**
     * Method to show the movement of a player on the map.
     * Step 1: remove player from old position on gridPane
     * Step 2: set new position (via Teammate class or via variable in Client class)
     * Step 3: display player at new position on gridPane
     *
     * @author Florian
     */
    private void processMovement(JsonObject jsonObject) {
        Integer movementClientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        Integer movementNewPosX = jsonObject.get(TypeName.POSX).getAsInt();
        Integer movementNewPosY = jsonObject.get(TypeName.POSY).getAsInt();


        for (Teammate teammate : getTeamMates()) {
            if (teammate.getClientID() == movementClientID) {

                //remove player from gridPane
                viewModel.hidePlayerOnMap(teammate.getPositionX(), teammate.getPositionY());

                //set new position
                teammate.setPositionX(movementNewPosX);
                teammate.setPositionY(movementNewPosY);

                //display player at new position on gridPane
                viewModel.showPlayerOnMap(movementNewPosX, movementNewPosY, teammate.getOrientation(), teammate.getFigure());
            }
        }
        if (this.getClientID() == movementClientID) {

            //remove player from gridPane
            viewModel.hidePlayerOnMap(getPositionX(), getPositionY());

            //set new position
            setPositionX(movementNewPosX);
            setPositionY(movementNewPosY);

            //display player at new position on gridPane
            viewModel.showPlayerOnMap(movementNewPosX, movementNewPosY, getOrientation(), getFigure());
        }

    }


    /**
     * Method that turns a players orientation that is shown clockwise or counterclockwise depending on the message received.
     *
     * @author Florian
     */
    private void processPlayerTurning(JsonObject jsonObject) {
        Integer playerTurningClientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        String playerTurningRotation = jsonObject.get(TypeName.ROTATION).getAsString();

        for (Teammate teammate : getTeamMates()) {
            if (teammate.getClientID() == playerTurningClientID) {

                String currentOrientation = teammate.getOrientation();

                //remove from gridPane
                viewModel.removePlayerFromOldField(teammate.getPositionX(), teammate.getPositionY());

                //set new orientation
                if (playerTurningRotation.equals("clockwise")) {
                    if (currentOrientation.equals("top")) {
                        teammate.setOrientation("right");
                    }
                    if (currentOrientation.equals("right")) {
                        teammate.setOrientation("bottom");
                    }
                    if (currentOrientation.equals("bottom")) {
                        teammate.setOrientation("left");
                    }
                    if (currentOrientation.equals("left")) {
                        teammate.setOrientation("top");
                    }
                }
                if (playerTurningRotation.equals("counterclockwise")) {
                    if (currentOrientation.equals("top")) {
                        teammate.setOrientation("left");
                    }
                    if (currentOrientation.equals("left")) {
                        teammate.setOrientation("bottom");
                    }
                    if (currentOrientation.equals("bottom")) {
                        teammate.setOrientation("right");
                    }
                    if (currentOrientation.equals("right")) {
                        teammate.setOrientation("top");
                    }
                }

                //show on gridPane
                viewModel.showPlayerOnMap(teammate.getPositionX(), teammate.getPositionY(), teammate.getOrientation(), teammate.getFigure());

            }
        }

        if (this.getClientID() == playerTurningClientID) {

            String currentOrientation = getOrientation();

            //remove from gridPane
            viewModel.removePlayerFromOldField(getPositionX(), getPositionY());

            //set new orientation
            if (playerTurningRotation.equals("clockwise")) {
                if (currentOrientation.equals("top")) {
                    setOrientation("right");
                }
                if (currentOrientation.equals("right")) {
                    setOrientation("bottom");
                }
                if (currentOrientation.equals("bottom")) {
                    setOrientation("left");
                }
                if (currentOrientation.equals("left")) {
                    setOrientation("top");
                }
            }
            if (playerTurningRotation.equals("counterclockwise")) {
                if (currentOrientation.equals("top")) {
                    setOrientation("left");
                }
                if (currentOrientation.equals("left")) {
                    setOrientation("bottom");
                }
                if (currentOrientation.equals("bottom")) {
                    setOrientation("right");
                }
                if (currentOrientation.equals("right")) {
                    setOrientation("top");
                }
            }

            //show on gridPane
            viewModel.showPlayerOnMap(getPositionX(), getPositionY(), getOrientation(), getFigure());


        }

    }

    /**
     * @param cards
     * @return a random card from the passed List (Cards in hand)
     * @author Johanna
     */
    private String getRandomCard(List<String> cards) {
        String card = cards.get(getRandomNumber(0, cards.size() - 1));

        return card;
    }

    /**
     * creating an object Message and set the message
     */
    private Message getTextMessage(String msg) {
        Message textMessage = new Message();
        textMessage.setSender(getClientID());

        for (Teammate teammate : getTeamMates()) {
            if (msg.contains("@" + teammate.getClientID())) {
                textMessage.addReceiver(teammate.getClientID());
                msg = msg.replaceAll("@" + teammate.getClientID(), "");
            }
        }
        textMessage.setMessage(msg);
        return textMessage;
    }

    /**
     * If the Client exits, the connection and the view will be closed.
     */
    public void close() {
        getClientReaderTask().send(JsonCommands.ConnectionUpdate(getClientID(), false, ConnectionUpdate.REMOVE));
        getClientReaderTask().close();
    }


    /**
     * required setter and getter Methods
     */
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        //viewModel.setPlayer(this);
    }

    public void setLoggingIn(boolean loggingIn) {
        isLoggingIn = loggingIn;
    }

    public boolean isLoggingIn() {
        return isLoggingIn;
    }

    public ClientReaderTask getClientReaderTask() {
        return clientReaderTask;
    }

    public void setClientReaderTask(ClientReaderTask clientReaderTask) {
        this.clientReaderTask = clientReaderTask;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public ConnectionUpdate getConnectionUpdate() {
        return connectionUpdate;
    }

    public void setConnectionUpdate(ConnectionUpdate connectionUpdate) {
        this.connectionUpdate = connectionUpdate;
    }

    public List<Teammate> getTeamMates() {
        return teamMates;
    }

    protected void addTeammate(Teammate teammate) {
        teamMates.add(teammate);
    }

    public List<FieldPosition> getRacingCourseMapList() {
        return racingCourseMapList;
    }

    public String getRacingCourseName() {
        return racingCourseName;
    }

    public void setRacingCourseName(String racingCourseName) {
        this.racingCourseName = racingCourseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isIsAI() {
        return isAI;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    public List<String> getProgrammingCards() {
        return programmingCards;
    }

    public String getMap() {
        return this.map;
    }

    public int getEnergy() {
        return energy;
    }
}
