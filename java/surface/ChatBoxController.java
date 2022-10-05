package surface;

import client.Client;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatBoxController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageInputField;
    @FXML
    private ImageView sendButton;


    public void initialize(Client player) {
        chatArea.setWrapText(true);
        chatArea.setText("*********\nTo send a private message to a player, add \"@\" and his/her ID in front of your message. You can see the ID in the window \"Connected Players\"\n*********\n");

        sendButton.setOnMouseClicked(new EventHandler() {

            @Override
            public void handle(Event event) {
                if (messageInputField != null && player.getFigure() >= 0) {
                    String input = messageInputField.getText();
                    if (player != null && chatArea != null) {
                        player.processViewMessage(input);
                        messageInputField.clear();
                    }
                }
            }
        });
        messageInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    if (messageInputField != null && player.getFigure() >= 0 ) {
                        String input = messageInputField.getText();
                        player.processViewMessage(input);
                        messageInputField.clear();
                    }
                }
            }
        });
    }

    public TextArea getChatArea() {
        return chatArea;
    }

    public void setChatArea(String msg) {
        chatArea.appendText(msg);
    }
}
