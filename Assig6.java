/*
 * Jospeh Cortez
 * Lyndsay Hackett
 * Mohklis Awad
 * Ahdia Fuller
 * 
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.io.File;

/**
 * 
 * Main method for the game
 *
 */
public class Assig6
{
   
   public static void main(String[] args)
   {  
      GameControl game = new GameControl();
      game.startGame();
   }

}

class Card
{
   //Public enum Members:
   public enum Suit
   {
      CLUBS(0, 'C'), DIAMONDS(1, 'D'), HEARTS(2, 'H'), SPADES(3, 'S');

      public static final int NUM_SUITS = 4;
      public int intValue;
      public char charValue;

      Suit(int intValue, char charValue)
      {
         this.intValue = intValue;
         this.charValue = charValue;
      }

      public static char valueOf(int integer)
      {
         for (Card.Suit suit : Card.Suit.values())
         {
            if (integer == suit.intValue)
            {
               return suit.charValue;
            }
         }
         throw new IllegalArgumentException(String.format("%s is not a valid argument"));
      }
   }

   //Public Static Data Members:
   public static final char[] LEGAL_VALUES = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   public static char[] valueRanks = LEGAL_VALUES;

   //Private Data Members:
   private char value;
   private Card.Suit cardSuit = null;
   private boolean errorFlag = false;

   /**
    * Default Constructor Initializes value to 'A' and Suit to spades
    */
   public Card()
   {
      set('A', Card.Suit.SPADES);
   }

   /**
    * Public Constructor
    *
    * @param newValue card value to be stored
    * @param newSuit  card suit to be stored
    */
   public Card(char newValue, final Card.Suit newSuit)
   {
      set(newValue, newSuit);
   }

   /**
    * Copy Constructor
    *
    * @param originalCard values to be copied into new separate
    *                     Card object
    */
   public Card(final Card originalCard)
   {
      value = originalCard.value;
      cardSuit = originalCard.cardSuit;
      errorFlag = originalCard.errorFlag;
   }

   public String toString()
   {
      return errorFlag ? "** illegal **" : String.format("%s of %s", value, cardSuit);
   }

   public boolean equals(final Card compareCard)
   {
      return compareCard.cardSuit == cardSuit && compareCard.value == value
              && compareCard.errorFlag == errorFlag;
   }


   public boolean set(char newValue, final Card.Suit newSuit)
   {
      if (!(errorFlag = !isValid(newValue, newSuit)))
      {
         // if valid we are saving the variables
         cardSuit = newSuit;
         value = newValue;
      }
      // returning whether or not the values are valid.
      return !errorFlag;
   }

   /**
    * Accessor Method for value
    *
    * @return the value of value
    */
   public char getValue()
   {
      return value;
   }

   /**
    * Accessor Method for cardSuit
    *
    * @return the value of cardSuit
    */
   public Card.Suit getSuit()
   {
      return cardSuit;
   }

   /**
    * Accessor Method for errorFlag
    *
    * @return the value of errorFlag
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }


   public static void arraySort(final Card[] cards, int arraySize)
   {
      boolean swapped = false;
      for (int i = 1; i < arraySize; i++)
      {
         if (cardValue(cards[i - 1]) > cardValue(cards[i]))
         {
            final Card temp = cards[i - 1];
            cards[i - 1] = cards[i];
            cards[i] = temp;
            swapped = true;
         }
      }
      if (swapped)
      {
         arraySort(cards, arraySize - 1);
      }
   }

   public static int valueOf(final Card card)
   {
      return new String(Card.valueRanks).indexOf(card.getValue());
   }


   public static int cardValue(final Card card)
   {
      return Card.valueRanks.length - new String(valueRanks).indexOf(card.getValue());
   }


   private boolean isValid(char checkValue, final Card.Suit checkSuit)
   {
      boolean isValidValue = false;

      // Check validity of checkValue against all legalValues
      // set isValidValue to true if valid
      for (char legalValue : LEGAL_VALUES)
      {
         if (checkValue == legalValue)
         {
            isValidValue = true;
         }
      }
      return isValidValue;
   }
}

class Deck
{
   // Public data members

   public Card[] cards;
   //Private Data Members:
   private static Card[] masterPack = new Card[56];

   //Public Data Members:
   private final int MAX_CARDS = 320;
   private int topCard;
   private int numPacks;

   // Default Constructor
   public Deck()
   {
      allocateMasterPack();
      init(numPacks = 1);
   }

   /**
    * Constructor
    *
    * @param numPacks number of decks determined by call
    */
   public Deck(int numPacks)
   {
      allocateMasterPack();
      init(numPacks);
   }

   public void init(int numPacks)
   {
      if (numPacks > 0 && numPacks < (MAX_CARDS / 56))
      {
         cards = new Card[numPacks * 56];
         int packCount = 0;
         for (int i = 0; i < numPacks; i++)
         {
            for (Card pulledCard : masterPack)
            {
               cards[packCount] = pulledCard;
               packCount++;
            }
         }
         topCard = cards.length - 1;
      }
      else
      {
         System.err.println("Cannot initialize deck. "
                 + "Number of cards either exceeds or does not reach "
                 + "the card boundaries.");
      }

   }

   /**
    * Method to shuffle the cards
    */

