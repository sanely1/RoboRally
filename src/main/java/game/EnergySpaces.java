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

public class EnergySpaces {
    public MessageDistributor messageDistributor;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }
    Server server = new Server();
    private int numberCubes;
    private PlayerMat playerMat;

    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }

    public void energyCubeFieldAction(int playerNum, File playerfile) throws IOException {

        List<Integer> ClientIds = playerMat.getClientIdsList();
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
        JSONArray jsonArray = jsonArrayMap.getJSONArray(currentPlayerPosX).getJSONArray(currentPlayerPosY);
        JSONObject tempJsonObject = jsonArray.getJSONObject(0);
        int cubeCountField = tempJsonObject.getInt("count");
        if (cubeCountField == 1) {
            int encub = energyCubes.get(playerNum);
            energyCubes.set(playerNum, encub + 1);
            tempJsonObject.put("count", 0);
            // FileWriter fw = new FileWriter(playerfile);
            // fw.write(jsonMes.toString());
            //fw.flush();
            //fw.close();
        }
        playerMat.setenergyCubes(energyCubes);
        int clId=playerMat.getClientIdsList().get(playerNum);
        server.SetObjServ(playerMat);
        server.setMessageDistributor(messageDistributor);
        server.sendEnergy(clId, cubeCountField, "EnergySpace");
    }
}