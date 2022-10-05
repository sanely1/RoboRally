package surface;

import client.Client;
import client.ClientReaderTask;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.JsonCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class LobbyController {

    private final int TWONKY = 0;
    private final int HULK = 1;
    private final int HAMMERBOT = 2;
    private final int SMASHBOT = 3;
    private final int ZOOMBOT = 4;
    private final int SPINBOT = 5;


    private Client clientLobby;
    private boolean isReadySet = false;
    private Integer figure = -1;
    @FXML
    public Button loginBtn;
    @FXML
    private  Button readyBtn;
    @FXML
    private TextField userNameInput;
    @FXML
    private TextArea errorMsgArea;
    @FXML
    private VBox connectedPlayer;
    @FXML
    private ImageView map1;
    @FXML
    private ImageView map2;
    @FXML
    private ImageView map3;
    @FXML
    private ImageView map4;
    @FXML
    private ImageView map5;
    @FXML
    private Label map1Text, map2Text, map3Text, map4Text;
    @FXML
    public ImageView figure0, figure1, figure2, figure3, figure4, figure5;

    private List<ImageView> mapImages;
    private ClientReaderTask clientReaderTask;
    private List<String> mapNames = new ArrayList<>();
    private HashMap<Integer, HBox> connectedPlayerHBox = new HashMap<>();

    private HashMap<Integer, Circle> connectedPlayerReadyCircles = new HashMap<>();


    public LobbyController() {
    }

    //TODO add button to send maps and make it possible to choose another map

    public void initialize(Client client) {
        this.clientReaderTask = clientReaderTask;
        mapImages = Arrays.asList(map1, map2, map3, map4, map5);

        setOnMouseClicked(client.getClientReaderTask());
        clickedOnFigure(client);

        map1Text.setVisible(false);
        map2Text.setVisible(false);
        map3Text.setVisible(false);
        map4Text.setVisible(false);
        map1.setDisable(true);
        map2.setDisable(true);
        map3.setDisable(true);
        map4.setDisable(true);
        map5.setDisable(true);


        readyBtn.setDisable(true);
        userNameInput.clear();
        this.clientLobby = client;
    }

    @FXML
    public void loginBtnClicked(ActionEvent actionEvent) throws Exception {
        if (!userNameInput.getText().isEmpty() && figure >= 0) {
            String input = userNameInput.getText();
            clientLobby.sendPlayerValues(input, figure);
            loginBtn.setText("logged in");
            userNameInput.clear();
            userNameInput.setDisable(true);
        }
    }

    @FXML
    public void handleUserNameInput(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            loginBtn.fire();
            userNameInput.clear();
        }
    }


    @FXML
    public void readyBtnClicked(ActionEvent actionEvent) throws Exception {
        if (loginBtn.isDisabled()) {
            if (!isReadySet) {
                clientLobby.processViewMessage("#SetStatus");
                readyBtn.setText("Unready");
                isReadySet = true;
                readyBtn.setDisable(false);
            } else {
                clientLobby.processViewMessage("#SetStatus = false");
                readyBtn.setText("Ready");
                isReadySet = false;
                setMapsVisible(false);
                map5.setVisible(false);
            }

        }
    }


    private void clickedOnFigure(Client client) {
        if (!loginBtn.isDisabled()) {
            figure0.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    disableButtons();
                    client.setFigure(0);
                    figure = TWONKY;
                    figure0.setOpacity(0.3);
                }
            });
            figure1.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    disableButtons();
                    client.setFigure(1);
                    figure = HULK;
                    figure1.setOpacity(0.3);
                }
            });
            figure2.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {

                    disableButtons();
                    client.setFigure(2);
                    figure = HAMMERBOT;
                    figure2.setOpacity(0.3);
                }
            });
            figure3.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    disableButtons();
                    client.setFigure(3);
                    figure = SMASHBOT;
                    figure3.setOpacity(0.3);
                }
            });
            figure4.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    disableButtons();
                    client.setFigure(4);
                    figure = ZOOMBOT;
                    figure4.setOpacity(0.3);
                }
            });
            figure5.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    //
                    disableButtons();
                    client.setFigure(5);
                    figure = SPINBOT;
                    figure5.setOpacity(0.3);
                }
            });
        }
    }

    public void disableButtons() {
        figure0.setDisable(true);
        figure1.setDisable(true);
        figure2.setDisable(true);
        figure3.setDisable(true);
        figure4.setDisable(true);
        figure5.setDisable(true);
    }

    /**
     * disable function to select specific robot, adapt opacity
     * @param figure
     * @param isClientLobby
     */
    public void disableRobo(Integer figure, boolean isClientLobby) {
        switch (figure) {
            case TWONKY:
                if (!isClientLobby) {
                    figure0.setDisable(true);
                    figure0.setOpacity(0.3);
                }
                break;
            case HULK:
                if (!isClientLobby) {
                    figure1.setDisable(true);
                    figure1.setOpacity(0.3);
                }
                break;
            case HAMMERBOT:
                if (!isClientLobby) {
                    figure2.setDisable(true);
                    figure2.setOpacity(0.3);
                }
                break;
            case SMASHBOT:
                if (!isClientLobby) {
                    figure3.setDisable(true);
                    figure3.setOpacity(0.3);
                }
                break;
            case ZOOMBOT:
                if (!isClientLobby) {
                    figure4.setDisable(true);
                    figure4.setOpacity(0.3);
                }
                break;
            case SPINBOT:
                if (!isClientLobby) {
                    figure5.setDisable(true);
                    figure5.setOpacity(0.3);
                }
                break;
        }
    }

    private void setOnMouseClicked(ClientReaderTask clientReaderTask) {
        DropShadow dsBlue = new DropShadow(20, Color.TURQUOISE);
        map1.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                clientReaderTask.send(JsonCommands.MapSelected(mapNames.get(0)));
                map1.setEffect(dsBlue);
                map1.setDisable(true);
            }
        });
        map2.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                clientReaderTask.send(JsonCommands.MapSelected(mapNames.get(1)));
                map2.setEffect(dsBlue);
                map2.setDisable(true);
            }
        });
        map3.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                clientReaderTask.send(JsonCommands.MapSelected(mapNames.get(2)));
                map3.setEffect(dsBlue);
                map3.setDisable(true);
            }
        });
        map4.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                clientReaderTask.send(JsonCommands.MapSelected(mapNames.get(3)));
                map4.setEffect(dsBlue);
                map4.setDisable(true);
            }
        });

    }

    public void setMaps(List<String> maps) {
        map1.setDisable(false);
        map2.setDisable(false);
        map3.setDisable(false);
        map4.setDisable(false);
        for (int i = 0; i < maps.size(); i++) {
            String cardPath = "/" + maps.get(i) + ".jpg";
            Image card = new Image(String.valueOf(getClass().getResource(cardPath)));
            mapImages.get(i).setImage(card);
            mapNames.add(maps.get(i));
        }
    }


    public Button getLoginButton() {
        return loginBtn;
    }

    public Button getReadyButton() {
        return readyBtn;
    }

    public Client getClient() {
        return clientLobby;
    }

    public HashMap<Integer, HBox> getConnectedPlayerHBox() {
        return connectedPlayerHBox;
    }

    public HashMap<Integer, Circle> getConnectedPlayerReadyCircles() {
        return connectedPlayerReadyCircles;
    }

    public VBox getConnectedPlayer() {
        return connectedPlayer;
    }

    public Integer getFigure() {
        return figure;
    }

    public void setFigure(Integer figure) {
        this.figure = figure;
    }


    public Label getMap1Text() {
        return map1Text;
    }


    public Label getMap2Text() {
        return map2Text;
    }

    public Label getMap3Text() {
        return map3Text;
    }

    public Label getMap4Text() {
        return map4Text;
    }

    public TextArea getErrorMsgArea() {
        return errorMsgArea;
    }

    public void setErrorMsgArea(String msg) {
        errorMsgArea.appendText(msg);
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public void setMapsVisible(boolean visible) {
        for (int i = 0; i<mapImages.size()-1; i++){
            mapImages.get(i).setVisible(visible);
        }
        map1Text.setVisible(visible);
        map2Text.setVisible(visible);
        map3Text.setVisible(visible);
        map4Text.setVisible(visible);
    }
}