   public void shuffle()
   {
      int cursor = cards.length - 1; //Delineates between shuffled and unshuffled portions of cards array
      for (int i = 0; i < cards.length && cursor > 0; i++)
      {
         int randomNumber = new java.util.Random().nextInt(cursor + 1); //Generate a random num between 0 and length of unshuffled side of array
         Card pulledCard = cards[randomNumber]; //save the randomly selected card in a temporary object
         for (int j = randomNumber; j < cursor; j++) //shift all unshuffled cards left by one index
         {
            cards[j] = cards[j + 1];
         }
         cards[cursor--] = pulledCard; //Place pulled card at cursor, shift cursor left and repeat
      }
   }

   
   public Card dealCard()
   {
      if (topCard >= 0)
      {
         final Card pulledCard = new Card(inspectCard(topCard)); //stores a temp location of top card deep copy
         cards[topCard--] = null; //sets the index of the cards array to null and decrements the top card
         return pulledCard;
      }
      else
      {
         return new Card('?', Card.Suit.DIAMONDS);
      }
   }

   /**
    * Sort array
    */
   public void sort()
   {
      Card.arraySort(cards, cards.length);
   }

   /**
    *  Accessor Method
    *
    * @return the index of the top card in the cards array.
    */
   public int getTopCard()
   {
      return topCard;
   }

   /**
    * Access Method
    *
    * @return the number of cards in the deck
    */
   public int getNumCards()
   {
      return topCard;
   }

   /**
    * Checks if there is room in deck to append card to
    *
    * @param card to be appended to the deck
    * @return boolean representing the success of the addition.
    */
   public boolean addCard(final Card card)
   {
      for (int i = 0, found = 0; i < topCard; i++)
      {
         if (cards[i].equals(card)) // check if we have found the card
         {
            if ((++found) > numPacks) // increment found and check if we have found too many
            {
               return false;
            }
         }
      }
      cards[topCard++] = card;
      return true;
   }

   /**
    * Removes card from deck
    *
    * @param card to be removed from the deck
    * @return boolean representing the success of the deletion
    */
   public boolean removeCard(final Card card)
   {
      for (int i = 0; i < topCard; i++)
      {
         if (cards[i].equals(card))
         {
            cards[i] = cards[topCard]; // swap removed card with top card
            cards[topCard--] = null; // decrement top card
            return true;
         }
      }
      return false;
   }


   public Card inspectCard(int k)
   {
      return k <= topCard ? cards[k] : new Card('?', Card.Suit.DIAMONDS);
   }


   private static void allocateMasterPack()
   {
      if (masterPack[0] == null) //checks to see if masterPack is already initialized.
      {
         int packIndex = 0; //counter for masterPack indexes.
         for (Card.Suit suit : Card.Suit.values())
         {
            for (char legalValue : Card.LEGAL_VALUES)
            {
               masterPack[packIndex++] = new Card(legalValue, suit);
            }
         }
      }
   }
}

class Hand
{
   // Public Data Members
   public static final int MAX_CARDS = 50; // Max number of cards allowed

   // Private Data Members
   private Card[] myCards; // Cards in users hand
   private int numCards;

   /**
    * Default Constructor
    */
   public Hand()
   {
      resetHand();
   }


   public boolean takeCard(final Card card)
   {
      if (numCards >= MAX_CARDS)
      {
         return false;
      }
  
      return !(myCards[numCards++] = new Card(card)).getErrorFlag();
   }

   /**
    *
    * @return top card of the hand
    */
   public Card playCard()
   {
      if (numCards <= 0)
      {
         return new Card('?', Card.Suit.DIAMONDS);
      }
      Card card = new Card(myCards[--numCards]); // cache temp location of top card deep copy
      myCards[numCards] = null;
      return card;
   }


   public Card playCard(int cardIndex)
   {
      if (numCards == 0) //error
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }
      //Decreases numCards.
      final Card card = myCards[cardIndex];

      numCards--;
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }

      myCards[numCards] = null;

      return card;
   }

   /**
    * Accessor Method
    *
    * @return an integer representing the current number of cards in the hand
    */
   public int getNumCards()
   {
      return numCards;
   }

   public Card inspectCard(int k)
   {
      return k < numCards ? myCards[k] : new Card('?', Card.Suit.DIAMONDS);
   }

   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }


   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }


   public String toString()
   {
      String sb = "( ";
      for (int i = 0; i < numCards; i++)
      {
         if (i + 1 >= numCards)
         {
            sb += (String.format("%s ", myCards[i]));
         }
         else
         {
            sb += (String.format("%s, ", myCards[i]));
         }
      }
      return sb += ")";
   }
}

/**
 * 
 * Card game model: Model for the Card game
 *
 */
class CardGameModel
{
 private static final int MAX_PLAYERS = 50;
 private int cardsInPlayArea;
 private int numPlayers;
 private int numPacks;
 private int numJokersPerPack;
 private int numUnusedCardsPerPack;
 private int numCardsPerHand;
 private Deck deck;
 private Hand[] hand;
 private Card[] unusedCardsPerPack;   // an array holding the cards not used
 private Card[][] playArea;
 private int[] scores;
 private int[] topStacks;


