import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    String message = "";

    int numDecks = 1;
    int cardCount = 0;

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

    //ratio of width to height should be 1 : 1.4
    int cardWidth = 110;
    int cardHeight = 154;


    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try {
                //draw hiddencard
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./PNG/blue_back.png")).getImage();
                if(!stayButton.isEnabled()){
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //draw dealer's hand
                for(int i = 0; i < dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null );
                }

                //player's hand
                for(int i = 0; i < playerHand.size(); i++){
                    Card card = playerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage,  20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null );
                }

                if(!stayButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);


                    if(playerSum > 21){
                        message = "You Lose";
                    }
                    else if (dealerSum > 21){
                        message = "You win";
                    }
                    else if(dealerSum == playerSum){
                        message = "Tie";
                    }
                    else if(playerSum > dealerSum){
                        message = "You win";
                    }
                    else if (playerSum < dealerSum){
                        message = "You lose";
                    }
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);


                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton playAgain = new JButton("Play again");

    JPanel deckPanel = new JPanel();
    JTextField deckNum = new JTextField(16);
    JButton setDecks = new JButton("Set");
    JTextArea points = new JTextArea(Integer.toString(cardCount), 1, 5);

    BlackJack(){
        //deck
        buildDeck();
        shuffleDeck(deck);

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
        playAgain.setFocusable(false);
        buttonPanel.add(playAgain);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        setDecks.setFocusable(false);
        deckPanel.add(setDecks);
        deckPanel.add(deckNum);
        deckPanel.add(points);

        frame.add(deckPanel, BorderLayout.NORTH);

        setDecks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numDecks = Integer.parseInt(deckNum.getText());
                buildDeck();
                shuffleDeck(deck);
                System.out.println(numDecks);
                System.out.println(deck.size());
            }
        });

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                Card card = deck.remove(deck.size()-1);
                cardCount += card.getCountValue();
                playerSum += card.getTrueValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                points.setText(Integer.toString(cardCount));

                if(reducePlayerAce() >= 21){
                    hitButton.setEnabled(false);
                }

                gamePanel.repaint();
            }
        });

        playAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(deck.size());
                int n = dealerHand.size();

                for (int i = 0; i < n; i++) {
                    dealerHand.remove(dealerHand.size() - 1);

                }
                int p = playerHand.size();
                for (int i = 0; i < p; i++) {
                    playerHand.remove(playerHand.size() - 1);

                }
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);


                if(deck.size() < 12*numDecks){
                    buildDeck();
                    shuffleDeck(deck);
                }
                //TODO make the game replay with the same deck and remove startGame()
                startGame();
                System.out.println(deck.size());



                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while(dealerSum < 17){
                    Card card = deck.remove(deck.size() - 1);
                    cardCount += card.getCountValue();
                    dealerSum += card.getTrueValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);

                    points.setText(Integer.toString(cardCount));
                }
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void startGame(){


        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getTrueValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        cardCount += card.getCountValue();
        dealerSum += card.getTrueValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);
        points.setText(Integer.toString(cardCount));

        System.out.println("dealerhand");
        System.out.println(dealerHand.toString());
        System.out.println(dealerSum);

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        Card card1 = deck.remove(deck.size() -1 );
        cardCount += card1.getCountValue();
        playerHand.add(card1);
        playerSum += card1.getTrueValue();
        playerAceCount += card1.isAce() ? 1 : 0;
        points.setText(Integer.toString(cardCount));

        Card card2 = deck.remove(deck.size() -1 );
        cardCount += card2.getCountValue();
        playerHand.add(card2);
        playerSum += card2.getTrueValue();
        playerAceCount += card2.isAce() ? 1 : 0;
        points.setText(Integer.toString(cardCount));

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

    public int reducePlayerAce(){
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -= 1;

        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    public ArrayList<Card> refresh(){
        ArrayList<Card> refreshed = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for(int i = 0; i < numDecks; i++){
            for(int k = 0; k < types.length; k++) {
                for (int j = 0; j < values.length; j++) {
                    Card card = new Card(values[j], types[k]);
                    refreshed.add(card);
                }
            }
        }
        shuffleDeck(refreshed);
        return refreshed;
    }
}
