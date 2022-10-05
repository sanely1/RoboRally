package game;

import model.JsonCommands;
import server.MessageDistributor;
import server.PlayerInformation;
import server.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pit {
    private PlayerMat playerMat;
public Server server=new Server();
    public void setObj(PlayerMat pl) {
        this.playerMat = pl;
    }
    public MessageDistributor messageDistributor;

    public void setMessageDistributor(MessageDistributor messageDistributor) {
        this.messageDistributor = messageDistributor;
    }


    public void pitAction(int playerNum) throws IOException {

        List<Integer> points = playerMat.getListPoints();
        List<Integer> playerPosX = playerMat.getListPosx();
        List<Integer> playerPosY = playerMat.getListPosy();
        List<Integer> energyCubes = playerMat.getListCubes();
        List<Integer> lives = playerMat.getListLives();
        List<String> playerOrientation = playerMat.getListOrientation();
        int playerLives = lives.get(playerNum);
        //give playerdeck a spam card

        int ResPosX=playerMat.getResPosXY().get(0);
        int ResPosY=playerMat.getResPosXY().get(1);
        String ResOrient=playerMat.getResOrient();

        playerPosX.set(playerNum,ResPosX);
        playerPosY.set(playerNum,ResPosY);
        int counter=TurningCounter(ResOrient,playerMat.getListOrientation().get(playerNum));
        playerOrientation.set(playerNum,ResOrient);
        playerMat.setposx(playerPosX);
        playerMat.setplayerPosY(playerPosY);
        playerMat.setplayerOrientation(playerOrientation);
        int clId=playerMat.getClientIdsList().get(playerNum);
        server.SetObjServ(playerMat);
        server.setMessageDistributor(messageDistributor);
        for(int i=0;i<counter;i++){
            for (PlayerInformation player : messageDistributor.getPlayerInformationList()) {
                System.out.println("TURNING WORKED !!!!!!");
                server.sendRotation(clId,"clockwise");
            }
        }

        server.sendMove(clId);
    }
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
}
