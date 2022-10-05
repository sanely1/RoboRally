package server;

import game.Deck;
import game.PlayerMat;
import model.*;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.List;

/**
 * This class contains the complete functionality and logic for running and controlling the game.
 */

public class MessageDistributor {
    public String[] maps = {"Dizzy Highway", "Lost Bearings", "Death Trap", "Extra Crispy"};
    public boolean OnlyAis = true;
    private final Logger logger = Logger.getLogger(MessageDistributor.class);
    public PlayerMat playerMat = new PlayerMat();
    private boolean gameStarted = false;
    boolean sendMap = true;
    private String groupName;
    private Integer AICounter = 0;
    private List<PlayerInformation> readyPlayerList = new ArrayList<>();
    private List<PlayerInformation> playerInformationList = new ArrayList<>();
    private Server server;
    public Deck value;
    public PlayerInformation info;
int plCount=0;

    /**
     * Initialising the instance variables values
     */
    public MessageDistributor(Server server) {
        this.server = server;
    }

    /**
     * handleHelloServer() do:
     * --Player will be registered on server.
     * --Server sends information of teammates as well as the players status back to client.
     */
    protected void handleHelloServer(PlayerInformation playerInformation, String groupName, boolean isAI) {

        this.groupName = groupName;
        playerInformation.setAI(isAI);
        playerInformationList.add(playerInformation);

        server.sendWelcome(playerInformation);

        for (PlayerInformation teammate : getPlayerInformationList()) {
            if ((playerInformation.getClientID() != teammate.getClientID()) && teammate.isAdded())
                server.sendPlayerAdded(playerInformation, teammate);
            server.sendPlayerStatus(playerInformation, teammate);
        }
        synchronized (this) {
            try {
                this.wait(50);
            } catch (InterruptedException e) {
                logger.error("There was an exception server.MessageDistributor.handleHelloServer : ", e);
            }
        }
        if (isAI) {
            server.broadcastPlayerStatus(playerInformation, true);
        }
    }

    /**
     * Server creates a new player and determines a random id.
     */
    protected PlayerInformation createPlayerInformation() {
        PlayerInformation playerInformation = new PlayerInformation();
        Random random = new Random();
        boolean idSet = false;
        int randomId = 0;
        while (!idSet) {
            randomId = random.nextInt(100);
            idSet = true;
            for (PlayerInformation otherPlayer : getPlayerInformationList()) {
                if (otherPlayer.getClientID() == randomId) {
                    idSet = false;
                    break;
                }
            }
        }

        playerInformation.setClientID(randomId);
        return playerInformation;
    }

    /**
     * get all players.
     */
    public List<PlayerInformation> getPlayerInformationList() {
        return playerInformationList;
    }


    /**
     * Server confirms figure and name of the player.
     */
    protected void handlePlayerValues(PlayerInformation playerInformation, String name, Integer figure) {
        for (PlayerInformation otherPlayerInformation : playerInformationList) {
            if (otherPlayerInformation.getFigure() == figure) {
                server.sendErrorMessage(playerInformation, "Figure " + figure + " is already in use! Try another");
                return;
            }
            if (!TypeName.FIGURES.contains(figure.toString())) {
                server.sendErrorMessage(playerInformation, "Figure " + figure + " is invalid!");
                return;
            }
        }
        playerInformation.setFigure(figure);
        playerInformation.setName(name);
        playerInformation.setAdded(true);
        server.broadcastPlayerAdded(playerInformation);
    }


    /**
     * Calls handleConnectionUpdate for removing a player
     */
    protected void handleCloseConnection(PlayerInformation playerInformation) {
        handleConnectionUpdate(playerInformation, playerInformation.getClientID(), false, ConnectionUpdate.REMOVE);
    }


    /**
     * Player will be deleted from ready players.
     * If player is required to choose the game map, the requirement will be transferred to next ready player in row.
     */
    protected void handleConnectionUpdate(PlayerInformation playerInformation, Integer playerId, boolean isConnected, String action) {
        if (action.equals(ConnectionUpdate.REMOVE) && playerInformationList.contains(playerInformation)) {
            if(readyPlayerList.contains(playerInformation)){
                readyPlayerList.remove(playerInformation);
            }
        }
    }


