package server;

import game.*;
import model.JsonCommands;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMethods {
    int CheckPointNum = 0;
    int PriorityAntPosX = 0;
    int PriorityAntPosY = 0;
    int RSPosx = 0;
    String RSOrient = "";
    int RSPosy = 0;
    private PlayerMat playerMat;
    public File playerfile;
public MessageDistributor messageDistributor;
public void setPlayerf(MessageDistributor messageDistributor){
    this.messageDistributor=messageDistributor;
}
    public File getPlayerfile() {
        return playerfile;
    }

    public void setCheckpointNum(int checkpoints, int AntPosx, int AntPosy, int ResPosX, int ResPosy, String ResOrient) {
        this.CheckPointNum = checkpoints;
        this.PriorityAntPosX = AntPosx;
        this.PriorityAntPosY = AntPosy;
        this.RSPosx = ResPosX;
        this.RSPosy = ResPosy;
        this.RSOrient = ResOrient;
    }

    public void setFile(File playerfile) {
        this.playerfile = playerfile;
    }

    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }

    ConveyorBelt conveyorBelt = new ConveyorBelt();
    EnergySpaces energySpaces = new EnergySpaces();
    Checkpoint checkpoint = new Checkpoint();
    Wall wall = new Wall();
    Gear gear = new Gear();
    Pit pit = new Pit();
    PushPanel pushPanel = new PushPanel();
    Server server = new Server();

    public void moveBack(int playerNum) throws IOException {
        boolean restart=false;
        int ResPosX=playerMat.getResPosXY().get(0);
        int ResPosY=playerMat.getResPosXY().get(1);
        String ResOrient=playerMat.getResOrient();
        int counter=TurningCounter(ResOrient,playerMat.getListOrientation().get(playerNum));
        List<Integer> points = server.playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        if (canMove(playerNum)) {
            if (plOrient.equalsIgnoreCase("left")) {
                currentPlayerPosX = currentPlayerPosX + 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("right")) {
                currentPlayerPosX = currentPlayerPosX - 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("bottom")) {
                currentPlayerPosY = currentPlayerPosY - 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("top")) {
                currentPlayerPosY = currentPlayerPosY + 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }

        }
        playerMat.setposx(playerPosX);
        playerMat.setplayerPosY(playerPosY);
        playerMat.setplayerOrientation(playerOrientation);
        int ClientID=playerMat.getClientIdsList().get(playerNum);
        if(restart){
        for(int i=0;i<counter;i++){
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                System.out.println("TURNING WORKED !!!!!!");
                player.getServerThread().send(JsonCommands.PlayerTurning(ClientID, "clockwise"));
            }
    }}}

    public void move1Action(int playerNum) throws IOException {
        boolean restart=false;
        int ResPosX=playerMat.getResPosXY().get(0);
        int ResPosY=playerMat.getResPosXY().get(1);
        String ResOrient=playerMat.getResOrient();
        int counter=TurningCounter(ResOrient,playerMat.getListOrientation().get(playerNum));
        List<Integer> points = server.playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        if (canMove(playerNum)) {
            if (plOrient.equalsIgnoreCase("right")) {
                currentPlayerPosX = currentPlayerPosX + 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    int playerLives = lives.get(playerNum);
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("left")) {
                currentPlayerPosX = currentPlayerPosX - 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("top")) {
                currentPlayerPosY = currentPlayerPosY - 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }
            if (plOrient.equalsIgnoreCase("bottom")) {
                currentPlayerPosY = currentPlayerPosY + 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
                    playerPosX.set(playerNum, ResPosX);
                    playerPosY.set(playerNum, ResPosY);
                    playerOrientation.set(playerNum, ResOrient);
                    restart=true;
                }
            }

        }
        playerMat.setposx(playerPosX);
        playerMat.setplayerPosY(playerPosY);
        playerMat.setplayerOrientation(playerOrientation);

        int ClientID=playerMat.getClientIdsList().get(playerNum);
        if(restart){
        for(int i=0;i<counter;i++){
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            System.out.println("TURNING WORKED !!!!!!");
                player.getServerThread().send(JsonCommands.PlayerTurning(ClientID, "clockwise"));
        }
    }
    }}
