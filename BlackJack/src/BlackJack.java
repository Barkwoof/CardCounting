import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    int numDecks = 1;

    ArrayList<Card> deck;

    //dealer hand
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player hand
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //window
    int boardWidth = 600;
    int boardHeight = 600;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    JPanel deckPanel = new JPanel();
    JTextField deckNum = new JTextField(16);
    JButton setDecks = new JButton("Set");
    JTextArea points = new JTextArea(1, 5);

    BlackJack(){
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(50, 110, 80));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        setDecks.setFocusable(false);
        deckPanel.add(setDecks);
        deckPanel.add(deckNum);
        deckPanel.add(points);

        frame.add(deckPanel, BorderLayout.NORTH);
    }

    public void startGame(){
        //deck
        buildDeck();
        shuffleDeck(deck);

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getTrueValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getTrueValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("dealerhand");
        System.out.println(dealerHand.toString());
        System.out.println(dealerSum);

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        Card card1 = deck.remove(deck.size() -1 );
        playerHand.add(card1);
        playerSum += card1.getTrueValue();
        playerAceCount += card1.isAce() ? 1 : 0;

        Card card2 = deck.remove(deck.size() -1 );
        playerHand.add(card2);
        playerSum += card2.getTrueValue();
        playerAceCount += card2.isAce() ? 1 : 0;

        System.out.println("playerhand: ");
        System.out.println(playerHand.toString());
        System.out.println(playerSum);

    }
    public void buildDeck(){

        //TODO add a text box into the gui to give the number of decks

        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for(int i = 0; i < numDecks; i++){
            for(int k = 0; k < types.length; k++) {
                for (int j = 0; j < values.length; j++) {
                    Card card = new Card(values[j], types[k]);
                    deck.add(card);
                }
            }
        }

    }
    public void shuffleDeck(ArrayList<Card> deck){
        Collections.shuffle(deck);


    }
}
