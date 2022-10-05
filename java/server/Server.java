package server;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.PlayerMat;
import model.*;
import org.apache.log4j.Logger;
import game.Deck;
import org.json.JSONObject;
import server.CardMethods;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static model.JsonCommands.TimerStarted;

/**
 * This class is the server control unit for the interaction with the Director (model logic) and the client.
 */
public class Server {
    public Deck deck1 = new Deck();
    private final Logger logger = Logger.getLogger(Server.class);
    private MessageDistributor messageDistributor;
    public PlayerMat playerMat = new PlayerMat();
    boolean createTurnIterator = true;
    public CardMethods cardMethods;
    boolean createCards = true;
    int clientCount=0;
    boolean createNewOrder = true;
    int CheckPointNum = 0;
    int PriorityAntPosX = 0;
    int PriorityAntPosY = 0;
    int RSPosx = 0;
    String RSOrient = "";
    int RSPosy = 0;
    int CardPlayedCounter = 0;
    int TurnNum = 0;
    int regNum = 0;
    boolean createClientCounter=true;
    ArrayList<String>[] Cards;
    String[] tempArray = new String[10];
    int iteratorCounter = 0;
    int currentCardsIterator = 0;
    int currentCardsNumber = 0;
    boolean createCurrentCardsNumber = true;
    boolean sendCurrentCards = true;
    int registerNum = 0;
    int TurnIterator = 0;
    List<Integer> PlayerOrder = new ArrayList<>();
    int registerCheck = 0;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }

    /**
     * A JSON file is created
     */

    public Server() {

        messageDistributor = new MessageDistributor(this);
    }

    public void CreateTurnIterator() {
        TurnIterator = (playerMat.getPlayerNum().size()) * 5;

    }

    /**
     * An own server Thread for every new Connection created from Server
     */
    private void startServerListener() {
        ServerSocket ss;
        try {
            ss = new ServerSocket(3232);
            logger.info("Server is running...");
            while (true) {
                ServerReaderTask serverReaderTask = new ServerReaderTask(ss.accept(), this);
                serverReaderTask.start();
                AliveThread aliveThread = new AliveThread(serverReaderTask);
                aliveThread.start();
                logger.info("Client is connected!");
            }
        } catch (Exception e) {
            logger.error("An error occurred while starting the client/server connection: ", e);
        }
    }

    /**
     * Server processes the message, message send from server thread and clients,
     * runs the corresponding methods depending on the type
     */
    protected void processMessage(ServerReaderTask serverReaderTask, String msg) throws IOException {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();

        if (jsonObject.has(TypeName.MESSAGE_TYPE)) {
            String jsonPrimitive = jsonObject.getAsJsonPrimitive(TypeName.MESSAGE_TYPE).getAsString();
            if (jsonPrimitive.equals(TypeName.SEND_CHAT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSendChat(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.HELLO_SERVER)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processHelloServer(serverReaderTask, jsonObject);

            } else if (jsonPrimitive.equals(TypeName.PLAYER_VALUES)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processPlayerValues(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SET_STATUS)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSetStatus(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.CONNECTION_UPDATE)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processConnectionUpdate(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.MAP_SELECTED)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processMapSelected(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SET_STARTING_POINT)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                processSetStartingPoint(serverReaderTask, jsonObject);
            } else if (jsonPrimitive.equals(TypeName.SELECTED_CARD)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                try {
                    processSelectedCard(serverReaderTask, jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (jsonPrimitive.equals(TypeName.PLAY_CARD)) {
                jsonObject = jsonObject.getAsJsonObject(TypeName.MESSAGE_BODY);
                CardPlayedCounter++;
                processPlayCard(serverReaderTask, jsonObject);


            }


        }
    }

    public void createPlayerValues(int ClientId) {

        List<Integer> Posx = new ArrayList<>();
        List<Integer> Posy = new ArrayList<>();
        List<Integer> ClIds = new ArrayList<>();
        List<Integer> PlNum = new ArrayList<>();
        List<Integer> Point = new ArrayList<>();
        List<Integer> EnCubes = new ArrayList<>();
        List<Integer> PlLives = new ArrayList<>();
        List<String> PlOrient = new ArrayList<>();
        List<String> PrCard = new ArrayList<>();
        Posx = playerMat.getListPosx();
        Posy = playerMat.getListPosy();
        ClIds = playerMat.getClientIdsList();
        PlNum = playerMat.getPlayerNum();
        Point = playerMat.getListPoints();
        EnCubes = playerMat.getListCubes();
        PlLives = playerMat.getListLives();
        PlOrient = playerMat.getListOrientation();
        PrCard = playerMat.getPreviousCard();
        PlNum.add(ClIds.size());
        ClIds.add(ClientId);
        Point.add(0);
        EnCubes.add(1);
        PlLives.add(10);
        Posx.add(0);
        Posy.add(0);
        PlOrient.add("right");
        PrCard.add("MoveI");
        playerMat.setClientIdsList(ClIds);
        playerMat.setPoints(Point);
        playerMat.setenergyCubes(EnCubes);
        playerMat.setPlayerNum(PlNum);
        playerMat.setplayerOrientation(PlOrient);
        playerMat.setLives(PlLives);
        playerMat.setPreviousCard(PrCard);
        cardMethods = new CardMethods();
        cardMethods.setObj(playerMat);
    }

    public void setCardsObj(CardMethods cardMethods) {
        this.cardMethods = cardMethods;
    }

    public void processSetStartingPoint(ServerReaderTask serverReaderTask, JsonObject jsonObject) {

        List<Integer> PositionsX = playerMat.getListPosx();
        List<Integer> PositionsY = playerMat.getListPosy();
        List<Integer> Players = playerMat.getPlayerNum();
        List<Integer> ids = playerMat.getClientIdsList();

        int PosX = jsonObject.get(TypeName.COORDINATE_X).getAsInt();
        int PosY = jsonObject.get(TypeName.COORDINATE_Y).getAsInt();
        int ClientId = serverReaderTask.getPlayerInformation().getClientID();
        int playerNum = ids.indexOf(ClientId);
        clientCount++;
        PositionsX.set(playerNum, PosX);
        PositionsY.set(playerNum, PosY);
        playerMat.setposx(PositionsX);
        playerMat.setplayerPosY(PositionsY);
        sendCheckpointTaken(PosX, PosY, ClientId);
        int clientId = serverReaderTask.getPlayerInformation().getClientID();

        if(clientCount==playerMat.getClientIdsList().size())
        {


            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null){
                    List<String> deck = null;
                    try {
                        deck = deck1.Give9Cards();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getServerThread().send(JsonCommands.YourCards(deck.get(0), deck.get(1), deck.get(2), deck.get(3), deck.get(4), deck.get(5), deck.get(6), deck.get(7), deck.get(8), player.getClientID()));
                }


            }}
    }


    public void sendCheckpointTaken(int x, int y, int ClientId) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.StartingPointTaken(x, y, ClientId));
        }
    }

    public void sendCurrentPlayer() {
        System.out.println("Current Player called");
        int clientId = playerMat.getClientIdsList().get(PlayerOrder.get(0));
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                if (player.getClientID() == clientId) {
                    System.out.println("THIS IS ORDER OF PLAYERS clientid: " + PlayerOrder + "and this was sent to client id: " + clientId);
                    int currentPl = PlayerOrder.get(0);
                    PlayerOrder.remove(0);
                    player.getServerThread().send(JsonCommands.CurrentPlayer(playerMat.getClientIdsList().get(currentPl)));
                }
            }
        }


    }

    public void sendGameStarted(JSONObject jsonMes) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(jsonMes);
            }
        }
    }

    private void processPlayCard(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        cardMethods.setObj(playerMat);
        String card = jsonObject.get(TypeName.CARD).getAsString();
        int clientId = serverReaderTask.getPlayerInformation().getClientID();
        System.out.println("THIS IS POS X BEFORE  PLAYING THE CARD: " + playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(clientId)) + " AND THIS IS POS Y: " + playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(clientId)));
        System.out.println("Card played: " + card);
        if (card.equalsIgnoreCase("Again")) {
            card = playerMat.getPreviousCard().get(playerMat.getClientIdsList().indexOf(clientId));
            System.out.println("Again card played! This is previous card: " + card);
            int plNum = playerMat.getClientIdsList().indexOf(clientId);
            Cards[plNum].remove(0);
            cardMethods.setObj(playerMat);
            try {
                cardMethods.setObj(playerMat);
                cardMethods.handleMove(playerMat.getClientIdsList().indexOf(clientId), card, playerMat.getClientIdsList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            int plNum = playerMat.getClientIdsList().indexOf(clientId);
            if (Cards[plNum].indexOf(card) != -1) {
                System.out.println("sent Handle Move with card");
                System.out.println("WE REMOVED THIS CARD: " + Cards[plNum].get(0));
                List<String> prevCards = playerMat.getPreviousCard();
                prevCards.set(plNum, card);
                playerMat.setPreviousCard(prevCards);
                Cards[plNum].remove(0);
                cardMethods.setObj(playerMat);
                try {
                    cardMethods.setObj(playerMat);
                    cardMethods.handleMove(playerMat.getClientIdsList().indexOf(clientId), card, playerMat.getClientIdsList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MoveHandler();
    }

    public List<Integer> whoIsNext1(int posxAn, int posyAn) {

        int iterator = 0;
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> values = new ArrayList<>();
        List<Integer> playerData = new ArrayList<>();
        List<Integer> nextPlayer = new ArrayList<>();
        for (int i = playerPosX.size(); i > 0; i--) {
            values.add(Math.abs(posyAn - playerPosY.get(iterator)) + playerPosX.get(iterator) - posxAn);

            playerData.add(values.get(iterator));
            iterator++;

        }
        Collections.sort(values);
        for (int i = 0; i < values.size(); i++) {
            nextPlayer.add(playerData.indexOf(values.get(i)));

        }
        for (int i = 0; i < nextPlayer.size() - 1; i++) {
            if (nextPlayer.get(i) == nextPlayer.get(i + 1)) {
                nextPlayer.set(i + 1, nextPlayer.get(i) + 1);
            }

        }
        return nextPlayer;
    }

    public void sendCurrentCards(int register) {
        cardMethods.setObj(playerMat);
        for (int c = 0; c < playerMat.getClientIdsList().size(); c++) {
            System.out.println("HERE CARDS OF ALL PLAYERS " + Cards[c]);

        }
        List<String> currentCards = new ArrayList<>();
        for (int i = 0; i < playerMat.getClientIdsList().size(); i++) {

            currentCards.add(Cards[i].get(0));//maybe wrong
        }
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.CurrentCards(currentCards, playerMat.getClientIdsList()));
            }
        }

    }

    public void TimerStarted()  {
        cardMethods.setObj(playerMat);
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.TimerStarted());
            }
        }
        TimerHasEndedTimerOff();

        //    TimerHasEnded(cardFill());

    }
    public void TimerHasEndedTimerOff() {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.ActivePhase(3));
            }
        }
        cardMethods.setObj(playerMat);
        MoveHandler();
    }
    public void TimerHasEnded(List<Integer> ClientIds) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.TimerEnded(ClientIds));
                player.getServerThread().send(JsonCommands.ActivePhase(3));
            }
        }
        cardMethods.setObj(playerMat);
        MoveHandler();
    }

    public List<Integer> cardFill() {
        cardMethods.setObj(playerMat);
        List<Integer> cliendIds = new ArrayList<>();
        int PlayerCount = 0;
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                PlayerCount++;
        }
        for (int i = 0; i < PlayerCount; i++) {
            if (Cards[i].size() < 5) {
                int cardsToFill = 5 - Cards[i].size();
                for (; cardsToFill > 0; cardsToFill--) {
                    // Cards[i].add(deck1.GiveCard());
                    if (cliendIds.indexOf(i) == -1) {
                        cliendIds.add(i);
                    }
                }
            }

        }
        cardMethods.setObj(playerMat);
        return cliendIds;
    }


    public void createCurrentCardsNum() {
        currentCardsNumber = playerMat.getClientIdsList().size();

    }

    /*Sends a client who should play a card now a "CurrentPlayer" message,
    creates a new player order when all cards of register were played,
    if all cards from all 5 registers were played, then start Programming phase
     */
    public void MoveHandler() {
        cardMethods.setObj(playerMat);
        System.out.println("Move Handle");
        if (createTurnIterator == true) {
            CreateTurnIterator();
            createTurnIterator = false;
        }
        if (createNewOrder == true) {
            PlayerOrder = whoIsNext1(PriorityAntPosX, PriorityAntPosY);
            createNewOrder = false;
        }
        if (createCurrentCardsNumber == true) {
            createCurrentCardsNum();
            createCurrentCardsNumber = false;
        }
        if (sendCurrentCards == true) {
            sendCurrentCards(registerNum);
            registerNum++;
            sendCurrentCards = false;
        }
        boolean NextRound = false;
        for (int v = 0; v < playerMat.getClientIdsList().size(); v++) {
            if (Cards[v].size() > 0) {
                NextRound = true;
            }
        }
        if (NextRound) {
            if (currentCardsIterator == currentCardsNumber) {
                currentCardsIterator = 1;
                System.out.println("SENT CURRENT CARDS with register: " + registerNum);
                int curReg = registerNum;
                registerNum++;

                sendCurrentCards(curReg);

            } else {
                currentCardsIterator++;
            }
            if (iteratorCounter == playerMat.getPlayerNum().size()) {
                System.out.println("CARDS IN REGISTER ENDED!");
                PlayerOrder = whoIsNext1(PriorityAntPosX, PriorityAntPosY);
                iteratorCounter = 0;
                try {
                    cardMethods.setPlayerf(messageDistributor);
                    System.out.println("Here currentField is called.!!!!!!!!");

                    cardMethods.currentFieldAction(registerNum - 1);

                    checkPointCheck();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cardMethods.playerShooting();
                if (registerNum > 4) {
                    registerNum = 0;
                }


            }
            TurnIterator--;
            iteratorCounter++;
            sendCurrentPlayer();

        } else {
            System.out.println("all registers ended");
            CreateTurnIterator();
            iteratorCounter = 0;
            startProgrammingPhase();
        }
    }

    public void checkPointCheck() {
        for (int i = 0; i < playerMat.getClientIdsList().size(); i++) {
            int playerCheckPoints = playerMat.getListPoints().get(i);
            if (playerCheckPoints == CheckPointNum) {
                for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                    if (player.getServerThread() != null) {
                        player.getServerThread().send(JsonCommands.GameFinished(playerMat.getClientIdsList().get(i)));
                    }
                }
            }
        }

    }

    public void setCheckpointNum(int checkpoints, int AntPosx, int AntPosy, int ResPosX, int ResPosy, String ResOrient) {
        this.CheckPointNum = checkpoints;
        this.PriorityAntPosX = AntPosx;
        this.PriorityAntPosY = AntPosy;
        this.RSPosx = ResPosX;
        this.RSPosy = ResPosy;
        this.RSOrient = ResOrient;
        playerMat.setResOrient(RSOrient);
        List<Integer> tempList=new ArrayList<>();
        tempList.add(RSPosx);
        tempList.add(RSPosy);
        playerMat.setResPosXY(tempList);
        cardMethods.setCheckpointNum(checkpoints, AntPosx, AntPosy, ResPosX, ResPosy, ResOrient);
    }

    public void startProgrammingPhase() {
        for (int i = 0; i < playerMat.getClientIdsList().size(); i++) {
            for (int b = 0; b < Cards[i].size(); b++) {
                Cards[i].remove(b);
            }

        }
        createNewOrder = true;
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.ActivePhase(2));
            }
        }
        List<String> deck = null;
        try {
            deck = deck1.Give9Cards();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {//todo Wait for all players and then send cards
            if (player.getServerThread() != null)
                System.out.println("New round starts!");
            player.getServerThread().send(JsonCommands.YourCards(deck.get(0), deck.get(1), deck.get(2), deck.get(3), deck.get(4), deck.get(5), deck.get(6), deck.get(7), deck.get(8), player.getClientID()));
        }
    }


    /**
     * Sends a message to the user specified by ReceiverName
     */
    protected void send(PlayerInformation playerInformation, String message, Integer receiverID) {
        boolean isPrivate = true;
        for (PlayerInformation receiver : messageDistributor.getPlayerInformationList()) {
            if (receiverID == receiver.getClientID()) {
                receiver.getServerThread().send(JsonCommands.ReceivedChat(message, playerInformation.getClientID(), isPrivate));
            }
        }
    }

    /**
     * method broadcast() do:
     * --The server broadcasts a message to all other users including the username of the user who wrote the chat
     * --The server broadcasts a message to the user who wrote the message
     */
    protected void broadcast(PlayerInformation playerInformation, String message) {
        boolean isPrivate = false;
        for (PlayerInformation receiver : messageDistributor.getPlayerInformationList()) {
            if (playerInformation.getClientID() != receiver.getClientID()) {
                receiver.getServerThread().send(JsonCommands.ReceivedChat(message, playerInformation.getClientID(), isPrivate));
            }
        }
    }

    /**
     * Client sends the protocol version, the group name
     */
    private void processHelloServer(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        if (jsonObject.has(TypeName.PROTOCOL)) {
            String clientProtocol = jsonObject.get(TypeName.PROTOCOL).getAsString();
            if (!TypeName.SERVER_PROTOCOL.equals(clientProtocol)) {
                sendErrorMessage(serverReaderTask, "Protocol mismatch server/client (" + TypeName.SERVER_PROTOCOL + "/" + clientProtocol + ")");
            } else {
                String groupName = "";
                boolean isAI = false;
                if (jsonObject.has(TypeName.GROUP)) {
                    groupName = jsonObject.get(TypeName.GROUP).getAsString();
                }
                if (jsonObject.has(TypeName.IS_AI)) {
                    isAI = jsonObject.get(TypeName.IS_AI).getAsBoolean();
                }
                PlayerInformation playerInformation = messageDistributor.createPlayerInformation();
                createPlayerValues(playerInformation.getClientID());


                //if(!isAI){
                playerInformation.setServerThread(serverReaderTask);
                serverReaderTask.setPlayerInformation(playerInformation);
                // } //TODO else AI -> playerInformation
                messageDistributor.handleHelloServer(playerInformation, groupName, isAI);

            }

        }
    }

    /**
     * Send error message.
     */
    protected void sendErrorMessage(PlayerInformation playerInformation, String errorMsg) {

        sendErrorMessage(playerInformation.getServerThread(), errorMsg);
    }

    /**
     * Send error message.
     */
    private void sendErrorMessage(ServerReaderTask serverReaderTask, String errMsg) {
        String message = "Try again, Did not Work!! ";
        if (errMsg == null || errMsg.isEmpty())
            message = message + "Try to adjust something.";
        else
            message = message + errMsg;
        serverReaderTask.send(JsonCommands.Error(message));
    }

    /**
     * method processSendChat() do:
     * --Sends a message to the client.
     * --If Receiver contains user the message will be send only to these users(private chat)
     * --otherwise it will be send to all users
     */
    private void processSendChat(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        if (jsonObject.has(TypeName.MESSAGE)) {
            String message = jsonObject.get(TypeName.MESSAGE).getAsString();
            Integer receiver = jsonObject.get(TypeName.TO).getAsInt();
            messageDistributor.handleSendChat(serverReaderTask.getPlayerInformation(), message, receiver);
        }
    }

    /**
     * Client sends player name and figure.
     */
    private void processPlayerValues(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        String name = "";
        Integer figure = 0;
        if (jsonObject.has(TypeName.CLIENT_NAME))
            name = jsonObject.get(TypeName.CLIENT_NAME).getAsString();
        if (jsonObject.has(TypeName.FIGURE))
            figure = jsonObject.get(TypeName.FIGURE).getAsInt();
        messageDistributor.handlePlayerValues(serverReaderTask.getPlayerInformation(), name, figure);
    }


    /**
     * Client set the status ready or unready.
     */
    private void processSetStatus(ServerReaderTask serverReaderTask, JsonObject jsonObject) throws IOException {
        boolean ready = true;
        if (jsonObject.has(TypeName.READY))
            ready = jsonObject.get(TypeName.READY).getAsBoolean();
        messageDistributor.handleSetStatus(serverReaderTask.getPlayerInformation(), ready);
    }


    /**
     * Player sends which map should be played.
     */
    private void processMapSelected(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        if (jsonObject.has(TypeName.MAP)) {
            String mapName = jsonObject.get(TypeName.MAP).getAsString();
            messageDistributor.mapSelect(mapName);

        }
    }

    public void setMap(File playerfile) {

        cardMethods = new CardMethods();
        cardMethods.setFile(playerfile);
        String contentMap = null;
        try {
            contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
            JSONObject jsonMes = new JSONObject(contentMap);
            sendGameStarted(jsonMes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Client sends connection update
     */
    private void processConnectionUpdate(ServerReaderTask serverReaderTask, JsonObject jsonObject) {
        Integer clientID = 0;
        boolean isConnected = true;
        String action = "";

        if (jsonObject.has(TypeName.CLIENT_ID)) {
            clientID = jsonObject.get(TypeName.CLIENT_ID).getAsInt();
        }
        if (jsonObject.has(TypeName.CONNECTED)) {
            isConnected = jsonObject.get(TypeName.CONNECTED).getAsBoolean();
        }
        if (jsonObject.has(TypeName.ACTION)) {
            action = jsonObject.get(TypeName.ACTION).getAsString();
        }
        messageDistributor.handleConnectionUpdate(serverReaderTask.getPlayerInformation(), clientID, isConnected, action);
    }

    /**
     * Server sends welcome to the client.
     */
    protected void sendWelcome(PlayerInformation playerInformation) {

        playerInformation.getServerThread().send(JsonCommands.Welcome(playerInformation.getClientID()));

    }


    /**
     * if a Client(Player) added than confirmed
     */
    protected void broadcastPlayerAdded(PlayerInformation playerInformation) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.PlayerAdded(playerInformation.getClientID(),
                        playerInformation.getName(), playerInformation.getFigure()));
        }
    }

    /**
     * Broacast players status.
     */
    protected void broadcastPlayerStatus(PlayerInformation playerInformation, boolean status) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.PlayerStatus(playerInformation.getClientID(), status));
        }
    }


    /**
     * Broadcast connection update of the players.
     */
    protected void broadcastConnectionUpdate(Integer clientID, boolean isConnector, String action) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.ConnectionUpdate(clientID, isConnector, action));
        }
    }

    /**
     * Player gets information successful added.
     */
    protected void sendPlayerAdded(PlayerInformation playerInformation, PlayerInformation teammate) {


        playerInformation.getServerThread().send(JsonCommands.PlayerAdded(teammate.getClientID(),
                teammate.getName(), teammate.getFigure()));
    }

    /**
     * Players gets current status.
     */
    protected void sendPlayerStatus(PlayerInformation playerInformation, PlayerInformation teammate) {


        playerInformation.getServerThread().send(JsonCommands.PlayerStatus(teammate.getClientID(), teammate.isReady()));
    }


    /**
     * Player gets the selected map of the game.
     */
    public void sendSelectMap(PlayerInformation playerInformation, String[] maps) {

        playerInformation.getServerThread().send(JsonCommands.SelectMap(maps));
    }


    /**
     * Broadcast, that a player has selected a map.
     */
    public void broadcastMapSelected(String map) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.MapSelected(map));
        }
    }


    /**
     * Creating an object TextMessage and set the message
     */
    private Message getTextMessage(String msg) {
        Message textMessage = new Message();
        textMessage.setMessage(msg);
        return textMessage;
    }

    /**
     * Starts the server
     */
    public static void main(String[] args) {
        new Server().startServerListener();
    }

    public void sendDeck9Cards(int CliendID, List<String> Deck9Cards) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.YourCards(Deck9Cards.get(0), Deck9Cards.get(1), Deck9Cards.get(2), Deck9Cards.get(3), Deck9Cards.get(4), Deck9Cards.get(5), Deck9Cards.get(6), Deck9Cards.get(7), Deck9Cards.get(8), CliendID));
            player.getServerThread().send(JsonCommands.NotYourCards(CliendID, Deck9Cards.size()));
        }
    }


    public void sendMove(int CliendID) {
        int PosX = playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(CliendID));
        int PosY = playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(CliendID));
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                player.getServerThread().send(JsonCommands.Movement(CliendID, PosX, PosY));
            }
        }

    }
    public void sendRotation(int CliendID, String Rotation) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.PlayerTurning(CliendID, Rotation));
        }
    }


    public void sendGameFinished(int CliendID) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.GameFinished(CliendID));
        }
    }

    public void closeConnection(ServerReaderTask serverReaderTask) {
        messageDistributor.handleCloseConnection(serverReaderTask.getPlayerInformation());
    }

    public void sendGiveCard(PlayerInformation playerInformation, String cardName) {

        playerInformation.getServerThread().send(JsonCommands.GiveCard(cardName));
    }

    protected void GiveCard(PlayerInformation playerInformation, String cardName) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.PlayerAdded(playerInformation.getClientID(),
                        playerInformation.getName(), playerInformation.getFigure()));
        }
    }


    public void processSelectedCard(ServerReaderTask serverReaderTask, JsonObject jsonObject) throws IOException {
        if(playerMat.getResPosXY().get(0)==12){
            List<String> tempList= playerMat.getListOrientation();
            for(int i=0;i<playerMat.getClientIdsList().size();i++){
                tempList.set(i,"left");
            }
            playerMat.setplayerOrientation(tempList);
        }
        List<Integer> registerList = new ArrayList<>();
        registerList = playerMat.getRegisterTaken();
        String cardName = jsonObject.get("card").getAsString();
        System.out.println("card name " + cardName);
        int register = jsonObject.get("register").getAsInt();
        int clID = serverReaderTask.getPlayerInformation().getClientID();
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                player.getServerThread().send(JsonCommands.CardSelected(register,
                        clID, true));
        }

        if (createCards == true) {
            createCards = false;
            Cards = new ArrayList[playerMat.getClientIdsList().size()];
            for (int i = 0; i < playerMat.getClientIdsList().size(); i++) {
                Cards[i] = new ArrayList<String>();
            }
        }
        int plNum = playerMat.getClientIdsList().indexOf(clID);
        System.out.println("Player Num: " + plNum);
        plNum = playerMat.getClientIdsList().indexOf(clID);
        Cards[plNum].add(cardName);
        System.out.println("CARD SAVED NUM "+Cards[0].size()+ "  "+Cards[1].size());
        if (Cards[0].size() > 4 && Cards[1].size() > 4) {
            System.out.println("Saved cards: " + Cards[plNum]);
            TimerStarted();
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null)

                    SelectionFinished(player.getClientID());
            }




        }
    }


    public void sendDrawDamage(Integer clientID, List<String> damageCards) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.DrawDamage(clientID, damageCards));
        }
    }

    public void sendPickDamage(Integer clientID, Integer count) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                if (player.getClientID() == clientID) {
                    player.getServerThread().send(JsonCommands.PickDamage(count));
                }


        }
    }

    public void sendAnimation(Integer clientID, String type) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                if (player.getClientID() == clientID) {
                    player.getServerThread().send(JsonCommands.Animation(type));
                }
        }
    }

    public void sendReboot(Integer clientID) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady()) {
                player.getServerThread().send(JsonCommands.Reboot(clientID));
            }
        }
    }

    public void sendRebootDirection(Integer clientID, String direction) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null)
                if (player.getClientID() == clientID) {
                    player.getServerThread().send(JsonCommands.RebootDirection(direction));
                }
        }


    }

    public void sendEnergy(Integer clientID, Integer count, String source) {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {


            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.Energy(clientID, 1, source));
        }
    }


    public void SelectionFinished(Integer ClientId) {
        cardMethods.setPlayerf(messageDistributor);
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null && player.isReady())
                player.getServerThread().send(JsonCommands.SelectionFinished(ClientId));
        }
    }

    public void LaserShot(Integer player1, Integer player2) {
        List<Integer> ClientIds = playerMat.getClientIdsList();
        int clientId1 = ClientIds.get(player1);
        int clientId2 = ClientIds.get(player2);
        sendAnimation(clientId1, "PlayerShooting");
        sendPickDamage(clientId2, 1);
    }

    public void SetObjServ(PlayerMat playerMat) {
        this.playerMat = playerMat;
    }
}

