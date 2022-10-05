package game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import server.MessageDistributor;
import server.Server;
import game.PlayerMat;

public class PushPanel {
    private PlayerMat playerMat;
    public MessageDistributor messageDistributor;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }
public Server server=new Server();
    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }

    //Server server=new Server();
    public void pushPanelAction(int playerNum, int playerRegister, File playerfile, int RSPosX, int RSPosY, String RSOrient) throws IOException {
        System.out.println("THIS IS CLIENT REGISTER: "+ playerRegister);
        List<Integer> ClientIds = playerMat.getClientIdsList();
        int clId = ClientIds.get(playerNum);
        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        String contentMap = null;
        contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
        JSONObject jsonMes = new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
        JSONArray jsonArray = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
        JSONObject tempJsonObject = jsonArray.getJSONObject(0);
        String currentFieldType = tempJsonObject.getString("type");
        System.out.println("THIS IS TYPE OF THE FIELD: " + currentFieldType);
        JSONArray array1 = tempJsonObject.getJSONArray("orientations");
        JSONArray array2 = tempJsonObject.getJSONArray("registers");
        String plOrient =array1.getString(0);
        if (currentFieldType.equals("PushPanel")) {
            for (int c = 0; c < array2.length(); c++) {
                if (array2.getInt(c) == playerRegister) {
                    if (plOrient.equalsIgnoreCase("left")) {
                        currentPlayerPosX = currentPlayerPosX - 1;
                        playerPosX.set(playerNum, currentPlayerPosX);
                        if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (plOrient.equalsIgnoreCase("right")) {
                        currentPlayerPosX = currentPlayerPosX + 1;
                        playerPosX.set(playerNum, currentPlayerPosX);
                        if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                            int playerLives = lives.get(playerNum);
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (plOrient.equalsIgnoreCase("bottom")) {
                        currentPlayerPosY = currentPlayerPosY + 1;
                        playerPosY.set(playerNum, currentPlayerPosY);
                        if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
                            int playerLives = lives.get(playerNum);
                            ;
                            playerPosX.set(playerNum, RSPosX);
                            playerPosY.set(playerNum, RSPosY);
                            playerOrientation.set(playerNum, RSOrient);
                        }
                    }
                    if (plOrient.equalsIgnoreCase("top")) {
                        currentPlayerPosY = currentPlayerPosY - 1;
                        playerPosY.set(playerNum, currentPlayerPosY);
                        if ((currentPlayerPosY < 0 || currentPlayerPosY > 9)) {
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
                int clIdd=playerMat.getClientIdsList().get(playerNum);
                server.SetObjServ(playerMat);
                server.setMessageDistributor(messageDistributor);
                server.sendMove(clIdd);

            }

        }

    }
}