public int TurningCounter(String ResOrient,String PlOrient){
        List<String>tempList=new ArrayList<>();
        tempList.add("top");
        tempList.add("left");
        tempList.add("bottom");
        tempList.add("right");
        int ResOrientCounter=tempList.indexOf(ResOrient);
        int PlOrientCounter=tempList.indexOf(PlOrient);
        int counter=PlOrientCounter-ResOrientCounter;
        if(counter<0){
            counter=4+counter;
        }
        return  counter;
}
    public void move2Action(int playerNum) throws IOException {
        move1Action(playerNum);
        move1Action(playerNum);
    }

    public void move3Action(int playerNum) throws IOException {
        move1Action(playerNum);
        move1Action(playerNum);
        move1Action(playerNum);
    }

    public void turnRightAction(int playerNum) throws IOException {

        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String currentPlayerOrientation = playerOrientation.get(playerNum);
        if (currentPlayerOrientation.equalsIgnoreCase("right")) {
            playerOrientation.set(playerNum, "bottom");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("left")) {
            playerOrientation.set(playerNum, "top");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("top")) {
            playerOrientation.set(playerNum, "right");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("bottom")) {
            playerOrientation.set(playerNum, "left");
        }
        playerMat.setplayerOrientation(playerOrientation);

    }

    public void turnUAction(int playerNum) throws IOException {

        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String currentPlayerOrientation = playerOrientation.get(playerNum);
        if (currentPlayerOrientation.equalsIgnoreCase("right")) {
            playerOrientation.set(playerNum, "left");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("left")) {
            playerOrientation.set(playerNum, "right");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("top")) {
            playerOrientation.set(playerNum, "bottom");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("bottom")) {
            playerOrientation.set(playerNum, "top");
        }
        playerMat.setplayerOrientation(playerOrientation);
    }

    public void turnLeftAction(int playerNum) throws IOException {

        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String currentPlayerOrientation = playerOrientation.get(playerNum);
        if (currentPlayerOrientation.equalsIgnoreCase("right")) {
            playerOrientation.set(playerNum, "top");
        }
        if (currentPlayerOrientation.equalsIgnoreCase("left")) {
            playerOrientation.set(playerNum, "bottom");
        }

        if (currentPlayerOrientation.equalsIgnoreCase("top")) {
            playerOrientation.set(playerNum, "left");
        }

        if (currentPlayerOrientation.equalsIgnoreCase("bottom")) {
            playerOrientation.set(playerNum, "right");
        }
        playerMat.setplayerOrientation(playerOrientation);
    }


    public boolean canMove(int playerNum) throws IOException {
        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        boolean canMove = true;
        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        String contentMap = null;
        contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
        JSONObject jsonMes = new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
        System.out.println("CURRENT POS X: " + currentPlayerPosX + " CURRENT POS Y: " + currentPlayerPosY);
        JSONArray jsonArray = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tempJsonObject = jsonArray.getJSONObject(i);
            String currentFieldType = tempJsonObject.getString("type");
            if (currentFieldType.equals("Wall")) {
                JSONArray orientArray = tempJsonObject.getJSONArray("orientations");
                String blockOrient = orientArray.getString(0);//wall can block only 1 direction for now

                if ((blockOrient.equals(plOrient))) {
                    canMove = false;
                    return canMove;
                }
            }
            if (plOrient.equals("bottom")) {
                int newFieldPosY = currentPlayerPosY + 1;
                if (newFieldPosY < 10) {
                    JSONArray jsonArrayNewField = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(newFieldPosY);
                    for (int d = 0; d < jsonArrayNewField.length(); d++) {
                        JSONObject tempJsonObject1 = jsonArrayNewField.getJSONObject(d);
                        String currentNewFieldType = tempJsonObject1.getString("type");
                        if (currentNewFieldType.equals("Wall")) {
                            JSONArray newOrientArray = tempJsonObject1.getJSONArray("orientations");
                            String newBlockOrient = newOrientArray.getString(0);//wall can block only 1 direction for now
                            if ((newBlockOrient.equals("top"))) {
                                canMove = false;
                                return canMove;
                            }
                        }

                    }
                }
            }
            if (plOrient.equals("top")) {
                int newFieldPosY = currentPlayerPosY - 1;
                if (newFieldPosY > 0) {
                    JSONArray jsonArrayNewField = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(newFieldPosY);
                    for (int d = 0; d < jsonArrayNewField.length(); d++) {
                        JSONObject tempJsonObject1 = jsonArrayNewField.getJSONObject(d);
                        String currentNewFieldType = tempJsonObject1.getString("type");
                        if (currentNewFieldType.equals("Wall")) {
                            JSONArray newOrientArray = tempJsonObject1.getJSONArray("orientations");
                            String newBlockOrient = newOrientArray.getString(0);//wall can block only 1 direction for now
                            if ((newBlockOrient.equals("bottom"))) {
                                canMove = false;
                                return canMove;
                            }
                        }
                    }
                }
            }
            if (plOrient.equals("right")) {
                int newFieldPosX = currentPlayerPosX + 1;
                if (newFieldPosX < 13) {
                    JSONArray jsonArrayNewField = jsonArrayMap.getJSONArray(newFieldPosX).getJSONArray(currentPlayerPosY);
                    for (int d = 0; d < jsonArrayNewField.length(); d++) {
                        JSONObject tempJsonObject1 = jsonArrayNewField.getJSONObject(d);
                        String currentNewFieldType = tempJsonObject1.getString("type");
                        if (currentNewFieldType.equals("Wall")) {
                            JSONArray newOrientArray = tempJsonObject1.getJSONArray("orientations");
                            String newBlockOrient = newOrientArray.getString(0);//wall can block only 1 direction for now
                            if ((newBlockOrient.equals("left"))) {
                                canMove = false;
                                return canMove;
                            }
                        }

                    }
                }
            }
            if (plOrient.equals("left")) {
                int newFieldPosX = currentPlayerPosX - 1;
                if (newFieldPosX > 0) {
                    JSONArray jsonArrayNewField = jsonArrayMap.getJSONArray(newFieldPosX).getJSONArray(currentPlayerPosY);
                    for (int d = 0; d < jsonArrayNewField.length(); d++) {
                        JSONObject tempJsonObject1 = jsonArrayNewField.getJSONObject(d);
                        String currentNewFieldType = tempJsonObject1.getString("type");
                        if (currentNewFieldType.equals("Wall")) {
                            JSONArray newOrientArray = tempJsonObject1.getJSONArray("orientations");
                            String newBlockOrient = newOrientArray.getString(0);//wall can block only 1 direction for now
                            if ((newBlockOrient.equals("right"))) {
                                canMove = false;
                                return canMove;
                            }
                        }

                    }
                }
            }
        }
        return canMove;
    }

    public void currentFieldAction(int register) throws IOException {
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                System.out.println("CHECK IF ITWORKS 2 TIMESS11111111111111111!!!!");
            }}
        server.setMessageDistributor(messageDistributor);
        for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
            if (player.getServerThread() != null) {
                System.out.println("CHECK IF ITWORKS 2 TIMESS222222222222222222!!!!");
            }}
        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String contentMap = null;
        for (int b = 0; b < playerPosX.size(); b++) {
            String plOrient = playerOrientation.get(b);
            int currentPlayerPosX = playerPosX.get(b);
            int currentPlayerPosY = playerPosY.get(b);
            contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
            JSONObject jsonMes = new JSONObject(contentMap);
            JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
            JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
            JSONArray jsonArray = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                String currentFieldType = tempJsonObject.getString("type");
                System.out.println("THIS IS FIELD TYPE: " + currentFieldType);
                if (currentFieldType.equals("PushPanel")) {
                    pushPanel.setObj(playerMat);
                    pushPanel.setMessageDistributor(messageDistributor);
                    pushPanel.pushPanelAction(b, register, playerfile, RSPosx, RSPosx, RSOrient);
                }
                if (currentFieldType.equals("Pit")) {
                    pit.setObj(playerMat);
                    pit.setMessageDistributor(messageDistributor);
                    pit.pitAction(b);
                }
                if (currentFieldType.equals("Gear")) {
                    gear.setObj(playerMat);
                    gear.setMessageDistributor(messageDistributor);
                    gear.gearAction(b, playerfile);
                }
                if (currentFieldType.equals("Laser")) {
                    wall.setObj(playerMat);
                    wall.setMessageDistributor(messageDistributor);
                    wall.wallAction(b, playerfile);
                }
                if (currentFieldType.equals("ConveyorBelt")) {
                    conveyorBelt.setObj(playerMat);
                    conveyorBelt.setMessageDistributor(messageDistributor);
                    conveyorBelt.convBeltAction(b, playerfile, RSPosx, RSPosx, RSOrient);
                }
                if (currentFieldType.equals("EnergySpace")) {
                    energySpaces.setObj(playerMat);
                    energySpaces.setMessageDistributor(messageDistributor);
                    energySpaces.energyCubeFieldAction(b, playerfile);
                }
                if (currentFieldType.equals("CheckPoint")) {
                    checkpoint.setObj(playerMat);
                    checkpoint.checkPointAction(b);
                }
            }
        }
        server.SetObjServ(playerMat);
    }

    public void PowerUp(int playerNum) throws IOException {

        List<Integer> ClientIds = playerMat.getClientIdsList();
        List<Integer> energyCubes = playerMat.getListCubes();

        int encub = energyCubes.get(playerNum);
        energyCubes.set(playerNum, encub + 1);

        playerMat.setenergyCubes(energyCubes);
        int clId = ClientIds.get(playerNum);
        server.sendEnergy(clId, 1, "PowerUp card");
    }

    public void Spam(int playerNum) throws IOException {

        List<Integer> ClientIds = playerMat.getClientIdsList();
        List<Integer> lives = playerMat.getListLives();

        int lv = lives.get(playerNum);
        lives.set(playerNum, lv - 1);
        playerMat.setLives(lives);
        int clId = ClientIds.get(playerNum);
        server.sendPickDamage(clId, 1);
    }

    public void handleMove(int PlayerNum, String card, List<Integer> clIdList) throws IOException {
server.setMessageDistributor(messageDistributor);
        List<Integer> ClientIdList = clIdList;
        int ClientId = ClientIdList.get(PlayerNum);
        if (card.equalsIgnoreCase("Spam")) {
            Spam(PlayerNum);
            server.SetObjServ(playerMat);

        }
        if (card.equalsIgnoreCase("PowerUp")) {
            PowerUp(PlayerNum);
            server.SetObjServ(playerMat);

        }
        if (card.equalsIgnoreCase("BackUp")) {
            moveBack(PlayerNum);
            server.SetObjServ(playerMat);
            int PosX = playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(ClientId));
            int PosY = playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(ClientId));
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null) {
                    player.getServerThread().send(JsonCommands.Movement(ClientId, PosX, PosY));
                }
            }
        }
        if (card.equalsIgnoreCase("MoveI")) {
            move1Action(PlayerNum);
            server.SetObjServ(playerMat);
            int PosX = playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(ClientId));
            int PosY = playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(ClientId));
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null) {
                    player.getServerThread().send(JsonCommands.Movement(ClientId, PosX, PosY));
                }
            }
        }
        if (card.equalsIgnoreCase("MoveII")) {
            move2Action(PlayerNum);
            server.SetObjServ(playerMat);
            int PosX = playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(ClientId));
            int PosY = playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(ClientId));
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null) {
                    player.getServerThread().send(JsonCommands.Movement(ClientId, PosX, PosY));
                }
            }

        }
        if (card.equalsIgnoreCase("MoveIII")) {
            move3Action(PlayerNum);
            server.SetObjServ(playerMat);
            int PosX = playerMat.getListPosx().get(playerMat.getClientIdsList().indexOf(ClientId));
            int PosY = playerMat.getListPosy().get(playerMat.getClientIdsList().indexOf(ClientId));
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null) {
                    player.getServerThread().send(JsonCommands.Movement(ClientId, PosX, PosY));
                }
            }

        }
        if (card.equalsIgnoreCase("TurnRight")) {

            turnRightAction(PlayerNum);
            server.SetObjServ(playerMat);
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null && player.isReady())
                    player.getServerThread().send(JsonCommands.PlayerTurning(ClientId, "clockwise"));
            }

        }
        if (card.equalsIgnoreCase("TurnLeft")) {

            turnLeftAction(PlayerNum);
            server.SetObjServ(playerMat);
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null && player.isReady())
                    player.getServerThread().send(JsonCommands.PlayerTurning(ClientId, "counterclockwise"));
            }

        }
        if (card.equalsIgnoreCase("UTurn")) {
            turnUAction(PlayerNum);
            server.SetObjServ(playerMat);
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                if (player.getServerThread() != null && player.isReady())
                    player.getServerThread().send(JsonCommands.PlayerTurning(ClientId, "clockwise"));
                    player.getServerThread().send(JsonCommands.PlayerTurning(ClientId, "clockwise"));
            }

        }

    }

    public void playerShooting() {

        int tempPosx = 0;
        int tempPosy = 0;
        List<Integer> playerNum = playerMat.getPlayerNum();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<String> playerOrientation = playerMat.getListOrientation();
        System.out.println(playerNum);
        System.out.println(playerPosX);
        for (int i = 0; i < playerPosX.size(); i++) {
            System.out.println("TEST2");
            if (playerOrientation.get(i).equalsIgnoreCase("bottom")) {
                tempPosx = playerPosX.get(i);
                tempPosy = playerPosY.get(i);
                for (int b = 9 - playerPosY.get(i); b > 0; b--) {
                    tempPosy = tempPosy + 1;
                    for (int c = 0; c < playerPosX.size(); c++) {
                        if (playerPosX.get(c) == tempPosx && playerPosY.get(c) == tempPosy) {
                            server.LaserShot(playerNum.get(i), playerNum.get(c));
                        }
                    }
                }
            }
            if (playerOrientation.get(i).equalsIgnoreCase("top")) {
                tempPosx = playerPosX.get(i);
                tempPosy = playerPosY.get(i);
                for (int b = 9 - playerPosY.get(i); b > 0; b--) {
                    tempPosy = tempPosy - 1;
                    for (int c = 0; c < playerPosX.size(); c++) {
                        if (playerPosX.get(c) == tempPosx && playerPosY.get(c) == tempPosy) {
                            server.LaserShot(playerNum.get(i), playerNum.get(c));
                        }
                    }
                }
            }
            if (playerOrientation.get(i).equalsIgnoreCase("right")) {
                tempPosx = playerPosX.get(i);
                tempPosy = playerPosY.get(i);
                for (int b = 12 - playerPosX.get(i); b > 0; b--) {
                    tempPosx = tempPosx + 1;
                    for (int c = 0; c < playerPosY.size(); c++) {
                        if (playerPosX.get(c) == tempPosx && playerPosY.get(c) == tempPosy) {
                            server.LaserShot(playerNum.get(i), playerNum.get(c));
                        }
                    }
                }
            }
            if (playerOrientation.get(i).equalsIgnoreCase("bottom")) {
                tempPosx = playerPosX.get(i);
                tempPosy = playerPosY.get(i);
                for (int b = 12 - playerPosY.get(i); b > 0; b--) {
                    tempPosx = tempPosx - 1;
                    for (int c = 0; c < playerPosX.size(); c++) {
                        if (playerPosX.get(c) == tempPosx && playerPosY.get(c) == tempPosy) {
                            server.LaserShot(playerNum.get(i), playerNum.get(c));
                        }
                    }
                }
            }

        }
    }

    public void checkpointCheck(Integer checkPointNum) {

        for (int i = 0; i < playerMat.getListPosx().size(); i++) {
            if (playerMat.getListPoints().get(i) == checkPointNum) {
                server.sendGameFinished(i);
            }
        }
    }
}
