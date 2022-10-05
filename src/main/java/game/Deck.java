package game;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private final LinkedList<Card> deck;
    ArrayList<String> cardDeck = new ArrayList<>();

    public Deck() {
        deck = new LinkedList<>();
    }

    public void shuffleUpgradeCards() {
    }

    public void shuffleProgrammingDeck() {
    }

    public void shuffleSpamCards() {
    }

    public void shuffleWormCards() {
    }

    public void shuffleVirusCards() {
    }

    public void shuffleTrojanHorseCards() {
    }

    public void drawCard(Deck deck) {
    }

    public List<String> Give9Cards() throws IOException {
        ArrayList<String> Deck9Cards = new ArrayList<>();
        cardDeck.add("MoveI");
        cardDeck.add("MoveI");
        cardDeck.add("MoveI");
        cardDeck.add("PowerUp");
        cardDeck.add("Again");
        cardDeck.add("MoveII");
        cardDeck.add("MoveII");
        cardDeck.add("PowerUp");
        cardDeck.add("Again");
        cardDeck.add("MoveIII");
        cardDeck.add("BackUp");
        cardDeck.add("TurnRight");
        cardDeck.add("TurnRight");
        cardDeck.add("TurnRight");
        cardDeck.add("BackUp");
        cardDeck.add("TurnLeft");
        cardDeck.add("TurnLeft");
        cardDeck.add("TurnLeft");
        cardDeck.add("UTurn");
        cardDeck.add("UTurn");

        for (int b = 0; b < 9; b++) {
            Random random = new Random();
            String card1 = cardDeck.get(random.nextInt(cardDeck.size()));
            cardDeck.remove(card1);
            if (card1 == null) {
                card1 = GiveCard();
            }
            Deck9Cards.add(card1);

        }
        return Deck9Cards;
    }

    public String GiveCard() {

        ArrayList<String> Deck9Cards = new ArrayList<>();
        Deck9Cards.add("MoveI");
        Deck9Cards.add("MoveI");
        Deck9Cards.add("MoveI");
        Deck9Cards.add("MoveI");
        Deck9Cards.add("MoveI");
        Deck9Cards.add("MoveII");
        Deck9Cards.add("MoveII");
        Deck9Cards.add("MoveII");
        Deck9Cards.add("MoveII");
        Deck9Cards.add("MoveII");
        Deck9Cards.add("TurnRight");
        Deck9Cards.add("TurnRight");
        Deck9Cards.add("TurnRight");
        Deck9Cards.add("TurnRight");
        Deck9Cards.add("TurnLeft");
        Deck9Cards.add("TurnLeft");
        Deck9Cards.add("TurnLeft");
        Deck9Cards.add("TurnLeft");
        Deck9Cards.add("UTurn");
        Deck9Cards.add("UTurn");

        Random random = new Random();
        String finalCard = Deck9Cards.get(random.nextInt(Deck9Cards.size()));
        return finalCard;
    }
}