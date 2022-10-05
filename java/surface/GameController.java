package surface;

import client.ClientReaderTask;
import client.Teammate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.JsonCommands;

import java.util.ArrayList;
import java.util.List;


public class GameController {
    @FXML
    private ImageView map;
    @FXML
    private ImageView register1;
    @FXML
    private ImageView register2;
    @FXML
    private ImageView register3;
    @FXML
    private ImageView register4;
    @FXML
    private ImageView register5;
    @FXML
    private TextField chatField;
    @FXML
    private ImageView startBoard;

    @FXML
    public GridPane grid;
    @FXML
    private TextArea connectedPlayersTextArea;
    @FXML
    private Button playButton;
    @FXML
    private Label playCardText;
    private int nClicks = 0;
    private ArrayList<String> cardNames = new ArrayList<String>();
    @FXML
    private ImageView gameFinishedImage;


    public GridPane getGrid() {
        return grid;
    }


    public ImageView getRegister1() {
        return register1;
    }

    public ImageView getRegister2() {
        return register2;
    }

    public ImageView getRegister3() {
        return register3;
    }

    public ImageView getRegister4() {
        return register4;
    }

    public ImageView getRegister5() {
        return register5;
    }

    public void addCardName(String cardName) {
        this.cardNames.add(cardName);
    }

    public void clearCardName() {
        cardNames.clear();
    }

    public void resetNClicks() {
        nClicks = 0;
    }


    public void initialize(JsonArray theMap, ClientReaderTask clientReaderTask, List<Teammate> teammates) {
        playButton.setDisable(true);
        showMap(theMap, clientReaderTask);
        Platform.runLater(() -> {
            for (Teammate teammate : teammates) {
                connectedPlayersTextArea.appendText(teammate.getClientID()+ " "+ teammate.getName()+ "\n");
            }
        });

        clickOnPlayButton(clientReaderTask);
    }

    private void clickOnPlayButton(ClientReaderTask clientReaderTask) {
        playButton.setOnAction(event -> {
            String cardToPlay = cardNames.get(nClicks);
            clientReaderTask.send(JsonCommands.PlayCard(cardToPlay));
            playButton.setDisable(true);
            if (nClicks >= 4) {
                nClicks = 0;
            } else {
                nClicks++;
            }
        });
    }


