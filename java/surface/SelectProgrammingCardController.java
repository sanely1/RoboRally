package surface;

import client.Client;
import client.ClientReaderTask;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.JsonCommands;
import model.Tools;

import java.util.ArrayList;
import java.util.List;


public class SelectProgrammingCardController {

    String card1Name = "";
    String card2Name = "";
    String card3Name = "";
    String card4Name = "";
    String card5Name = "";
    String card6Name = "";
    String card7Name = "";
    String card8Name = "";
    String card9Name = "";
    boolean isSelected1 = false;
    boolean isSelected2 = false;
    boolean isSelected3 = false;
    boolean isSelected4 = false;
    boolean isSelected5 = false;
    boolean isSelected6 = false;
    boolean isSelected7 = false;
    boolean isSelected8 = false;
    boolean isSelected9 = false;
    @FXML
    private Pane pane;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card4;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card5;
    @FXML
    private ImageView card6;
    @FXML
    private ImageView card7;
    @FXML
    private ImageView card8;
    @FXML
    private ImageView card9;
    @FXML
    private GameWindowController gameWindowController;
    private ArrayList<String> selectedCards;
    private Stage stage;
    @FXML
    private Label warningTextField;


    public void initialize(List<String> progCards, ClientReaderTask clientReaderTask, Stage stage, GameWindowController gameWindowController, Client player) {
        stage.setResizable(false);
        selectedCards = new ArrayList<>();
        passCards(progCards);
        setOnMouseClicked(clientReaderTask, stage, gameWindowController, player);
    }

    private void passSelectedCards(GameController gameController) {
        for (int i = 0; i < selectedCards.size(); i++) {
            String cardPath = "/" + selectedCards.get(i) + ".jpg";
            Image card = new Image(String.valueOf(getClass().getResource(cardPath)));
            gameController.addCardName(selectedCards.get(i));


            switch (i) {
                case 0:
                    gameController.getRegister1().setImage(card);
                    break;
                case 1:
                    gameController.getRegister2().setImage(card);
                    break;
                case 2:
                    gameController.getRegister3().setImage(card);
                    break;
                case 3:
                    gameController.getRegister4().setImage(card);
                    break;
                case 4:
                    gameController.getRegister5().setImage(card);
                    break;
            }


        }
    }