 /**
  *   
  * Constructor for the model
  */
 public CardGameModel(int numPacks, int numJokersPerPack,
                      int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
                      int numPlayers, int numCardsPerHand, int cardsInPlayArea)
 {
    // filter bad values
    if (numPacks < 1 || numPacks > 6)
    {
       numPacks = 1;
    }
    if (numJokersPerPack < 0 || numJokersPerPack > 4)
    {
       numJokersPerPack = 0;
    }
    if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50)
    {
       numUnusedCardsPerPack = 0;
    }
    if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
    {
       numPlayers = 4;
    }
    if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 + numJokersPerPack - numUnusedCardsPerPack) / numPlayers)
    {
       numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
    }

    scores = new int[numPlayers];


    this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
    this.hand = new Hand[numPlayers];
    for (int i = 0; i < numPlayers; i++)
    {
       this.hand[i] = new Hand();
    }
    deck = new Deck(numPacks);

    this.numPacks = numPacks;
    this.numJokersPerPack = numJokersPerPack;
    this.numUnusedCardsPerPack = numUnusedCardsPerPack;
    this.numPlayers = numPlayers;
    this.numCardsPerHand = numCardsPerHand;
    for (int i = 0; i < numUnusedCardsPerPack; i++)
    {
       this.unusedCardsPerPack[i] = unusedCardsPerPack[i];
    }

    // prepare deck and shuffle
    newGame();
    this.cardsInPlayArea = cardsInPlayArea;

 }

 /**
  * Default constructor
  */
 public CardGameModel()
 {
    this(1, 0, 0, null, 4, 13, 2);
 }

 /**
  * Accessor method for getting the hand
  */
 public Hand getHand(int i)
 {
    if (i < 0 || i >= numPlayers)
    {
       return new Hand();
    }
    return hand[i];
 }

 /**
  * Accessor method for number of players
  */
 public int getNumPlayers()
 {
    return numPlayers;
 }

 /**
  * Accessor method for number of cards per hand
  */
 public int getNumCardsPerHand()
 {
    return numCardsPerHand;
 }

 /**
  * Accessor method for 2d array of play area
  */
 public Card[][] getPlayArea()
 {
    return playArea;
 }

 /**
  * Method to get the number of cards in the indexed play area
  */
 public int numCardsInPlayArea(int playerAreaIndex)
 {
    return topStacks[playerAreaIndex];
 }

 public int totalCardsInPlay()
 {
    int total = 0;
    for (int i = 0; i < topStacks.length; i++)
    {
       total += topStacks[i];
    }
    return total;
 }


 public Card getCardFromDeck()
 {
    return deck.dealCard();
 }

 public int getNumCardsRemainingInDeck()
 {
    return deck.getNumCards();
 }

 
 public void newGame()
 {
    // set play area
    playArea = new Card[numPlayers][numPacks * numPlayers * (52 + numJokersPerPack - numUnusedCardsPerPack)];

    topStacks = new int[numPlayers];
    for (int i = 0; i < numPlayers; i++)
    {
       topStacks[i] = 0;
    }
    resetScores();
    // clear the hands
    for (int i = 0; i < numPlayers; i++)
    {
       hand[i].resetHand();
    }

    deck.init(numPacks);

    // remove unused cards
    for (int i = 0; i < numUnusedCardsPerPack; i++)
    {
       deck.removeCard(unusedCardsPerPack[i]);
    }

    // add jokers
    for (int i = 0; i < numPacks; i++)
    {
       for (int j = 0; j < numJokersPerPack; j++)
       {
          deck.addCard(new Card('X', Card.Suit.values()[j]));
       }
    }
    // shuffle cards
    deck.shuffle();
 }

 /**
  * Accessor method for computer card in play area
  */
 public Card getTopCardAtPlayAreaIndex(int index)
 {
    if (index == 0 || index == 1)
    {
       int topIndex = topStacks[index] == 0 ? topStacks[index] : topStacks[index] - 1;
       final Card card = playArea[index][topIndex];
       return card == null ? new Card('?', Card.Suit.SPADES) : card;
    }
    else
    {
       return new Card('?', Card.Suit.SPADES);
    }
 }

 /**
  * Method to deal cards to all players
  */
 public boolean deal()
 {
    boolean enoughCards = true;

    // clear all hands
    for (int i = 0; i < numPlayers; i++)
    {
       hand[i].resetHand();
    }

    for (int i = 0; i < numCardsPerHand && enoughCards; i++)
    {
       for (int j = 0; j < numPlayers; j++)
       {
          if (deck.getNumCards() > 0)
          {
             hand[j].takeCard(deck.dealCard());
          }
          else
          {
             enoughCards = false;
             break;
          }
       }
    }
    return enoughCards;
 }

 /**
  * Sorts every hand in play
  */
 public boolean sortHands()
 {
    for (int i = 0; i < numPlayers; i++)
    {
       hand[i].sort();
    }
    return true;
 }

 /**
  * Puts the card at the top of the play area array at the area
  * index.
  */
 public boolean placeCardInPlayArea(final Card card, int areaIndex)
 {
    try
    {
       playArea[areaIndex][topStacks[areaIndex]] = card;
       topStacks[areaIndex] += 1;
    }
    catch (IndexOutOfBoundsException e)
    {
       e.printStackTrace();
       return false;
    }
    return true;
 }


 public int getPlayerScore(int playerIndex)
 {
    return scores[playerIndex];
 }

 public boolean increaseScoreBy(int playerIndex, int scoreIncrement)
 {
    try
    {
       scores[playerIndex] += scoreIncrement;
    }
    catch (IndexOutOfBoundsException e)
    {
       return false;
    }
    return true;
 }

 /**
  * Player at given index takes a card from the deck
  */
 public boolean takeCard(int playerIndex)
 {
    // returns false if either argument is bad
    if (playerIndex < 0 || playerIndex > numPlayers - 1)
    {
       return false;
    }

    // Are there enough Cards?
    return deck.getNumCards() > 0 && hand[playerIndex].takeCard(deck.dealCard());
 }

 /**
  * Resets all players scores to zero
  */
 public boolean resetScores()
 {
    for (int i = 0; i < scores.length; i++)
    {
       scores[i] = 0;
    }
    return true;
 }
}