    /**
     * Method to display the game map.
     *
     * @param theMap JsonArray that contains the map. Will be received by the client from the server with gameStarted-JSON-message.
     * @author Florian
     */
    @FXML
    public void showMap(JsonArray theMap, ClientReaderTask clientReaderTask) {

        for (int x = 0; x < theMap.size(); x++) {
            JsonArray tempJsonArray_row = theMap.get(x).getAsJsonArray();

            for (int y = 0; y < tempJsonArray_row.size(); y++) {

                JsonArray tempJsonArray_row_column = tempJsonArray_row.get(y).getAsJsonArray();

                //for (int i = 0; i < tempJsonArray_row_column.size(); i++) {
                for (int i = tempJsonArray_row_column.size() - 1; i >= 0; i--) {

                    Object map_field = tempJsonArray_row_column.get(i);
                    String map_field_string = map_field.toString();


                    if (!map_field_string.equals("null")) {

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(map_field_string, JsonElement.class);
                        JsonObject map_field_json = element.getAsJsonObject();


                        if (map_field_json.get("type").toString().equals("\"Empty\"")) {

                            if (tempJsonArray_row_column.size() == 1) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/nullField.png", grid);
                            }
                        }


                        if (map_field_json.get("type").toString().equals("\"StartPoint\"")) {

                            addSpawnButton(x, y, clientReaderTask);

                        }


                        if (map_field_json.get("type").toString().equals("\"ConveyorBelt\"")) {

                            JsonElement orientation_jsonelement = map_field_json.get("orientations");
                            JsonArray orientation_array = orientation_jsonelement.getAsJsonArray();
                            String orientation_first = orientation_array.get(0).toString();

                            //conveyor belts that go straight to one direction
                            if (orientation_first.equals("\"top\"")) {

                                displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed",  "/Conv_one_top.png",  "/Conv_two_top.png");

                            }

                            if (orientation_first.equals("\"bottom\"")) {

                                displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_bottom.png", "/Conv_two_bottom.png");

                            }

                            if (orientation_first.equals("\"left\"")) {

                                displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_left.png", "/Conv_two_left.png");

                            }

                            if (orientation_first.equals("\"right\"")) {

                                displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_right.png", "/Conv_two_right.png");

                            }

                            //conveyor belts that contain corners

                            if (orientation_array.toString().contains("bottom") && orientation_array.toString().contains("right")) {

                                if (orientation_first.equals("\"bottom\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_bottom_right.png", "/Conv_bottom_right.png");

                                }

                                if (orientation_first.equals("\"right\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_right_bottom.png", "/Conv_right_bottom.png");
                                }

                            }

                            if (orientation_array.toString().contains("top") && orientation_array.toString().contains("right")) {

                                if (orientation_first.equals("\"top\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_top_right.png", "/Conv_top_right.png");

                                }

                                if (orientation_first.equals("\"right\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_right_top.png", "/Conv_right_top.png");
                                }

                            }

                            if (orientation_array.toString().contains("bottom") && orientation_array.toString().contains("left")) {

                                if (orientation_first.equals("\"bottom\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_bottom_left.png", "/Conv_bottom_left.png");

                                }

                                if (orientation_first.equals("\"left\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_left_bottom.png", "/Conv_left_bottom.png");
                                }

                            }

                            if (orientation_array.toString().contains("top") && orientation_array.toString().contains("left")) {

                                if (orientation_first.equals("\"top\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_top_left.png", "/Conv_top_left.png");

                                }

                                if (orientation_first.equals("\"left\"")) {

                                    displayConvBeltConditionalOnSpeed(x, y, map_field_json, "speed", "/Conv_one_left_top.png", "/Conv_left_top.png");
                                }

                            }


                            if (orientation_array.toString().contains("top") && orientation_array.toString().contains("bottom") && orientation_array.toString().contains("right")) {

                                if (orientation_first.equals("\"top\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_top_bottom_right.png", grid);
                                }

                                if (orientation_first.equals("\"bottom\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_bottom_top_right.png", grid);
                                }

                                if (orientation_first.equals("\"right\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_right_top_bottom.png", grid);
                                }
                            }

                            if (orientation_array.toString().contains("top") && orientation_array.toString().contains("bottom") && orientation_array.toString().contains("left")) {

                                if (orientation_first.equals("\"top\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_top_bottom_left.png", grid);
                                }

                                if (orientation_first.equals("\"bottom\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_bottom_top_left.png", grid);
                                }

                                if (orientation_first.equals("\"right\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_left_top_bottom.png", grid);
                                }
                            }

                            if (orientation_array.toString().contains("top") && orientation_array.toString().contains("left") && orientation_array.toString().contains("right")) {

                                if (orientation_first.equals("\"top\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_top_left_right.png", grid);
                                }

                                if (orientation_first.equals("\"left\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_left_right_top.png", grid);
                                }

                                if (orientation_first.equals("\"right\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_right_left_top.png", grid);
                                }
                            }

                            if (orientation_array.toString().contains("bottom") && orientation_array.toString().contains("left") && orientation_array.toString().contains("right")) {

                                if (orientation_first.equals("\"bottom\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_bottom_left_right.png", grid);
                                }

                                if (orientation_first.equals("\"left\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_left_right_bottom.png", grid);
                                }

                                if (orientation_first.equals("\"right\"")) {

                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/Conv_right_left_bottom.png", grid);
                                }
                            }

                        }


                        if (map_field_json.get("type").toString().equals("\"Antenna\"")) {

                            //create label containing image
                            showImageAsLabelOnGridPane(x, y, "/antenna.png", grid);


                        }


                        if (map_field_json.get("type").toString().equals("\"EnergySpace\"")) {

                            if (tempJsonArray_row_column.size() == 1) {
                                //display full size
                                showImageAsLabelOnGridPane(x, y, "/energy.png", grid);
                            } else {
                                //display small size image
                                showImageAsLabelOnGridPane(x, y, "/energy_small.png", grid);
                            }


                        }


                        if (map_field_json.get("type").toString().equals("\"PushPanel\"")) {

                            JsonElement orientationPushpanelJsonelement = map_field_json.get("orientations");
                            JsonElement registersPushpanelJsonelement = map_field_json.get("registers");

                            if (registersPushpanelJsonelement.toString().contains("1,3,5")) {
                                if (orientationPushpanelJsonelement.toString().contains("top")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_135_top.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("bottom")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_135_bottom.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("left")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_135_left.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("right")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_135_right.png", grid);
                                }
                            }

                            if (registersPushpanelJsonelement.toString().contains("2,4")) {
                                if (orientationPushpanelJsonelement.toString().contains("top")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_24_top.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("bottom")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_24_bottom.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("left")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_24_left.png", grid);
                                }
                                if (orientationPushpanelJsonelement.toString().contains("right")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/pushpanel_24_right.png", grid);
                                }
                            }

                        }


                        if (map_field_json.get("type").toString().equals("\"Pit\"")) {

                            //create label containing image
                            showImageAsLabelOnGridPane(x, y, "/pit.png", grid);

                        }


                        if (map_field_json.get("type").toString().equals("\"Gear\"")) {

                            JsonElement orientation_gear_jsonelement = map_field_json.get("orientations");
                            JsonArray orientation_array = orientation_gear_jsonelement.getAsJsonArray();
                            String orientation_gear_first = orientation_array.get(0).toString();

                            if (orientation_gear_first.equals("\"clockwise\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/gear_clockwise.png", grid);
                            }

                            if (orientation_gear_first.equals("\"counterclockwise\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/gear_counterclockwise.png", grid);
                            }

                        }


                        if (map_field_json.get("type").toString().equals("\"RestartPoint\"")) {

                            JsonElement orientationRestartJsonelement = map_field_json.get("orientations");
                            JsonArray orientationRestartArray = orientationRestartJsonelement.getAsJsonArray();
                            String orientationRestartFirst = orientationRestartArray.get(0).toString();

                            if (orientationRestartFirst.equals("\"top\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/restart_top.png", grid);
                            }

                            if (orientationRestartFirst.equals("\"bottom\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/restart_bottom.png", grid);
                            }

                            if (orientationRestartFirst.equals("\"left\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/restart_left.png", grid);
                            }

                            if (orientationRestartFirst.equals("\"right\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/restart_right.png", grid);
                            }

                        }


                        if (map_field_json.get("type").toString().equals("\"Wall\"")) {

                            JsonElement orientation_jsonelement = map_field_json.get("orientations");
                            JsonArray orientation_array = orientation_jsonelement.getAsJsonArray();
                            String orientation_first = orientation_array.get(0).toString();

                            if (orientation_first.equals("\"top\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/Wall_top.png", grid);
                            }

                            if (orientation_first.equals("\"bottom\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/Wall_bottom.png", grid);
                            }

                            if (orientation_first.equals("\"left\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/Wall_left.png", grid);
                            }

                            if (orientation_first.equals("\"right\"")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/Wall_right.png", grid);
                            }

                        }

                    }

                }

            }
        }

