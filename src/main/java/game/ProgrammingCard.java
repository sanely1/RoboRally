package game;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProgrammingCard extends Card {
    private PlayerMat value;
    private boolean isSpecialCard;

    public ProgrammingCard(String category, String description, String name) {
        super(category, description, name);
    }

    public void cardAction() {
    }

    public void backUp() {
    }

    public void again() {
    }

    public void energyRoutineAction() {
    }

    public void sandboxRoutineAction() {
    }

    public void weaselRoutineAction() {
    }

    public void repeatRoutineAction() {
    }

    public void move1Action(int playerNum) throws IOException {

        value = new PlayerMat();//get player information
        List<Integer> points = value.getListPoints();
        List<Integer> playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        List<Integer> energyCubes = value.getListCubes();
        List<Integer> lives = value.getListLives();
        List<String> playerOrientation = value.getListOrientation();

        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        if (canMove(playerNum)) {
            if (plOrient.equalsIgnoreCase("right")) {
                currentPlayerPosX = currentPlayerPosX + 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    int playerLives = lives.get(playerNum);
                    lives.set(playerNum, playerLives - 1);
                    playerPosX.set(playerNum, 7);
                    playerPosY.set(playerNum, 3);
                    playerOrientation.set(playerNum, "bottom");
                }
            }
            if (plOrient.equalsIgnoreCase("left")) {
                currentPlayerPosX = currentPlayerPosX - 1;
                playerPosX.set(playerNum, currentPlayerPosX);
                if ((currentPlayerPosX < 0 || currentPlayerPosX > 12)) {
                    int playerLives = lives.get(playerNum);
                    lives.set(playerNum, playerLives - 1);
                    playerPosX.set(playerNum, 7);
                    playerPosY.set(playerNum, 3);
                    playerOrientation.set(playerNum, "bottom");
                }
            }
            if (plOrient.equalsIgnoreCase("top")) {
                currentPlayerPosY = currentPlayerPosY - 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 12)) {
                    int playerLives = lives.get(playerNum);
                    lives.set(playerNum, playerLives - 1);
                    playerPosX.set(playerNum, 7);
                    playerPosY.set(playerNum, 3);
                    playerOrientation.set(playerNum, "bottom");
                }
            }
            if (plOrient.equalsIgnoreCase("bottom")) {
                currentPlayerPosY = currentPlayerPosY + 1;
                playerPosY.set(playerNum, currentPlayerPosY);
                if ((currentPlayerPosY < 0 || currentPlayerPosY > 12)) {
                    int playerLives = lives.get(playerNum);
                    lives.set(playerNum, playerLives - 1);
                    playerPosX.set(playerNum, 7);
                    playerPosY.set(playerNum, 3);
                    playerOrientation.set(playerNum, "bottom");
                }
            }

        }
        value.setposx(playerPosX);
        value.setplayerPosY(playerPosY);
        value.setplayerOrientation(playerOrientation);
        value.setLives(lives);
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
        value = new PlayerMat();//get player information
        List<Integer> points = value.getListPoints();
        List<Integer> playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        List<Integer> energyCubes = value.getListCubes();
        List<Integer> lives = value.getListLives();
        List<String> playerOrientation = value.getListOrientation();
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
        value.setplayerOrientation(playerOrientation);

    }

    public void turnUAction(int playerNum) throws IOException {
        value = new PlayerMat();//get player information
        List<Integer> points = value.getListPoints();
        List<Integer> playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        List<Integer> energyCubes = value.getListCubes();
        List<Integer> lives = value.getListLives();
        List<String> playerOrientation = value.getListOrientation();
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
        value.setplayerOrientation(playerOrientation);
    }

    /*
    public List<Integer>  whoIsNext(){
            int iterator=0;
            int posxAn=0;
            int posyAn=4;
            int minNum=1000;

        List<Integer>  playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        int numOfPlayer=value.getListPosx().size();
        int[] val=new int[numOfPlayer];
        int[][]playerData= new int[numOfPlayer][1];
        List<Integer> nextPlayer=new ArrayList<>();
               for(int i=value.getListPosx().size();i>0;i--)
               {
    val[iterator]=(Math.abs(posyAn-playerPosY.get(iterator))+playerPosX.get(iterator)-posxAn);
    iterator++;

               }
    iterator=0;
               for(int b=val.length;b>0;b--){
                   if(val[iterator]<minNum){
                       minNum=val[iterator];
                       nextPlayer.add(iterator);
                   }
                   iterator++;
               }
               return nextPlayer;
                      }
    */
    public void turnLeftAction(int playerNum) throws IOException {
        value = new PlayerMat();//get player information
        List<Integer> points = value.getListPoints();
        List<Integer> playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        List<Integer> energyCubes = value.getListCubes();
        List<Integer> lives = value.getListLives();
        List<String> playerOrientation = value.getListOrientation();
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
        value.setplayerOrientation(playerOrientation);
    }


    public boolean canMove(int playerNum) throws IOException {
        value = new PlayerMat();//get player information
        List<Integer> points = value.getListPoints();
        List<Integer> playerPosX = value.getListPosx();
        List<Integer> playerPosY = value.getListPosy();
        List<Integer> energyCubes = value.getListCubes();
        List<Integer> lives = value.getListLives();
        List<String> playerOrientation = value.getListOrientation();
        boolean canMove = true;
        String plOrient = playerOrientation.get(playerNum);
        int currentPlayerPosX = playerPosX.get(playerNum);
        int currentPlayerPosY = playerPosY.get(playerNum);
        File playerfile = new File("E:\\jstestt\\ddz.json");
        String contentMap = null;
        contentMap = new String(Files.readAllBytes(Paths.get(playerfile.toURI())), "UTF-8");
        JSONObject jsonMes = new JSONObject(contentMap);
        JSONObject jsonObjMap = jsonMes.getJSONObject("messageBody");
        JSONArray jsonArrayMap = jsonObjMap.getJSONArray("gameMap");
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
            if (plOrient.equals("top")) {
                int newFieldPosY = currentPlayerPosY - 1;
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
            if (plOrient.equals("right")) {
                int newFieldPosX = currentPlayerPosX + 1;
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
            if (plOrient.equals("left")) {
                int newFieldPosX = currentPlayerPosX - 1;
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
        return canMove;
    }
}
