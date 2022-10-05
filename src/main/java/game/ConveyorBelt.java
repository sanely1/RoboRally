package game;

import model.JsonCommands;
import org.json.JSONArray;
import org.json.JSONObject;
import server.MessageDistributor;
import server.PlayerInformation;
import server.Server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConveyorBelt {
    private PlayerMat playerMat;
    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }
    public MessageDistributor messageDistributor;
public Server server=new Server();
    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }

    public void convBeltAction(int playerNum, File playerfile, int RSPosX, int RSPosY, String RSOrient) throws IOException {

        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        boolean cubeLeft = true;
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        String contentMap = null;
        contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
        JSONObject jsonMes = new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
        int posx = playerPosX.get(playerNum);
        int posy = playerPosY.get(playerNum);
        System.out.println("THIS is pos of player before we move him with conv belt X: " + posy + " Y: " + posy);
        JSONArray jsonArray = jsonArrayMap.getJSONArray(posx).getJSONArray(posy);
        for (int v = 0; v < jsonArray.length(); v++) {
            JSONObject tempJsonObject = jsonArray.getJSONObject(v);
            String currentFieldType = tempJsonObject.getString("type");
            if (currentFieldType.equalsIgnoreCase("ConveyorBelt")) {
                JSONObject Object1 = tempJsonObject;
                int power = Object1.getInt("speed");
                JSONArray orientArray = Object1.getJSONArray("orientations");
                String[] orientations = new String[3];
                for (int b = 0; b < orientArray.length(); b++) {
                    String orient1 = orientArray.getString(b);
                    orientations[b] = orient1;
                    //  playerOrientation.set(playerNum,orient1);
                }
                int i=0;
                    if (orientations[i].equalsIgnoreCase("up")) {

                        playerPosY.set(playerNum, posy - power);
                        if ((posy - power < 0 || posy - power > 9)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (orientations[i].equalsIgnoreCase("bottom")) {
                        playerPosY.set(playerNum, posy + power);
                        if ((posy + power < 0 || posy + power > 9)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (orientations[i].equalsIgnoreCase("left")) {
                        playerPosX.set(playerNum, posx - power);
                        if ((posx - power < 0 || posx - power > 12)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (orientations[i].equalsIgnoreCase("right")) {
                        playerPosX.set(playerNum, posx + power);
                        if ((posx + power < 0 || posx + power > 12)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                }
                playerMat.setposx(playerPosX);
                playerMat.setplayerPosY(playerPosY);
                playerMat.setplayerOrientation(playerOrientation);
               int clId=playerMat.getClientIdsList().get(playerNum);
               server.SetObjServ(playerMat);
               server.setMessageDistributor(messageDistributor);
                server.sendMove(clId);
            }
        }
    }
