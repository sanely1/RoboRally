
package model;


import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * This class contains the JSON commands which are sent between client and server
 */
public class JsonCommands {
    private final static Logger logger = Logger.getLogger(JsonCommands.class);

    /**
     * creates a HelloClient JSON object and sends the protocol version
     */
    public static JSONObject HelloClient() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.PROTOCOL, TypeName.SERVER_PROTOCOL);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.HELLO_CLIENT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.HelloClient() ", e);
        }
        return jsonObject;
    }


    public static JSONObject Alive() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.ALIVE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Alive() ", e);

        }
        return jsonObject;
    }


    /**
     * creates a HelloServer JSON object and sends the protocol version, the group name and the information about being an AI
     */
    public static JSONObject HelloServer(boolean isAI) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.GROUP, TypeName.GROUP_NAME);
            jsonObject1.put(TypeName.IS_AI, isAI);
            jsonObject1.put(TypeName.PROTOCOL, TypeName.CLIENT_PROTOCOL);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.HELLO_SERVER);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.HelloServer()  ", e);
        }

        return jsonObject;
    }


    /**
     * creates a Welcome JSON object and sends the clientID
     */
    public static JSONObject Welcome(Integer clientId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientId);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.WELCOME);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Welcome() ", e);
        }

        return jsonObject;
    }

    /**
     * creates a PlayerValues JSON object and sends the player name and the figure
     */
    public static JSONObject PlayerValues(String name, Integer figure) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_NAME, name);
            jsonObject1.put(TypeName.FIGURE, figure);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAYER_VALUES);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PlayerValues() ", e);
        }

        return jsonObject;
    }


    /**
     * creates a PlayerAdded JSON object and sends the clientID, the player name and the figure
     */
    public static JSONObject PlayerAdded(Integer clientID, String name, Integer figure) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.CLIENT_NAME, name);
            jsonObject1.put(TypeName.FIGURE, figure);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAYER_ADDED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PlayerAdded() ", e);
        }

        return jsonObject;
    }


    /**
     * creates a SetStatus JSON object and sends information about being ready
     */
    public static JSONObject SetStatus(boolean status) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.READY, status);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SET_STATUS);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SetStatus() ", e);
        }

        return jsonObject;
    }

    /**
     * creates a PlayerStatus JSON object and sends the clientID and the information about being ready
     */
    public static JSONObject PlayerStatus(Integer clientID, boolean status) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.READY, status);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAYER_STATUS);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PlayerStatus() ", e);
        }

        return jsonObject;
    }

    /**
     * creates a SelectMap JSON object and sends the available maps in an array
     */
    public static JSONObject SelectMap(String[] availableMaps) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        if (availableMaps != null) {
            for (String availableMap : availableMaps) {
                jsonArray.put(availableMap);
            }
        }

        try {
            jsonObject1.put(TypeName.AVAILABLE_MAPS, jsonArray);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SELECT_MAP);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SelectMap() ", e);
        }

        return jsonObject;
    }


    /**
     * creates a MapSelected JSON object and sends the name of the selected map in an array
     */
    public static JSONObject MapSelected(String map) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();


        try {
            jsonObject1.put(TypeName.MAP, map);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.MAP_SELECTED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.MapSelected() ", e);
        }

        return jsonObject;
    }


    /**
     * creates a GameStarted JSON object and sends the map
     */
    public static JSONObject GameStarted(JSONObject jsonMap) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        Gson gson = new Gson();
        return jsonObject;
    }


    /**
     * creates a SendChat JSON object and sends the message and the clientID of the player/players that should receive the message
     */
    public static JSONObject SendChat(String message, Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.MESSAGE, message);
            jsonObject1.put(TypeName.TO, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SEND_CHAT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SendChat() ", e);
        }

        return jsonObject;
    }

    /**
     * creates a ReceivedChat JSON object and sends the message, the sender and the information about a private message
     */
    public static JSONObject ReceivedChat(String message, Integer clientID, Boolean isPrivate) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.MESSAGE, message);
            jsonObject1.put(TypeName.FROM, clientID);
            jsonObject1.put(TypeName.IS_PRIVATE, isPrivate);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.RECEIVED_CHAT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.ReceivedChat() ", e);
        }

        return jsonObject;
    }

    /**
     * creates an Error JSON object and sends the error
     */
    public static JSONObject Error(String error) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsujsonObject1msg = new JSONObject();

        try {
            jsujsonObject1msg.put(TypeName.ERROR, error);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.eRROR);
            jsonObject.put(TypeName.MESSAGE_BODY, jsujsonObject1msg);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Error()  ", e);
        }

        return jsonObject;
    }

    /**
     * creates a ConnectionUpdate JSON object and sends the clientID, the information about being connected and the action
     */
    public static JSONObject ConnectionUpdate(Integer clientID, boolean connected, String action) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.CONNECTED, connected);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CONNECTION_UPDATE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.ConnectionUpdate() ", e);
        }

        return jsonObject;
    }

    public static JSONObject GiveCard(String cardName) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CARD, cardName);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAY_CARD);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.GiveCard()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject CardPlayed(String cardName, Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CARD, cardName);
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CARD_PLAYED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CardPlayed()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject CurrentPlayer(Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CURRENT_PLAYER);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CurrentPlayer()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject ActivePhase(Integer phase) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.PHASE, phase);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.ACTIVE_PHASE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.ActivePhase()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject SetStartingPiont(Integer posx, Integer posy) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("x", posx);
            jsonObject1.put("y", posy);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SET_STARTING_POINT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SetStartingPiont()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject StartingPointTaken(Integer posx, Integer posy, Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("x", posx);
            jsonObject1.put("y", posy);
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.STARTING_POINT_TAKEN);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.StartingPointTaken()  ", e);
        }

        return jsonObject;
    }


    //TODO SHUFFLECODING

    public static JSONObject YourCards(String card1, String card2, String card3, String card4, String card5, String card6, String card7, String card8, String card9, Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {
            jsonArray1.put(card1);
            jsonArray1.put(card2);
            jsonArray1.put(card3);
            jsonArray1.put(card4);
            jsonArray1.put(card5);
            jsonArray1.put(card6);
            jsonArray1.put(card7);
            jsonArray1.put(card8);
            jsonArray1.put(card9);
            jsonObject1.put(TypeName.CARDS_IN_HAND, jsonArray1);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.YOUR_CARDS);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
            jsonObject.put(TypeName.CLIENT_ID, clientID);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.YourCards()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject NotYourCards(Integer clientID, Integer cards) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.CARDS_IN_HAND, cards);

            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.NOT_YOUR_CARDS);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.NotYourCards()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject SelectedCard(Integer register, String cardType) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject1.put(TypeName.CARD, cardType);
            jsonObject1.put(TypeName.REGISTER, register);

            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SELECTED_CARD);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SelectedCard()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject CardSelected(Integer register, Integer clientID, boolean filled) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.REGISTER, register);
            jsonObject1.put(TypeName.FILLED, filled);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CARD_SELECTED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CardSelected()  " ,e );
        }

        return jsonObject;
    }

    public static JSONObject SelectionFinished(Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SELECTION_FINISHED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SelectionFinished()  " ,e );
        }

        return jsonObject;
    }

    public static JSONObject TimerStarted() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.TIMER_STARTED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.TimerStarted()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject PlayCard(String cardName) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CARD, cardName);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAY_CARD);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PlayCard() ", e);
        }

        return jsonObject;
    }

    public static JSONObject TimerEnded(List<Integer> clientIDs) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        if (clientIDs != null) {
            for (Integer clientID : clientIDs) {
                jsonArray1.put(clientID);
            }
        }

        try {
            jsonObject1.put(TypeName.CLIENT_IDS, jsonArray1);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.TIMER_ENDED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.TimerEnded()  ", e);
        }
        return jsonObject;
    }

    public static JSONObject CardsYouGotNow(String[] cards) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        if (cards != null) {
            for (String card : cards) {
                jsonArray1.put(card);
            }
        }
        try {

            jsonObject1.put(TypeName.CARDS, jsonArray1);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CARDS_YOU_GOT_NOW);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CardsYouGotNow()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject CurrentCards(List<String> activeCards, List<Integer> clientids) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsArray = new JSONArray();
        Gson gson = new Gson();

        try {
            for (int i = 0; i < clientids.size(); i++) {
                JSONObject tempJsonObject = new JSONObject();
                tempJsonObject.put(TypeName.CLIENT_ID, clientids.get(i));
                tempJsonObject.put(TypeName.CARD, activeCards.get(i));
                jsArray.put(tempJsonObject);
            }
            jsonObject1.put(TypeName.ACTIVE_CARDS, jsArray);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CURRENT_CARDS);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CurrentCards()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject ReplaceCard(Integer register, Integer clientID, String newCard) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.REGISTER, register);
            jsonObject1.put(TypeName.CARD, newCard);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.REPLACE_CARD);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.ReplaceCard()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject Movement(Integer clientID, Integer x, Integer y) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put("x", x);
            jsonObject1.put("y", y);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.MOVEMENT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Movement()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject PlayerTurning(Integer clientID, String rotation) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.ROTATION, rotation);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PLAYER_TURNING);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PlayerTurning()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject DrawDamage(Integer clientID, List<String> cards) {
        JSONObject jsonObject= new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        if (cards != null) {
            for (String card : cards) {
                jsonArray1.put(card);
            }
        }

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.CARDS, jsonArray1);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.DRAW_DAMAGE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.DrawDamage()  " ,e );
        }

        return jsonObject ;
    }

    public static JSONObject PickDamage(Integer count) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.COUNT, count);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.PICK_DAMAGE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.PickDamage()  " ,e );
        }

        return jsonObject;
    }

    public static JSONObject SelectDamage(String[] cards) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        if (cards != null) {
            for (String card : cards) {
                jsonArray1.put(card);
            }
        }

        try {
            jsonObject1.put(TypeName.CARDS, jsonArray1);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SELECT_DAMAGE);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SelectDamage()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject Reboot(Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.REBOOT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Reboot()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject RebootDirection(String direction) {
        JSONObject jsonObject = new JSONObject();
        JSONObject  jsonObject1= new JSONObject();

        try {
            jsonObject1.put(TypeName.SET, direction);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.REBOOT_DIRECTION);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.RebootDirection()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject CheckpointReached(Integer clientID, Integer number) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.NUMBER, number);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.CHECKPOINT_REACHED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.CheckPointReached()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject GameFinished(Integer clientID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.GAME_FINISHED);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.GameFinished()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject SetStartingPoint(Integer posx, Integer posy) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("x", posx);
            jsonObject1.put("y", posy);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.SET_STARTING_POINT);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.SetStartingPiont()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject Animation(String type) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.TYPE, type);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.ANIMATION);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Animation()  ", e);
        }

        return jsonObject;
    }

    public static JSONObject Energy(Integer clientID, Integer count, String source) {    //es fehlt mir hier noch die "source": "EnergySpace"

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put(TypeName.SOURCE, source);
            jsonObject1.put(TypeName.CLIENT_ID, clientID);
            jsonObject1.put(TypeName.COUNT, count);
            jsonObject.put(TypeName.MESSAGE_TYPE, TypeName.ENERGY);
            jsonObject.put(TypeName.MESSAGE_BODY, jsonObject1);
        } catch (Exception e) {
            logger.error("Error from JsonCommands.Energy()  ", e);
        }

        return jsonObject;
    }
}


/**************************************************
 *                  SPIELKARTEN                   *
 **************************************************/


/**************************************************
 *               SPIELZUG ABHANDELN               *
 **************************************************/

//TODO SHUFFLECODING

//TODO TIMERENDED

/**************************************************
 *       AKTIONEN,EREIGNISSE UND EFFEKTe          *
 **************************************************/

//TODO ANIMATION