    /**
     * method handleSetStatus do:
     * --Set status and broadcast it.
     * --If status is unready (false) player will be delete from ready players.
     * --If all players ready the game will be started
     */
    protected void handleSetStatus(PlayerInformation playerInformation, boolean status) throws IOException {
        System.out.println("GOT SET STATUS");
        if (status && getReadyPlayerList() == TypeName.MAX_PLAYER) {
            server.broadcastPlayerStatus(playerInformation, false);
            server.sendErrorMessage(playerInformation, "Sorry. There are already a maximum of players");
            return;

        }

        playerInformation.setReady(status);
        server.broadcastPlayerStatus(playerInformation, status);

        List<PlayerInformation> toBeDeleted = new ArrayList<>();
        for (PlayerInformation player : readyPlayerList) {
            if (!playerInformationList.contains(player)) {
                toBeDeleted.add(player);
            }
        }
        if (toBeDeleted.size() > 0)
            readyPlayerList.removeAll(toBeDeleted);

        if (status) {
            if (!readyPlayerList.contains(playerInformation)) {
                readyPlayerList.add(playerInformation);
                if (readyPlayerList.size() == 1 && !playerInformation.isAI())
                    playerInformation.setSelectMap(true);
                if (!playerInformation.isAI()) {
                    OnlyAis = false;
                }
                if (playerInformation.isAI()) {
                    playerInformation.setIsAI(true);
                }
                plCount++;
                int tempCount=0;
                for(PlayerInformation player : getPlayerInformationList()) {
                    tempCount++;
                }
                for (PlayerInformation player : getPlayerInformationList()) {
                    System.out.println(plCount);
                    System.out.println(playerMat.getClientIdsList().size());
if(plCount==tempCount && tempCount>1){
                    if (sendMap) {

                        System.out.println("SEND SELECT MAP");
                        player.getServerThread().send(JsonCommands.SelectMap(maps));
                        sendMap = false;

                    }}
            }}
        } else {
            readyPlayerList.remove(playerInformation);
            if (playerInformation.isSelectMap()) {
                playerInformation.setSelectMap(false);
                for (PlayerInformation readyPlayer : readyPlayerList) {
                    if (!readyPlayer.isAI()) {
                        readyPlayer.setSelectMap(true);
                        break;
                    }
                }
            }
        }
            try {
                if (OnlyAis) {
                    handleStartGame();
                }
            } catch (IOException e) {
            }
        }




    public void mapSelect(String map) {
        System.out.println(String.valueOf(this.getClass().getResource("/dz.json")).replace("file:", ""));

        System.out.println("MAP: " + map);
        if (map.equalsIgnoreCase("Dizzy Highway")) {

            File playerFile = new File(String.valueOf(this.getClass().getResource("/dz.json")).replace("file:", ""));
            server.setCheckpointNum(1, 0, 4, 7, 3, "bottom");
            server.setMap(playerFile);
        }
        if (map.equalsIgnoreCase("Lost Bearings")) {
            File playerFile = new File(String.valueOf(this.getClass().getResource("/lb.json")).replace("file:", ""));
            server.setCheckpointNum(4, 0, 4, 0, 0, "right");
            server.setMap(playerFile);
        }
        if (map.equalsIgnoreCase("Death Trap")) {
            File playerFile = new File(String.valueOf(this.getClass().getResource("/dt.json")).replace("file:", ""));
            server.setCheckpointNum(5, 12, 5, 12, 9, "left");
            server.setMap(playerFile);
        }
        if (map.equalsIgnoreCase("Extra Crispy")) {
            File playerFile = new File(String.valueOf(this.getClass().getResource("/ec.json")).replace("file:", ""));
            server.setCheckpointNum(5, 0, 4, 0, 0, "right");
            server.setMap(playerFile);
        }
        for (PlayerInformation player : getPlayerInformationList()) {
            player.getServerThread().send(JsonCommands.ActivePhase(0));
            player.getServerThread().send(JsonCommands.CurrentPlayer(player.getClientID()));
        }


    }

    /**
     * Send private or public message
     */
    protected void handleSendChat(PlayerInformation playerInformation, String message, Integer receiver) {
        if (receiver == TypeName.TO_ALL) {
            server.broadcast(playerInformation, message);
        } else {
            server.send(playerInformation, message, receiver);
        }
    }

    public void handleStartGame() throws IOException {
        Random rand = new Random();
        int n = rand.nextInt(4);
        if (n == 0) {
            mapSelect("DizzyHighway");
        }
        if (n == 1) {
            mapSelect("LostBearings");
        }
        if (n == 2) {
            mapSelect("DeathTrap");
        }
        if (n == 3) {
            mapSelect("ExtraCrispy");
        }
    }
    /**
     * Determines the number of ready players.
     */
    private int getReadyPlayerList() {
        int readyCounter = 0;
        for (PlayerInformation playerInformation : playerInformationList) {
            if (playerInformation.isReady())
                readyCounter++;
        }
        return readyCounter;
    }

}