/**
 * The model for the timer
 */
class Timer extends Thread
{
   private int minutes, seconds;
   private boolean timerOn, timerStarted;
   private final static int WAIT = 1000;
   private final TimerDisplay display;

   /**
    * Default constructor
    */
   public Timer(TimerDisplay displayIn)
   {
      minutes = 0;
      seconds = 0;
      timerOn = false;
      display = displayIn;
   }

   /**
    * Contains all of the needed code to operate the timer model
    */
   public void run()
   {
      timerStarted = true;
      while (timerStarted)
      {
         doNothing(WAIT);
         if (seconds < 59 && timerOn)
         {
            seconds++;
         }
         else if ((seconds >= 59) && timerOn)
         {
            seconds = 0;
            minutes++;
         }
         if (timerOn)
         {
            display.update(this);
         }

      }
   }


   public void stopTimer()
   {
      timerOn = false;
   }


   public void resumeTimer()
   {
      timerOn = true;
   }

   /**
    * Method to check if timer is on
    */
   public boolean timerOn()
   {
      return timerOn;
   }

 
   public int getMinutes()
   {
      return minutes;
   }


   public int getSeconds()
   {
      return seconds;
   }

   /**
    * Change minutes and seconds to milliseconds
    */
   public int getMilliseconds()
   {
      return (minutes * 60000) + (seconds * 1000);
   }

   /**
    * Check if timer started
    */
   public boolean started()
   {
      return timerStarted;
   }

   public TimerDisplay getDisplayObject()
   {
      return display;
   }

   /**
    * String representation for the timer
    */
   public String toString()
   {
      if (seconds < 10)
      {
         return String.format(" %s:0%s ", minutes, seconds);
      }
      else
      {
         return String.format(" %s:%s ", minutes, seconds);
      }
   }

   /* 
      Private helper method to test timer in console
    */
  // private void printTime()
  // {
   //   System.out.printf(toString());
   //}

   /*
   Private helper method pauses for waitTime milliseconds
    */
   private void doNothing(int waitTime)
   {
      try
      {
         Thread.sleep(waitTime);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
         System.exit(0);
      }

   }
}

/**
 * View class to intergrate Timer class into GUI interface
 */
class TimerDisplay extends JPanel
{

   public JButton startStopButton;

   private JTextArea timerText;

   private final int WIDTH = 200;
   private final int HEIGHT = 80;

   private String timeString;

   /**
    * Default constructor
    */
   public TimerDisplay()
   {
      super();
      timeString = String.format(" %s:0%s ", 0, 0);
      this.setSize(WIDTH, HEIGHT);
      initTimerText();
      initStartStopButton();
   }

   /**
    * Constructor with a timeIn
    */
   public TimerDisplay(String timeIn)
   {
      this();
      timeString = timeIn;
      timerText.validate();
   }

   /**
    * Refresh the timer panel
    */
   public void update(final Timer timer)
   {
      timerText.setText(timer.toString());
      timerText.repaint();
      timerText.validate();
      startStopButton.repaint();
      startStopButton.validate();
      repaint();
      validate();
   }

   /*
      Private helper method for initializing the start/stop button
    */
   private void initStartStopButton()
   {
      startStopButton = new JButton("Start");
      startStopButton.setVisible(true);
      add(startStopButton);
   }

   /*
      Private helper method to initialize the timer text object
    */
   private void initTimerText()
   {
      timerText = new JTextArea(timeString);
      timerText.setBackground(Color.BLACK);
      timerText.setForeground(Color.WHITE);
      timerText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
      add(timerText);
   }
}

/**
 * View class that represents the game screen that the player will interact with
 *
 */
class CardGameView extends JFrame
{
   private static final int MAX_CARDS_PER_HAND = 56;
   private static final int MAX_PLAYERS = 2;
   private int numCardsPerHand;
   private int numPlayers;

   // JPanel objects representing the sections of the Frame
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlDashboard;
   private JButton cantPlayBtn;
   private JButton replayBtn;

   /**
    * Constructor for the JPanels
    */
   public CardGameView(String title, int numCardsPerHand, int numPlayers, int width, int height)
   {
      initDataMembers(numCardsPerHand, numPlayers);
      initFrame(title);
      setSize(width, height);
      initReplayButton();
      initCantPlayBtn();
      initPanels();
   }