    private void setOnMouseClicked(ClientReaderTask clientReaderTask, Stage stage, GameWindowController gameWindowController, Client player) {
        DropShadow dsGreen = new DropShadow(20, Color.GREEN);
        DropShadow dsNone = new DropShadow(0, Color.WHITE);
        this.stage = stage;

        card1.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected1) {
                    registerIndex = Tools.findIndex(selectedCards, card1Name) + 1;
                    selectedCards.remove(card1Name);
                    card1.setEffect(dsNone);
                    isSelected1 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card1Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card1Name);
                        registerIndex = Tools.findIndex(selectedCards, card1Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card1Name));
                        isSelected1 = true;
                        card1.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card2.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected2) {
                    registerIndex = Tools.findIndex(selectedCards, card2Name) + 1;
                    selectedCards.remove(card2Name);
                    card2.setEffect(dsNone);
                    isSelected2 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card2Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card2Name);
                        registerIndex = Tools.findIndex(selectedCards, card2Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card2Name));
                        isSelected2 = true;
                        card2.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card3.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected3) {
                    registerIndex = Tools.findIndex(selectedCards, card3Name) + 1;
                    selectedCards.remove(card3Name);
                    card3.setEffect(dsNone);
                    isSelected3 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card3Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card3Name);
                        registerIndex = Tools.findIndex(selectedCards, card3Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card3Name));
                        isSelected3 = true;
                        card3.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card4.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected4) {
                    registerIndex = Tools.findIndex(selectedCards, card4Name) + 1;
                    selectedCards.remove(card4Name);
                    card4.setEffect(dsNone);
                    isSelected4 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card4Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card4Name);
                        registerIndex = Tools.findIndex(selectedCards, card4Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card4Name));
                        isSelected4 = true;
                        card4.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card5.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected5) {
                    registerIndex = Tools.findIndex(selectedCards, card5Name) + 1;
                    selectedCards.remove(card5Name);
                    card5.setEffect(dsNone);
                    isSelected5 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card5Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card5Name);
                        registerIndex = Tools.findIndex(selectedCards, card5Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card5Name));
                        isSelected5 = true;
                        card5.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card6.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected6) {
                    registerIndex = Tools.findIndex(selectedCards, card6Name) + 1;
                    selectedCards.remove(card6Name);
                    card6.setEffect(dsNone);
                    isSelected6 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card6Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card6Name);
                        registerIndex = Tools.findIndex(selectedCards, card6Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card6Name));
                        isSelected6 = true;
                        card6.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card7.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected7) {
                    selectedCards.remove(card7Name);
                    registerIndex = Tools.findIndex(selectedCards, card7Name) + 1;
                    card7.setEffect(dsNone);
                    isSelected7 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card7Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card7Name);
                        registerIndex = Tools.findIndex(selectedCards, card7Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card7Name));
                        isSelected7 = true;
                        card7.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });


        card8.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected8) {
                    registerIndex = Tools.findIndex(selectedCards, card8Name) + 1;
                    selectedCards.remove(card8Name);
                    card8.setEffect(dsNone);
                    isSelected8 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card8Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card8Name);
                        registerIndex = Tools.findIndex(selectedCards, card8Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card8Name));
                        isSelected8 = true;
                        card8.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });

        card9.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                int registerIndex;
                if (isSelected9) {
                    registerIndex = Tools.findIndex(selectedCards, card9Name) + 1;
                    selectedCards.remove(card9Name);
                    card9.setEffect(dsNone);
                    isSelected9 = false;
                    clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, null));
                } else if (selectedCards.size() < 5) {
                    if (card9Name.equals("Again") && selectedCards.isEmpty()) {
                        warningTextField.setText("Again cannot be put into the first register.\nSelect another card.");
                    } else {
                        selectedCards.add(card9Name);
                        registerIndex = Tools.findIndex(selectedCards, card9Name) + 1;
                        clientReaderTask.send(JsonCommands.SelectedCard(registerIndex, card9Name));
                        isSelected9 = true;
                        card9.setEffect(dsGreen);
                        if (selectedCards.size() == 5) {
                            endSelection(gameWindowController.getGameController());
                        }
                    }
                }
            }
        });
    }

    public void endSelection(GameController gameController) {
        passSelectedCards(gameController);

        Platform.runLater(() -> {
            stage.close();
        });
    }

    private void passCards(List<String> progCards) {
        for (int i = 0; i < progCards.size(); i++) {
            String cardPath = "/" + progCards.get(i) + ".jpg";
            Image card = new Image(String.valueOf(getClass().getResource(cardPath)));


            switch (i) {
                case 0:
                    card1.setImage(card);
                    card1Name = progCards.get(i);
                    break;
                case 1:
                    card2.setImage(card);
                    card2Name = progCards.get(i);
                    break;
                case 2:
                    card3.setImage(card);
                    card3Name = progCards.get(i);
                    break;
                case 3:
                    card4.setImage(card);
                    card4Name = progCards.get(i);
                    break;
                case 4:
                    card5.setImage(card);
                    card5Name = progCards.get(i);
                    break;
                case 5:
                    card6.setImage(card);
                    card6Name = progCards.get(i);
                    break;
                case 6:
                    card7.setImage(card);
                    card7Name = progCards.get(i);
                    break;
                case 7:
                    card8.setImage(card);
                    card8Name = progCards.get(i);
                    break;
                case 8:
                    card9.setImage(card);
                    card9Name = progCards.get(i);
                    break;
            }
        }
    }

    public ArrayList<String> getSelectedCards() {
        return selectedCards;
    }

}