        //go through whole map again to add lasers
        for (int x = 0; x < theMap.size(); x++) {
            JsonArray tempJsonArray_row = theMap.get(x).getAsJsonArray();

            for (int y = 0; y < tempJsonArray_row.size(); y++) {

                JsonArray tempJsonArray_row_column = tempJsonArray_row.get(y).getAsJsonArray();

                for (int i = 0; i < tempJsonArray_row_column.size(); i++) {

                    Object map_field = tempJsonArray_row_column.get(i);
                    String map_field_string = map_field.toString();

                    if (!map_field_string.equals("null")) {

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(map_field_string, JsonElement.class);
                        JsonObject mapFieldJson = element.getAsJsonObject();

                        if (mapFieldJson.get("type").toString().equals("\"Laser\"")) {

                            JsonElement orientationJsonElement = mapFieldJson.get("orientations");
                            JsonArray orientationArray = orientationJsonElement.getAsJsonArray();
                            String orientationFirst = orientationArray.get(0).toString();

                            String showLaser = "";

                            if (orientationFirst.equals("\"top\"")) {

                                //check where further lasers have to be added in this horizontal line above the field
                                showLaser = "yes";

                                for (int n = 1; n < tempJsonArray_row.size() && showLaser.equals("yes"); n++) {

                                    if (y - n >= 0 && y - n < tempJsonArray_row.size()) {

                                        if (!tempJsonArray_row.get(y - n).equals(null)) {
                                            //JsonArray laserCheckTempJsonArray_row = theMap.get(x-n).getAsJsonArray();
                                            JsonArray laserCheckTempJsonArray_row_column = tempJsonArray_row.get(y - n).getAsJsonArray();


                                            for (Integer f = 0; f < laserCheckTempJsonArray_row_column.size(); f++) {
                                                Object laserCheckMapField = laserCheckTempJsonArray_row_column.get(f);
                                                String laserCheckMapFieldString = laserCheckMapField.toString();
                                                JsonElement laserCheckElement = gson.fromJson(laserCheckMapFieldString, JsonElement.class);
                                                JsonObject laserCheckMapFieldJson = laserCheckElement.getAsJsonObject();
                                                JsonElement laserCheckType = laserCheckMapFieldJson.get("type");
                                                JsonElement laserCheckOrientation = laserCheckMapFieldJson.get("orientations");

                                                if (laserCheckType.equals("\"Antenna\"") || (laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("bottom"))) {

                                                    showLaser = "no";

                                                } else if ((laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("top"))) {

                                                    showLaser = "no";
                                                    showImageAsLabelOnGridPane(x, y - n, "/laser_one_vertical.png", grid);

                                                } else {

                                                    showImageAsLabelOnGridPane(x, y - n, "/laser_one_vertical.png", grid);

                                                }
                                            }
                                        }
                                    }
                                    //add start laser
                                    if (mapFieldJson.get("count").toString().equals("1")) {
                                        //create label containing image
                                        showImageAsLabelOnGridPane(x, y, "/laser_one_top_start.png", grid);
                                    }

                                    if (mapFieldJson.get("count").toString().equals("2")) {
                                        //create label containing image
                                        showImageAsLabelOnGridPane(x, y, "/laser_two_top_start.png", grid);
                                    }

                                    if (mapFieldJson.get("count").toString().equals("3")) {
                                        //create label containing image
                                        showImageAsLabelOnGridPane(x, y, "/laser_three_top_start.png", grid);
                                    }
                                }
                            }

                            if (orientationFirst.equals("\"bottom\"")) {

                                //check where further lasers have to be added in this horizontal line below the field
                                showLaser = "yes";
                                for (int n = 1; n < tempJsonArray_row.size() && showLaser.equals("yes"); n++) {

                                    if (y + n >= 0 && y + n < tempJsonArray_row.size()) {

                                        if (!tempJsonArray_row.get(Integer.sum(y, n)).equals(null)) {
                                            JsonArray laserCheckTempJsonArray_row_column = tempJsonArray_row.get(y + n).getAsJsonArray();

                                            for (Integer f = 0; f < laserCheckTempJsonArray_row_column.size(); f++) {
                                                Object laserCheckMapField = laserCheckTempJsonArray_row_column.get(f);
                                                String laserCheckMapFieldString = laserCheckMapField.toString();
                                                JsonElement laserCheckElement = gson.fromJson(laserCheckMapFieldString, JsonElement.class);
                                                JsonObject laserCheckMapFieldJson = laserCheckElement.getAsJsonObject();

                                                JsonElement laserCheckType = laserCheckMapFieldJson.get("type");
                                                JsonElement laserCheckOrientation = laserCheckMapFieldJson.get("orientations");

                                                if (laserCheckType.equals("\"Antenna\"") || (laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("top"))) {
                                                    showLaser = "no";

                                                } else if ((laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("bottom"))) {

                                                    showLaser = "no";
                                                    showImageAsLabelOnGridPane(x, y + n, "/laser_one_vertical.png", grid);


                                                } else {

                                                    showImageAsLabelOnGridPane(x, y + n, "/laser_one_vertical.png", grid);

                                                }

                                            }
                                        }
                                    }

                                }

                                //add start laser
                                if (mapFieldJson.get("count").toString().equals("1")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_one_bottom_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("2")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_two_bottom_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("3")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_three_bottom_start.png", grid);
                                }

                            }

                            if (orientationFirst.equals("\"left\"")) {

                                //check where further lasers have to be added in this vertical line left from the field
                                showLaser = "yes";
                                for (int n = 1; n < theMap.size() && showLaser.equals("yes"); n++) {

                                    if (x - n >= 0 && x - n < theMap.size()) {

                                        if (!theMap.get(x - n).equals(null)) {
                                            JsonArray laserChecktempJsonArray_row = theMap.get(x - n).getAsJsonArray();
                                            JsonArray laserCheckTempJsonArray_row_column = laserChecktempJsonArray_row.get(y).getAsJsonArray();
                                            for (Integer f = 0; f < laserCheckTempJsonArray_row_column.size(); f++) {
                                                Object laserCheckMapField = laserCheckTempJsonArray_row_column.get(f);
                                                String laserCheckMapFieldString = laserCheckMapField.toString();
                                                JsonElement laserCheckElement = gson.fromJson(laserCheckMapFieldString, JsonElement.class);
                                                JsonObject laserCheckMapFieldJson = laserCheckElement.getAsJsonObject();

                                                JsonElement laserCheckType = laserCheckMapFieldJson.get("type");
                                                JsonElement laserCheckOrientation = laserCheckMapFieldJson.get("orientations");

                                                if (laserCheckType.equals("\"Antenna\"") || (laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("right"))) {

                                                    showLaser = "no";

                                                } else if ((laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("left"))) {

                                                    showLaser = "no";
                                                    showImageAsLabelOnGridPane(x - n, y, "/laser_one_horizontal.png", grid);

                                                } else {

                                                    showImageAsLabelOnGridPane(x - n, y, "/laser_one_horizontal.png", grid);

                                                }
                                            }
                                        }
                                    }

                                }

                                //add start laser
                                if (mapFieldJson.get("count").toString().equals("1")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_one_left_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("2")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_two_left_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("3")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_three_left_start.png", grid);
                                }

                            }

                            if (orientationFirst.equals("\"right\"")) {

                                //check where further lasers have to be added in this vertical line right from the field
                                showLaser = "yes";
                                for (int n = 1; n < theMap.size() && showLaser.equals("yes"); n++) {

                                    if (x + n >= 0 && x + n < theMap.size()) {

                                        if (!theMap.get(x + n).equals(null)) {
                                            JsonArray laserChecktempJsonArray_row = theMap.get(x + n).getAsJsonArray();
                                            JsonArray laserCheckTempJsonArray_row_column = laserChecktempJsonArray_row.get(y).getAsJsonArray();
                                            for (Integer f = 0; f < laserCheckTempJsonArray_row_column.size(); f++) {
                                                Object laserCheckMapField = laserCheckTempJsonArray_row_column.get(f);
                                                String laserCheckMapFieldString = laserCheckMapField.toString();
                                                JsonElement laserCheckElement = gson.fromJson(laserCheckMapFieldString, JsonElement.class);
                                                JsonObject laserCheckMapFieldJson = laserCheckElement.getAsJsonObject();

                                                JsonElement laserCheckType = laserCheckMapFieldJson.get("type");
                                                JsonElement laserCheckOrientation = laserCheckMapFieldJson.get("orientations");

                                                if (laserCheckType.equals("\"Antenna\"") || (laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("left"))) {

                                                    showLaser = "no";

                                                } else if ((laserCheckType.toString().equals("\"Wall\"") && laserCheckOrientation.toString().contains("right"))) {

                                                    showLaser = "no";
                                                    showImageAsLabelOnGridPane(x + n, y, "/laser_one_horizontal.png", grid);

                                                } else {

                                                    showImageAsLabelOnGridPane(x + n, y, "/laser_one_horizontal.png", grid);

                                                }
                                            }
                                        }
                                    }

                                }

                                //add start laser
                                if (mapFieldJson.get("count").toString().equals("1")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_one_right_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("2")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_two_right_start.png", grid);
                                }

                                if (mapFieldJson.get("count").toString().equals("3")) {
                                    //create label containing image
                                    showImageAsLabelOnGridPane(x, y, "/laser_three_right_start.png", grid);
                                }

                            }

                        }

                    }

                }

            }
        }