   /**
    * Accessor method for number of cards allowed per hand
    */
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   public int getNumPlayers()
   {
      return numPlayers;
   }

   /**
    * Method for replay button
    */
   public boolean replayButtonAction(Runnable action)
   {
      replayBtn.addMouseListener(new MouseAdapter()
      {
         /**
          * Method to start a new game when the replay button is pressed
          */
         public void mousePressed(MouseEvent e)
         {
            action.run();
         }
      });
      return true;
   }

   /**
    * Method for can't play button
    */
   public boolean cantPlayButtonAction(Runnable action)
   {
      cantPlayBtn.addMouseListener(new MouseAdapter()
      {
         /**
          * Method that starts when a user presses on a card they can't play
          */
         public void mousePressed(MouseEvent e)
         {
            action.run();
         }
      });
      return true;
   }

   /**
    * Set the replay button
    */
   public boolean enableReplayButton(boolean bool)
   {
      if(bool)
      {
         enableCantPlayButton(false);
      }
      replayBtn.setVisible(bool);
      replayBtn.setEnabled(bool);
      return true;
   }

   /**
    * Set the can't play button
    */
   public boolean enableCantPlayButton(boolean bool)
   {
      cantPlayBtn.setVisible(bool);
      cantPlayBtn.setEnabled(bool);
      return true;
   }


   /*
    * Private helper method for initializing the cantPlayButton button
    */
   private void initCantPlayBtn()
   {
      cantPlayBtn = new JButton("I cannot play");
      int btnWidth = 200, btnHeight = 30;
      cantPlayBtn.setBounds(20, 270, btnWidth, btnHeight);
      enableCantPlayButton(false);
      add(cantPlayBtn);
   }

   
   private void initDataMembers(int numCardsPerHand, int numPlayers)
   {
      if (numCardsPerHand < 0)
      {
         numCardsPerHand = 7;
      }
      if (numPlayers <= 0)
      {
         numPlayers = 1;
      }
      this.numPlayers = numPlayers > MAX_PLAYERS ? MAX_PLAYERS : numPlayers;
      this.numCardsPerHand = numCardsPerHand > MAX_CARDS_PER_HAND ? MAX_CARDS_PER_HAND : numCardsPerHand;
   }


   /**
    * Sets the replay button
    */
   private void initReplayButton()
   {
      replayBtn = new JButton("Start Over");
      int btnWidth = 200, btnHeight = 30;
      replayBtn.setBounds((int) replayBtn.CENTER, (int) replayBtn.CENTER, btnWidth, btnHeight);
      add(replayBtn);
      replayBtn.setVisible(false);
      replayBtn.setEnabled(false);
   }

   /**
    * Sets the JFrame layout and title
    */
   private void initFrame(String title)
   {
      final BorderLayout layout = new BorderLayout();
      setLayout(layout);
      setTitle(title);
   }

   /**
    * Set the JPanels
    */
   private void initPanels()
   {
      setupPanel(pnlComputerHand = new JPanel(), "Computer Hand", 0, 0, 100);
      add(pnlComputerHand, BorderLayout.NORTH);
      setupPanel(pnlPlayArea = new JPanel(), "Playing Area", 0, pnlComputerHand.getHeight(), 100);
      pnlPlayArea.setLayout(new GridLayout(1, 2));
      add(pnlPlayArea, BorderLayout.CENTER);
      setupPanel(pnlHumanHand = new JPanel(), "Your Hand", 0, pnlComputerHand.getHeight() + pnlPlayArea.getHeight(), 100);
      add(pnlHumanHand, BorderLayout.SOUTH);
      pnlDashboard = new JPanel();
      add(pnlDashboard, BorderLayout.WEST);
   }

   private void setupPanel(JPanel panel, String title, int x, int y, int minHeight)
   {
      // Create Title Border With Title at top left
      final TitledBorder border = new TitledBorder(title);
      border.setTitleJustification(TitledBorder.LEFT);
      border.setTitlePosition(TitledBorder.TOP);

      // Initialize JPanel
      panel.setBorder(border);
      panel.setLocation(x, y);
      panel.setMinimumSize(new Dimension((int) panel.getSize().getWidth(), minHeight));
      panel.setEnabled(true);
      panel.setVisible(true);
   }
}

