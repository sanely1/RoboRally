package game;

import org.json.JSONArray;
import org.json.JSONObject;
import server.MessageDistributor;
import server.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Gear {
    private PlayerMat playerMat;
    public MessageDistributor messageDistributor;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }

    public Server server=new Server();
    public void setObj(PlayerMat pl){
        this.playerMat=pl;
    }

    public void gearAction(int playerNum,File playerfile) throws IOException {

        List<Integer> ClientIds= playerMat.getClientIdsList();
        int clId=ClientIds.get(playerNum);
        List<Integer> points = playerMat.getListPoints();
        List<Integer>  playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String plOrient=playerOrientation.get(playerNum);
        int currentPlayerPosX=playerPosX.get(playerNum);
        int currentPlayerPosY=playerPosY.get(playerNum);
        String contentMap=null;
        contentMap= new String(Files.readAllBytes(Paths.get(playerfile.toURI())),"UTF-8");
        JSONObject jsonMes= new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
        JSONArray jsonArray= jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
        for(int i=0; i<jsonArray.length();i++){
            JSONObject tempJsonObject= jsonArray.getJSONObject(i);
            String  currentFieldType=tempJsonObject.getString("type");
            if(currentFieldType.equalsIgnoreCase("Gear")){
                String orient = tempJsonObject.getJSONArray("orientations").getString(0);
                if (orient.equalsIgnoreCase("clockwise")) {
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
                    server.sendRotation(ClientIds.get(playerNum), "clockwise");
                }

                if (orient.equalsIgnoreCase("counterclockwise")) {
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
                    server.setMessageDistributor(messageDistributor);
                    int clID=playerMat.getClientIdsList().get(playerNum);
                    server.SetObjServ(playerMat);
                    server.sendRotation(ClientIds.get(playerNum), "counterclockwise");
                }

            }


        }
    }
}