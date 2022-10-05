package game;

import org.json.JSONArray;
import org.json.JSONObject;
import server.MessageDistributor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Wall {
    private PlayerMat playerMat;

    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }
    public MessageDistributor messageDistributor;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }
    //Server server=new Server();
    public void wallAction(int playerNum, File playerfile) throws IOException {
        List<Integer> ClientIds = playerMat.getClientIdsList();
        int clId = ClientIds.get(playerNum);
        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        String contentMap = null;
        contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
        JSONObject jsonMes = new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
        JSONArray jsonArray = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tempJsonObject = jsonArray.getJSONObject(i);
            String currentFieldType = tempJsonObject.getString("type");

            if (currentFieldType.equals("Laser")) {
                JSONArray array1 = tempJsonObject.getJSONArray("orientations");
                String laserOrientation = array1.getString(0);
                int laserPower = tempJsonObject.getInt("count");
                int pllives = lives.get(playerNum);
                lives.set(playerNum, pllives - laserPower);
                // server.sendPickDamage(clId,laserPower);
            }
        }
        playerMat.setLives(lives);
    }
}
