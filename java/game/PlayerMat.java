package game;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerMat {
    private final int energyCubes = 5;
    private final Deck programmingDeck;
    private Card[] register;
    private final ArrayList<Card> discardPile;
    private final int checkPointTokens;
    private final ArrayList<Card> temporaryUpgrades;
    private final ArrayList<Card> permanentUpgrades;

    public PlayerMat() {
        this.programmingDeck = new Deck();
        this.register = new Card[5];
        this.discardPile = new ArrayList<>();
        this.checkPointTokens = 0;
        this.temporaryUpgrades = new ArrayList<>();
        this.permanentUpgrades = new ArrayList<>();
    }

    public void setRegister(Card[] register) {
        this.register = register;
    }

    public void clearRegister() {
        Arrays.fill(register, null);
    }
    List<Integer> clientIdsList = new ArrayList<Integer>();
    List<Integer> points = new ArrayList<Integer>();
    List<Integer> playerPosX = new ArrayList<Integer>();
    List<Integer> playerNum = new ArrayList<Integer>();
    List<Integer> playerPosY = new ArrayList<Integer>();
    List<Integer> energyCubes1 = new ArrayList<Integer>();
    List<Integer> lives = new ArrayList<Integer>();
    List<Integer> registerTaken=new ArrayList<>();
    List<String> playerOrientation = new ArrayList<String>();
    List<String> PreviousCard=new ArrayList<>();
    String ResOrient="";
    List<Integer> ResPosXY=new ArrayList<>();

    public List<Integer> getListPoints() {
        return points;
    }
    public List<Integer> getResPosXY() {
        return ResPosXY;
    }
    public String getResOrient(){return  ResOrient;}
    public List<String> getPreviousCard() {
        return PreviousCard;
    }
    public List<Integer> getClientIdsList() {
        return clientIdsList;
    }


    public List<Integer> getPlayerNum() {
        return playerNum;
    }

    public List<Integer> getRegisterTaken() {
        return registerTaken;
    }

    public List<Integer> getListPosx() {
        return playerPosX;
    }

    public List<Integer> getListPosy() {
        return playerPosY;
    }

    public List<Integer> getListCubes() {
        return energyCubes1;
    }

    public List<Integer> getListLives() {
        return lives;
    }

    public List<String> getListOrientation() {
        return playerOrientation;
    }


    public void setPreviousCard(List<String> PreviousCard) {
        this.PreviousCard = PreviousCard;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public void setResPosXY(List<Integer> ResPosXY) {
        this.ResPosXY = ResPosXY;
    }
    public void setResOrient(String ResOrient) {
        this.ResOrient = ResOrient;
    }

    public void setLives(List<Integer> lives) {
        this.lives = lives;
    }

    public void setposx(List<Integer> playerPosX) {
        this.playerPosX = playerPosX;
    }

    public void setplayerPosY(List<Integer> playerPosY) {
        this.playerPosY = playerPosY;
    }

    public void setenergyCubes(List<Integer> energyCubes1) {
        this.energyCubes1 = energyCubes1;
    }

    public void setplayerOrientation(List<String> playerOrientation) {
        this.playerOrientation = playerOrientation;
    }

    public void setPlayerNum(List<Integer> playerNum) {
        this.playerNum = playerNum;
    }

    public void  setClientIdsList(List<Integer> clientIdsList) {
        this.clientIdsList=clientIdsList;
    }

}
