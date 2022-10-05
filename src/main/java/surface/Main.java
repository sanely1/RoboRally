package surface;

import client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.TypeName;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import static java.lang.Thread.sleep;

public class Main extends Application {
    private TextField ipTextField,portTextField;
    private int port;
    private String ip;
    private ViewModel viewModel;
    private LobbyController lobbyController;
    LobbyWindow lobbyWindow = new LobbyWindow();
    GameWindow gameWindow = new GameWindow();
    public GameInformationBoxController gameInformationBoxController;
    public ChatBoxController chatBoxController;
    private Client client;
    private Button loginBtn = new Button("Connect");
    private Button loginAIBtn = new Button ("Connect as AI");
    private final Text actionTarget = new Text();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;
        primaryStage.setWidth(400);
        primaryStage.setTitle("Connection Window: Robo Rally");
        primaryStage.setResizable(true);

        this.viewModel = new ViewModel(primaryStage);
        Image gridImage = new Image(String.valueOf(getClass().getResource("/background.jpg")));

        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundImage(gridImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFill(Color.SNOW);
        sceneTitle.setFont(Font.font("System Bold Italic", FontWeight.NORMAL, 35));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label ipLabel = new Label("    IP:");
        ipLabel.setTextFill(Color.WHITESMOKE);
        ipLabel.setFont(Font.font("System Bold Italic", 15));
        grid.add(ipLabel, 0, 1);

        ipTextField = new TextField();
        grid.add(ipTextField, 1, 1);

        Label port = new Label("Port:");
        port.setTextFill(Color.WHITESMOKE);
        port.setFont(Font.font("System Bold Italic", 15));
        grid.add(port, 0, 2);

        portTextField = new TextField();
        grid.add(portTextField, 1, 2);

        ipTextField.setText(TypeName.ip);
        portTextField.setText(String.valueOf(TypeName.port));

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginBtn);
        grid.add(hbBtn, 1, 4);
        hbBtn.getChildren().add(loginAIBtn);

        grid.add(actionTarget, 1, 6);


        loginBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
            buttonClicked(false);
            }
        });

        loginAIBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                buttonClicked(true);
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);


        primaryStage.show();
    }


    public void establishConnection(){
        ip = ipTextField.getText();
        port = Integer.parseInt(portTextField.getText());
        TypeName.ip = ip;
        TypeName.port = port;

    }


    public void startLobby(Stage stage) throws Exception {

        lobbyWindow.start(client, stage);;

    }

    public void buttonClicked(Boolean isAI){
        loginAIBtn.setDisable(true);
        loginBtn.setDisable(true);
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setText("Connection is being set up!\nWaiting for other players.");
        establishConnection();
        try {
            client = new Client(isAI);
            client.setViewModel(viewModel);
            viewModel.setPlayer(client);
            if (!isAI){
                startLobby(this.stage);
                gameInformationBoxController = lobbyWindow.getLobbyWindowController().getGameInformationBoxController();
                chatBoxController = lobbyWindow.getLobbyWindowController().getChatBoxController();
                viewModel.setLobbyWindowController(lobbyWindow.getLobbyWindowController());
                viewModel.setGameInformationBoxController(gameInformationBoxController);
                viewModel.setChatBoxController(this.chatBoxController);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args){
        launch(args);
    }

}