        //go through whole map again to add checkpoints
        for (int x = 0; x < theMap.size(); x++) {
            JsonArray tempJsonArray_row = theMap.get(x).getAsJsonArray();

            for (int y = 0; y < tempJsonArray_row.size(); y++) {

                JsonArray tempJsonArray_row_column = tempJsonArray_row.get(y).getAsJsonArray();

                for (int i = 0; i < tempJsonArray_row_column.size(); i++) {

                    Object map_field = tempJsonArray_row_column.get(i);
                    String map_field_string = map_field.toString();

                    if (!map_field_string.equals("null")) {

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(map_field_string, JsonElement.class);
                        JsonObject mapFieldJson = element.getAsJsonObject();

                        if (mapFieldJson.get("type").toString().equals("\"CheckPoint\"")) {

                            if (mapFieldJson.get("count").toString().equals("1")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_1.png", grid);
                            }

                            if (mapFieldJson.get("count").toString().equals("2")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_2.png", grid);
                            }

                            if (mapFieldJson.get("count").toString().equals("3")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_3.png", grid);
                            }

                            if (mapFieldJson.get("count").toString().equals("4")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_4.png", grid);
                            }

                            if (mapFieldJson.get("count").toString().equals("5")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_5.png", grid);
                            }

                            if (mapFieldJson.get("count").toString().equals("6")) {
                                //create label containing image
                                showImageAsLabelOnGridPane(x, y, "/checkpoint_6.png", grid);
                            }

                        }

                    }

                }

            }
        }

    }

    /**
     * Method to show a conveyor belt given its position, speed, and given an image for each state
     *
     * @param x x-position where to show conveyor belt
     * @param y y-position where to show conveyor belt
     * @param speed speed of the conveyor belt, can be either 1 or 2
     * @param pngSpeed1 url of image that will be shown when speed is 1
     * @param pngSpeed2 url of image that will be shown when speed is 2
     * @author Florian
     */
    private void displayConvBeltConditionalOnSpeed(int x, int y, JsonObject map_field_json, String speed, String pngSpeed1, String pngSpeed2) {
        if (map_field_json.get(speed).toString().equals("1")) {
            showImageAsLabelOnGridPane(x, y, pngSpeed1, grid);
        }

        if (map_field_json.get(speed).toString().equals("2")) {
            showImageAsLabelOnGridPane(x, y, pngSpeed2, grid);
        }
    }


    /**
     * Method to show a button as spawn-field, is used by method displayMap
     *
     * @param x x-position where to show spawn-field
     * @param y y-position where to show spawn-field
     * @param clientReaderTask object of class ClientReaderTask that is used to process clicking on spawn field / spawn button
     * @author Florian
     */
    @FXML
    private void addSpawnButton(int x, int y, ClientReaderTask clientReaderTask) {

        final Button btnSpawn = new Button();
        btnSpawn.setPadding(Insets.EMPTY);
        Image image = new Image("/Spawn.png");
        btnSpawn.setGraphic(new ImageView(image));
        btnSpawn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clientReaderTask.send(JsonCommands.SetStartingPoint(x, y));

            }
        });
        grid.add(btnSpawn, x, y);
    }


    /**
     * Method to remove a player from the map / make player invisible.
     *
     * @param column x-position of the player
     * @param row    y-position of the player
     * @author Florian
     */
    public void removePlayerFromOldField(int column, int row) {

        ObservableList<Node> allChildrens = grid.getChildren();
        for (Node node : allChildrens) {
            if (node instanceof ImageView && grid.getRowIndex(node) == row && grid.getColumnIndex(node) == column) {
                node.setVisible(false);
            }
        }

    }


    /**
     * Method to show a player on the map given its figure, position and orientation.
     *
     * @param posX        x-position of the player
     * @param posY        y-position of the player
     * @param orientation current orientation of the player
     * @param figure      figure number of the player (between 0 and 5)
     * @author Florian
     */
    @FXML
    public void showPlayer(int posX, int posY, String orientation, int figure) {

        if (figure == 0) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player0_top.png", "/player0_bottom.png", "/player0_left.png", "/player0_right.png");
        }

        if (figure == 1) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player1_top.png", "/player1_bottom.png", "/player1_left.png", "/player1_right.png");
        }

        if (figure == 2) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player2_top.png", "/player2_bottom.png", "/player2_left.png", "/player2_right.png");
        }

        if (figure == 3) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player3_top.png", "/player3_bottom.png", "/player3_left.png", "/player3_right.png");
        }

        if (figure == 4) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player4_top.png", "/player4_bottom.png", "/player4_left.png", "/player4_right.png");

        }

        if (figure == 5) {
            displayPlayerConditionalOnOrientation(posX, posY, orientation, "/player5_top.png", "/player5_bottom.png", "/player5_left.png", "/player5_right.png");

        }

    }


    /**
     * Method to show a player on the map.
     *
     * @param posX        x-position of the player
     * @param posY        y-position of the player
     * @param orientation orientation of the player
     * @param imgTop      url of image that will be shown when orientation is "top"
     * @param imgBottom      url of image that will be shown when orientation is "bottom"
     * @param imgLeft      url of image that will be shown when orientation is "left"
     * @param imgRight      url of image that will be shown when orientation is "right"
     * @author Florian
     */
    private void displayPlayerConditionalOnOrientation(int posX, int posY, String orientation, String imgTop, String imgBottom, String imgLeft, String imgRight) {
        if (orientation.equals("top")) {
            showImageAsImageViewOnGridPane(posX, posY, imgTop, grid);
        }

        if (orientation.equals("bottom")) {
            showImageAsImageViewOnGridPane(posX, posY, imgBottom, grid);
        }

        if (orientation.equals("left")) {
            showImageAsImageViewOnGridPane(posX, posY, imgLeft, grid);
        }

        if (orientation.equals("right")) {
            showImageAsImageViewOnGridPane(posX, posY, imgRight, grid);
        }
    }


    /**
     * Method to show an image in a label on a given gridpane.
     *
     * @param x         x-position of gridpane
     * @param y         y-position of the gridpane
     * @param s         url of the image
     * @param gridPane  gridpane on which it will be shown
     * @author Florian
     */
    @FXML
    public void showImageAsLabelOnGridPane(int x, int y, String s, GridPane gridPane) {
        Image image = new Image(String.valueOf(getClass().getResource(s)));
        ImageView imageView = new ImageView(image);
        Label label_feld = new Label("", imageView);
        gridPane.add(label_feld, x, y);
    }

    /**
     * Method to show an image on a given gridpane.
     *
     * @param posX      x-position of gridpane
     * @param posY      y-position of the gridpane
     * @param s         url of the image
     * @param gridPane  gridpane on which it will be shown
     * @author Florian
     */
    @FXML
    private void showImageAsImageViewOnGridPane(int posX, int posY, String s, GridPane gridPane) {
        Image image = new Image(s);
        ImageView imageView = new ImageView(image);
        gridPane.add(imageView, posX, posY);
    }


    public void showLabelText(List<String> currentCard) {
        playCardText.setVisible(true);
        playCardText.setText("In order to play the card "+cardNames.get(nClicks)+", click the play button. ");
    }


    public void enablePlayButton() {
        playButton.setDisable(false);
    }


    public void setCardsYouGotNow(List<String> currentCard) {
        System.out.println("übergebene: " +currentCard.toString());
        for (int i = 0; i < currentCard.size(); i++) {
            cardNames.add(currentCard.get(i));
            System.out.println("Added card: " +currentCard.get(i));
        }
        System.out.println("Nach dem ginzufügen: " +cardNames.toString());
    }


    public void setCardImages() {
        for (int i = 0; i < cardNames.size(); i++) {
            String cardPath = "/" + cardNames.get(i) + ".jpg";
            Image card = new Image(String.valueOf(getClass().getResource(cardPath)));

            switch (i) {
                case 0:
                    register1.setImage(card);

                    break;
                case 1:
                    register2.setImage(card);
                    break;
                case 2:
                    register3.setImage(card);
                    break;
                case 3:
                    register4.setImage(card);
                    break;
                case 4:
                    register5.setImage(card);
                    break;
            }
        }
    }


    public void setCardNames(ArrayList<String> selectedCards) {
        this.cardNames = selectedCards;
    }


    public void setGameFinishedImage(boolean isWinner){
        Platform.runLater(() -> {
            if (isWinner==true) {
                gameFinishedImage.setImage(new Image(String.valueOf(getClass().getResource("/YouWon.jpg"))));
            } else {
                gameFinishedImage.setImage(new Image(String.valueOf(getClass().getResource("/game-over.jpg"))));
            }
            gameFinishedImage.toFront();
        });
    }

    public void replaceCard(int register, String card){
        cardNames.set(register, card);
    }

}