class GUICard 
{

   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   static final char[] cardsValues = new char[]{'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   static final char[] cardsSuits = new char[] {'C', 'D', 'H', 'S'};
   static final String cardBack = "BK";

   static void loadCardIcons() 
   {
      if (iconsLoaded) return;
      for (int k = 0; k < cardsValues.length; k++) 
      {
         for (int j = 0; j < cardsSuits.length; j++)
            iconCards[k][j] = new ImageIcon(String.format("images/%s%s.gif", turnIntIntoCardValue(k), turnIntIntoCardSuit(j)));
      }
      iconBack = new ImageIcon(String.format("images/%s.gif", cardBack));
      iconsLoaded = true;
   }

   static public Icon getIcon(Card card) 
   {
      loadCardIcons();
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   static public Icon getBackCardIcon()
   {
      loadCardIcons();
      return iconBack;
   }

   static char turnIntIntoCardValue(int k) 
   {
      return cardsValues[k];
   }

   static char turnIntIntoCardSuit(int j) 
   {
      return cardsSuits[j];
   }

   private static int valueAsInt(Card card) 
   {
      for (int i = 0; i < cardsValues.length; i++) 
      {
         if (cardsValues[i] == card.getValue())
            return i;
      }
      return -1;
   }

   private static int suitAsInt(Card card) 
   {
      for (int i = 0; i < cardsSuits.length; i++) 
      {
         if (card.getSuit().toString().charAt(0) == cardsSuits[i])
            return i;
      }
      return -1;
   }
}

class GameControl
{
   private static String LEFT_STACK = "Stack One";
   private static String RIGHT_STACK = "Stack Two";
   private static final int NUM_CARDS_PER_HAND = 7;
   private static final int NUM_PLAYERS = 2;
   private static int PLAYER_INDEX = 0;
   private static int CPU_INDEX = 1;
   private boolean computerLockout = false;
   private boolean playerLockout = false;
   private CPU computer;
   private CardGameModel model;
   private CardGameView view;
   private Timer timer;
   private TimerDisplay timerView;
   
   /**
    * Default Constructor
    */
   public GameControl()
   {
      init();
   }
   
   /**
    * Begin the game logic
    */
   public void startGame()
   {
      model.resetScores();
      view.enableReplayButton(false);
      view.enableCantPlayButton(false);
      model.newGame(); // begin new game
      if (!model.deal()) // deal the cards
      {
         displayError("There was an error dealing cards. Closing game.");
         System.exit(11); // exit game
      }
      model.sortHands();
      addLabelsToPanels();
      initPlayArea();
      view.replayButtonAction(this::startGame);
      view.cantPlayButtonAction(this::cantPlayButtonAction);
      view.setVisible(true);
      JOptionPane.showMessageDialog(view, "Click and Drag your card to Play!");

   }
   
   /**
    * Method that initiates cant play button actions and increases the players score 
    */
   private void cantPlayButtonAction()
   {
      model.increaseScoreBy(PLAYER_INDEX, 1);
      afterPlayerTurn();
      if (playerHasNoMoves())
      {
         JOptionPane.showMessageDialog(view, "You have no legal moves!");
      }
      playerLockout = computerLockout = false;
   }
   
   /**
    * Method that checks for a card clicked event
    * 
    */
   private void cardClickedEvent(MouseEvent e)
   {
      if (playerTurnTaken(e))
      {
         afterPlayerTurn();
         if (playerHasNoMoves())
         {
            JOptionPane.showMessageDialog(view, "You have no legal moves!");
         }
      }
      else
      {
         JOptionPane.showMessageDialog(view, "You have no legal moves!");
      }
   }
   
   /**
    * Helper method to handle logic after a player turn
    */
   private void afterPlayerTurn()
   {
      computerTurn();
      if (playerHasNoMoves()) // If player can't go, check endgame conditions
      {
         if (isEndGame())
         {
            return;
         }
      }
      else if (computerLockout)// otherwise display computer has no card
      {
         displayError("Computer had no card to play");
      }
      if (playerLockout && computerLockout)
      {
         JOptionPane.showMessageDialog(view, "Everyone is out of moves!");
         view.enableCantPlayButton(false);
         final Card pileOneCard = model.getCardFromDeck();
         if (isEndGame())
         {
            return;
         }
         final Card pileTwoCard = model.getCardFromDeck();
         if (isEndGame())
         {
            return;
         }
         updateModelAndViewPlayArea(pileOneCard, 0);
         updateModelAndViewPlayArea(pileTwoCard, 1);
      }
      isEndGame();
      draw();
   }
   
   private void updateModelAndViewPlayArea(final Card card, int atIndex)
   {
      model.placeCardInPlayArea(card, atIndex);
      ((JLabel) view.pnlPlayArea.getComponent(atIndex)).setIcon(GUICard.getIcon(card));
      draw();
   }
   
   /**
    * Private helper method to check if it is the end of the game
    * @return boolean value for end of game status
    */
   private boolean isEndGame()
   {
      if (model.getNumCardsRemainingInDeck() < 0) // no more cards, reset game
      {
         view.enableReplayButton(true);
         final String message = model.getPlayerScore(PLAYER_INDEX) <= model.getPlayerScore(CPU_INDEX) ? "Player wins the match" : "Computer wins the match";
         JOptionPane.showMessageDialog(view, message);
         return true;
      }
      return false;
   }
   
   /**
    * Private Helper method for logic during a players turn
    * @param e Mouse event
    * @return boolean if the player could move
    */
   private boolean playerTurnTaken(MouseEvent e)
   {
      if (playerHasNoMoves())
      {
         return false;
      }
      // find out which play area stack the player dropped card onto
      for (int i = 0; i < view.pnlPlayArea.getComponents().length; i++)
      {
         if (isCollided(e, view.pnlPlayArea.getComponent(i)))
         {
            final Card sourceCard = getSourceCard(e);
            final Card targetCard = model.getTopCardAtPlayAreaIndex(i);
            // This will update the cards and should only happen on legal moves
            if (targetCard == null || targetCard.getErrorFlag() || isLegalMove(sourceCard, targetCard))
            {
               updateModelAndViewPlayArea(sourceCard, i);
               view.pnlHumanHand.remove(e.getComponent());
               // retrieve card from deck and update model/view
               final Card playerDraws = model.getCardFromDeck();
               model.getHand(PLAYER_INDEX).takeCard(playerDraws);
               view.pnlHumanHand.add(generatePlayerLabel(playerDraws)); // setup new label
               return true;
            }
         }
      }
      return false;
   }
   
   /**
    * Private helper method for logic for computer's turn
    */
   private void computerTurn()
   {
      if (!updateComputer()) //returns if computer has a valid card to play
      {
         computerLockout = true;
         model.increaseScoreBy(CPU_INDEX, 1);
      }
   }
   
   /**
    * Private accessor method to determine the card the user is dragging
    * @param e Mouse event
    * @return Card the user is dragging
    */
   private Card getSourceCard(MouseEvent e)
   {
      Card card = null;
      final String clickedName = e.getComponent().getName();
      // Identifies name of clicked JLabel and finds associated card in player deck and plays
      for (int i = 0; i < model.getHand(PLAYER_INDEX).getNumCards(); i++)
      {
         if (model.getHand(PLAYER_INDEX).inspectCard(i).toString().equals(clickedName))
         {
            card = model.getHand(PLAYER_INDEX).playCard(i);
         }
      }
      return card;
   }
   
   
   /**
    * Private helper method to determine the player has no moves
    * @return boolean of the status of the player having any moves
    */
   private boolean playerHasNoMoves()
   {
      // if either stack is empty, return false
      if (model.numCardsInPlayArea(0) == 0 || model.numCardsInPlayArea(1) == 0)
      {
         view.enableCantPlayButton(false);
         return false;
      }
      for (int i = 0; i < model.getHand(PLAYER_INDEX).getNumCards(); i++)
      {
         final Card potCard = model.getHand(PLAYER_INDEX).inspectCard(i);
         for (int j = 0; j < model.numCardsInPlayArea(0); j++)
         {
            final Card potTarget = model.getTopCardAtPlayAreaIndex(0);
            if (potTarget.getErrorFlag() || isLegalMove(potCard, potTarget))
            {
               view.enableCantPlayButton(false);
               return playerLockout = false;
            }
         }
         for (int j = 0; j < model.numCardsInPlayArea(1); j++)
         {
            final Card potTarget = model.getTopCardAtPlayAreaIndex(1);
            if (potTarget.getErrorFlag() || isLegalMove(potCard, potTarget))
            {
               view.enableCantPlayButton(false);
               return playerLockout = false;
            }
         }
      }
      view.enableCantPlayButton(true);
      draw();
      return playerLockout = true;
   }
   
   /**
    * Private helper method to determenine if its a legal move
    * @param source Card object
    * @param target Card object
    * @return boolean returning a valid move
    */
   private boolean isLegalMove(Card source, Card target)
   {
      int sourceValue = Card.cardValue(source);
      int targetValue = Card.cardValue(target);
      return (!source.getErrorFlag() && !target.getErrorFlag()) && (sourceValue == (targetValue + 1)) || (sourceValue == (targetValue - 1));
   }
   
   /**
    * Private helper method to check if the card collided with the card stack
    * @param e Mouse event
    * @param c Component
    * @return boolean of status of collision
    */
   private boolean isCollided(MouseEvent e, Component c)
   {
      double ex = e.getLocationOnScreen().getX();
      double ey = e.getLocationOnScreen().getY();
      double cx = c.getLocationOnScreen().getX();
      double cy = c.getLocationOnScreen().getY();
      return ex >= c.getX() && ex < (cx + c.getWidth()) && ey >= ey && ey < (cy + c.getHeight());
   }
   
   /**
    * Update the view after the computer's turn
    */
   private boolean updateComputer()
   {
      // wait for update
      final Card card = computer.takeTurn(); //returns a card the computer can play
      if (card == null || card.getErrorFlag())
      {
         return false;
      }
      updateModelAndViewPlayArea(card, computer.isPlayedRight() ? 1 : 0);
      final Component component = view.pnlComputerHand.getComponent(0);
      // remove card back from computer hand
      if (component != null)
      {
         view.pnlComputerHand.remove(component);
      }
      final Card computerDraws = model.getCardFromDeck();
      // if still cards in deck, update the view and model
      if (model.getNumCardsRemainingInDeck() > 0)
      {
         model.getHand(CPU_INDEX).takeCard(computerDraws);
         view.pnlComputerHand.add(new JLabel(GUICard.getBackCardIcon()));
      }
      return true;
   }
   
   private void draw()
   {
      for (int i = 0; i < view.pnlPlayArea.getComponentCount(); i++)
      {
         ((JLabel) view.pnlPlayArea.getComponent(i)).setTransferHandler(new TransferHandler("icon"));
      }
      view.pnlPlayArea.validate();
      view.pnlPlayArea.repaint();
      view.validate();
      view.repaint();
   }
   
   private JLabel createJLabel(String labelText)
   {
      final JLabel label = new JLabel(labelText, JLabel.CENTER);
      label.setIcon(GUICard.getBackCardIcon());
      label.setVerticalTextPosition(JLabel.BOTTOM);
      label.setHorizontalTextPosition(JLabel.CENTER);
      return label;
   }
   
  /**
   * Initializes the play area
   */
   private void initPlayArea()
   {
      for (Component component : view.pnlPlayArea.getComponents())
      {
         if (component instanceof JLabel)
         {
            ((JLabel) component).setIcon(GUICard.getBackCardIcon());
         }
      }
   }
   
   /**
    * Initialize the game Model
    */
   private void initModel()
   {
      model = new CardGameModel(1, 0, 0,
              null, NUM_PLAYERS, NUM_CARDS_PER_HAND, 2);
   }
   
   /**
    * Initialize the View
    */
   private void initView()
   {
      view = new CardGameView("CardGameView", NUM_CARDS_PER_HAND, NUM_PLAYERS, 800, 600);
      view.setLocationRelativeTo(null);
      view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }
   
   /**
    * Initialize the timer
    */
   private void initTimer()
   {
      timerView = new TimerDisplay();
      timer = new Timer(timerView);
      view.pnlDashboard.add(timerView);
      view.pnlDashboard.validate();

      // Event handler for timer button
      timerView.startStopButton.addActionListener(e ->
      {
         if (timer.timerOn())
         {
            timer.stopTimer();
            timerView.startStopButton.setText("Start");
            timerView.startStopButton.validate();
         }
         else
         {
            timer.resumeTimer();
            timerView.startStopButton.setText("Stop");
            timerView.startStopButton.validate();
         }
      });

      timer.start();
   }
   
   private void addLabelsToPanels()
   {
      // remove all old icons from table
      view.pnlHumanHand.removeAll();
      view.pnlComputerHand.removeAll();
      // add new ones
      for (int i = 0; i < model.getNumCardsPerHand(); i++)
      {
         final Card playerCard = model.getHand(PLAYER_INDEX).inspectCard(i);
         view.pnlHumanHand.add(generatePlayerLabel(playerCard));
         view.pnlComputerHand.add(new JLabel(GUICard.getBackCardIcon()));
      }
      if (view.pnlPlayArea.getComponents().length <= 0)
      {
         view.pnlPlayArea.add(createJLabel(LEFT_STACK));
         view.pnlPlayArea.add(createJLabel(RIGHT_STACK));
      }
      draw();
   }
   
   private void displayError(String errorMessage)
   {
      JOptionPane.showMessageDialog(view, errorMessage);
   }
   
   private void init()
   {
      initModel(); // setup the card game framework
      initView(); // setup the card table
      computer = new CPU(CPU_INDEX, model);
      initTimer();
   }
   
   private JLabel generatePlayerLabel(Card card)
   {
      JLabel label = new JLabel(GUICard.getIcon(card));
      label.setName(card.toString());
      final MouseInputAdapter adapter = generateAdapter();
      label.addMouseListener(adapter);
      label.addMouseMotionListener(adapter);
      label.setTransferHandler(new TransferHandler("icon"));
      return label;
   }
   
   private MouseInputAdapter generateAdapter()
   {
      return new MouseInputAdapter()
      {
         /**
          * Updates drag and drop action of the mouse for the player
          */
         public synchronized void mouseReleased(MouseEvent e)
         {
            cardClickedEvent(e);
            playerLockout = computerLockout = false;
         }
      };
   }
   
}

class CPU
{
   private boolean playedRight;
   private int handNumber;
   private CardGameModel modelRef;

   /**
    * CPU constructor
    * @param handNumber
    * @param modelRef
    */
   public CPU(int handNumber, CardGameModel modelRef)
   {
      this.handNumber = handNumber;
      this.modelRef = modelRef;
   }

   /**
    * Updates the CPU model
    * @return The card the CPU decides to play
    */
   public Card takeTurn()
   {
      if (modelRef.getHand(handNumber).getNumCards() == 0)
      {
         return null;
      }
      final Hand hand = modelRef.getHand(handNumber); // cache local hand reference
      final Card rightCard = modelRef.getTopCardAtPlayAreaIndex(1);
      final Card leftCard = modelRef.getTopCardAtPlayAreaIndex(0);
      if (rightCard.getErrorFlag())
      {
         playedRight = true;
         System.out.println("The right stack is empty");
         return hand.playCard();
      }
      if (leftCard.getErrorFlag())
      {
         playedRight = false;
         System.out.println("The left stack is empty");
         return hand.playCard();
      }

      hand.sort(); // sort computer's hand
      Card computersPick = null;
      int leftValue = Card.cardValue(leftCard); //gets value of top card of left stack
      int rightValue = Card.cardValue(rightCard); //gets value of top card of right stack

      // find a card that can beat the card in play
      for (int i = 0; i < hand.getNumCards(); i++)
      {
         int computerCard = Card.cardValue((hand.inspectCard(i)));
         if (computerCard == leftValue + 1 || computerCard == leftValue - 1)
         {
            playedRight = false;
            computersPick = hand.playCard(i);
            break;
         }
         else if (computerCard == rightValue + 1 || computerCard == rightValue - 1)
         {
            playedRight = true;
            computersPick = hand.playCard(i);
            break;
         }
      }
      return computersPick;
   }

   /**
    * Determines if the CPU played from the right Stack
    * @return boolean of status of playing from right stack
    */
   public boolean isPlayedRight()
   {
      return playedRight;
   }
}





