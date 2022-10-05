package model;


import java.util.Arrays;
import java.util.List;


/**
 * Purpose of this Class is to contains general parameters, which we need through out whole Project.
 */
public class TypeName {


    public final static String MSG_TYPE_INFO = "Info";
    public final static String MSG_TYPE_ERROR = "Error";
    public final static String EMPTY_STRING = "";
    public static final String CHECKPOINT_REACHED = "checkPointReached";
    public static final String GAME_FINISHED = "GameFinished";
    public static final String NUMBER = "number";
    public static final String CARDS_YOU_GOT_NOW = "CardsYouGotNow";
    public static final String IS_ON_BOARD = "isOnBoard";
    public static final String DEATH_TRAP = "DeathTrap";
    public static final String SET_STARTING_POINT = "SetStartingPoint";
    public static final String SOURCE = "source";
    public static final String COUNT = "count";
    public static final String ENERGY = "Energy";
    public static final String SELECT_DAMAGE = "selectedDamage";
    public static final String ANIMATION = "Animation";
    public static final String REBOOT_DIRECTION = "RebootDirection";
    public static final String DRAW_DAMAGE = "DrawDamage";
    public static final String PICK_DAMAGE = "PickDamage";
    public static final String REPLACE_CARD = "ReplaceCard";
    public final static String TO = "to";

    ;
    public final static String CARD_PLAYED = "CardPlayed";
    public final static String PLAY_CARD = "PlayCard";


    /**
     * Number of min/max players
     */
    public final static int MAX_PLAYER = 6;
    public final static int MIN_PLAYER = 2;


    /**
     * Protocol versions
     */
    public final static String SERVER_PROTOCOL = "Version 1.0";
    public final static String CLIENT_PROTOCOL = "Version 1.0";
    public final static String PROTOCOL = "protocol";
    public final static String ALIVE = "Alive";
    public final static String MESSAGE_TYPE = "messageType";
    public final static String MESSAGE_BODY = "messageBody";
    public final static String HELLO_CLIENT = "HelloClient";
    public final static String HELLO_SERVER = "HelloServer";
    public final static String IS_AI = "isAI";
    public final static String GROUP = "group";
    public final static String WELCOME = "Welcome";
    public final static String PLAYER_ADDED = "PlayerAdded";
    public final static String READY = "ready";
    public final static String PLAYER_STATUS = "PlayerStatus";
    public final static String SEND_CHAT = "SendChat";
    public final static String MESSAGE = "message";
    public final static String MOVEMENT = "Movement";
    public final static String FROM = "from";
    public final static String RECEIVED_CHAT = "ReceivedChat";
    public final static String IS_PRIVATE = "isPrivate";
    public final static String ERROR = "Error";
    public final static String eRROR = "error";
    public final static String CONNECTION_UPDATE = "ConnectionUpdate";
    public final static String CONNECTED = "connected";
    public final static String ACTION = "action";
    public final static Integer TO_ALL = -1;
    public final static String CLIENT_ID = "clientID";
    public final static String ROTATION = "rotation";
    public final static String POSX = "x";
    public final static String POSY = "y";
    public final static String MAP = "map";
    public final static String GAME_MAP = "gameMap";
    public final static String GAME_STARTED = "GameStarted";
    public final static String POSITION = "position";
    public final static String ACTIVE_CARDS = "activeCards";
    public final static String CURRENT_CARDS = "CurrentCards";
    public final static String AVAILABLE_MAPS = "availableMaps";
    public final static String SELECT_MAP = "SelectMap";
    public final static String MAP_SELECTED = "MapSelected";
    public final static String SET = "set";
    public final static String YOUR_CARDS = "YourCards";
    public final static String NOT_YOUR_CARDS = "NotYourCards";
    public final static String PHASE = "phase";
    public final static String CARDS_IN_HAND = "cardsInHand";
    public final static String PLAYER_TURNING = "PlayerTurning";
    public static final String CLIENT_IDS = "clientIDs";
    public final static String TYPE = "type";
    public final static String COORDINATE_X = "x";
    public final static String COORDINATE_Y = "y";
    public final static String SET_STARTINGPOINT = "SetStartingPoint";

    public final static String GROUP_NAME = "AdretteApparate";
    public final static String CLIENT_NAME = "name";
    public final static String FIGURE = "figure";
    public final static String STARTING_POINT_TAKEN = "StartingPointTaken";
    public final static String CARD_SELECTED = "CardSelected";

    public final static String BYE = "Bye";
    public final static String PLAYER_VALUES = "PlayerValues";
    public final static String SHOW_TEAMMATES = "ShowTeammates";
    public final static String SET_STATUS = "SetStatus";
    public final static String CURRENT_PLAYER = "CurrentPlayer";
    public final static String CARD = "card";
    public final static String SELECTCARD = "SelectCard";
    public final static String PLAYIT = "PlayIt";
    public final static String SELECTDAMAGE = "SelectDamage";
    public final static String MAPSELECTED = "MapSelected";
    public final static String SELECTED_CARD = "SelectedCard";
    public final static String FILLED = "filled";
    public final static String SELECTION_FINISHED = "SelectionFinished";
    public final static String TIMER_STARTED = "TimerStarted";
    public final static String TIMER_ENDED = "TimerEnded";
    public final static String CARDS = "cards";
    public final static String ACTIVE_PHASE = "ActivePhase";
    public final static String REBOOT = "Reboot";
    public final static String ADD_AI = "AddAI";
    public final static List<String> COMMANDS = Arrays.asList(BYE, PLAYER_VALUES, SHOW_TEAMMATES, SET_STATUS,
            SET_STARTINGPOINT, SELECTCARD, PLAYIT, SELECTDAMAGE, MAPSELECTED);
    public final static String REGISTER = "register";
    public final static int TIMER = 30;


    public final static String EMPTY = "Empty";//
    public final static String BELT = "Belt";
    public final static String CONVEYOR_BELT = "ConveyorBelt";//
    public final static String PUSH_PANEL = "PushPanel";//
    public final static String GEAR = "Gear";//
    public final static String PIT = "Pit";//
    public final static String ENERGY_SPACE = "EnergySpace";//
    public final static String WALL = "Wall";//
    public final static String LASER = "Laser";//
    public final static String ANTENNA = "Antenna";//
    public final static String START_POINT = "StartPoint";//
    public final static String CHECK_POINT = "CheckPoint";//
    public final static String RESTART_POINT = "RestartPoint";//
    public final static String New_CARD = "newCard";

    public static enum MSG_TYPE {MSG_TYPE_INFO, MSG_TYPE_ERROR}


    public final static List<String> ROBOTS = Arrays.asList("Twonky", "Hulk", "Hammerbot", "Smashbot", "Zoombot", "Spinbot");
    public final static int TWONKY = 0;
    public final static int HULK = 1;
    /**
     * possible figures
     */
    private final static String FIGURE_1 = "0";
    private final static String FIGURE_2 = "1";
    private final static String FIGURE_3 = "2";
    private final static String FIGURE_4 = "3";
    private final static String FIGURE_5 = "4";
    private final static String FIGURE_6 = "5";
    public final static List<String> FIGURES = Arrays.asList(FIGURE_1, FIGURE_2, FIGURE_3, FIGURE_4, FIGURE_5, FIGURE_6);
    private final static int HAMMERBOT = 2;
    private final static int SMASHBOT = 3;
    private final static int ZOOMBOT = 4;
    private final static int SPINBOT = 5;


    /**
     * Ip address and port of the server
     */
    public static String ip = "sep21.dbs.ifi.lmu.de";
    public static int port = 52018;

}



