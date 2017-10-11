/*
 * Jospeh Cortez
 * Lyndsay Hackett
 * Mohklis Awad
 * Ahdia Fuller
 * 
 */

import javax.swing.*;
import java.util.Random;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

   private char value;
   private Suit suit;
   private boolean errorFlag;

   public static char[] valueRanks = new char[]{'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};

   // Constructor
   public Card() 
   {
      set('A', Suit.Spades);
   }

   // Constructor
   public Card(char value, Suit suit) 
   {
      set(value, suit);
   }
   
   // Copy Constructor
   public Card(final Card originalCard)
   {
      value = originalCard.value;
      suit = originalCard.suit;
      errorFlag = originalCard.errorFlag;
   }

   // Method to get String representation of Card
   public String toString() 
   {
      if (errorFlag) 
      {
          return "** illegal **";
      } 
      else 
      {
          return value + " of " + suit;
      }
   }

   // Method to set value and suit of Card
   public void set(char value, Suit suit) 
   {
      this.value = value;
      this.suit = suit;
      this.errorFlag = !isValid(value, suit);
   }

   // Method to get value of Card
   public char getValue() 
   {
     return value;
   }

   // Method to get Suit of Card
   public Suit getSuit() 
   {
      return suit;
   }

   // Method to get errorFlag of Card
   public boolean getErrorFlag() 
   {
      return errorFlag;
   }

   // Method to check equality of two Card objects
   public boolean equals(Card card) 
   {
      return (getValue() == card.getValue() && getSuit() == card.getSuit());
   }

   // Method to check if value and suit are valid
   private boolean isValid(char value, Suit suit) 
   {
      String values = "A23456789TJQKX";
      return (values.indexOf(value) != -1);
   }

   static void arraySort(Card[] cards, int arraySize) 
   {
      Card temp;
      //track if changes were made during iteration
      boolean changesMade;
      for (int i = 0; i < arraySize; i++) 
      {
          changesMade = false;
          for (int j = 1; j < arraySize; j++) 
          {
              //go through the elements of valueRank array
              //and compare every two cards to every one
              for (char valueRank : valueRanks) 
              {
                  //if left card's rank is found - this two cards
                  //are in correct position
                  if (cards[j-1].getValue() == valueRank)
                      break;
                  //if right card's rank is found - swap tho cards
                  if (cards[j].getValue() == valueRank) 
                  {
                      temp = cards[j];
                      cards[j] = cards[j-1];
                      cards[j-1] = temp;
                      changesMade = true;
                  }
              }
          }
          //if changes were not made - finish sorting
          if (!changesMade) break;
      }
   
   }
   
   public static int cardValue(final Card card)
   {
      return Card.valueRanks.length - new String(valueRanks).indexOf(card.getValue());
   }

   // Main method which executes test code for Card
   public static void main(String[] args) 
   {
      Card card1 = new Card('A', Suit.Spades);
      Card card2 = new Card('T', Suit.Clubs);
      Card card3 = new Card('Z', Suit.Hearts);
      System.out.println(card1);
      System.out.println(card2);
      System.out.println(card3);

      System.out.println();

      card2.set('U', Suit.Clubs);
      card3.set('5', Suit.Hearts);
      System.out.println(card1);
      System.out.println(card2);
      System.out.println(card3);
   }
 
}

enum Suit 
{
   Spades, Hearts, Diamonds, Clubs
}

class Deck 
{

   public final int MAX_CARDS = 6*52;
   
   private static Card[] masterPack;
   
   private Card[] cards;
   private int topCard;
   private int numPacks;
   
   // Constructor
   public Deck() 
   {
      init(1);
   }
   
   // Constructor
   public Deck(int numPacks) 
   {
      init(numPacks);
   }
   
   // Method to initialize Deck
   public void init(int numPacks) 
   {
      allocateMasterPack();
      this.numPacks = numPacks;
      cards = new Card[56*numPacks];
      int i = 0;
      for (int j = 0; j < numPacks; j++) 
      {
          for (int k = 0; k < 56; k++) 
          {
             cards[i++] = masterPack[k];
          }
      }
      this.topCard = 56 * numPacks - 1;
   }
   
   // Method to shuffle Deck
   public void shuffle() 
   {
      for (int i = 0; i < cards.length; i++) 
      {
          Card original = cards[i];
          int j = (int)(Math.random() * cards.length);
          cards[i] = cards[j];
          cards[j] = original;
      }
   }
   
   // Method to deal Card from Deck
   public Card dealCard() 
   {
      if (topCard >= 0) 
      {
          return cards[topCard--];
      } else 
      {
          return null;
      }
   }
   
   // Method to get index of top Card
   public int getTopCard() 
   {
      return topCard;
   }
   
   // Method to inspect Card
   public Card inspectCard(int k) 
   {
      if (k < 0 || k >= topCard) 
      {
          return new Card('0', Suit.Spades);
      } 
      else 
      {
          return cards[k];
      }
   }
   
   public boolean addCard(Card card) 
   {
      if (cards.length > topCard) 
      {
          cards[++topCard] = card;
          return true;
      }
      return false;
   }
   
   public boolean removeCard(Card card) 
   {
      for (int i = 0; i < cards.length; i++) 
      {
          if (cards[i].equals(card)) 
          {
              cards[i] = cards[topCard--];
              return true;
          }
      }
      return false;
   }
   
   public void sort() 
   {
      Card.arraySort(cards, topCard + 1);
   }
   
   public int getNumCards() 
   {
      return topCard + 1;
   }
   
   // Method to allocate Master Deck
   private static void allocateMasterPack() 
   {
      if (masterPack == null) 
      {
          masterPack = new Card[56];
          Suit[] suits = {Suit.Clubs, Suit.Diamonds, Suit.Hearts, Suit.Spades};
          String values = "A23456789TJQKX";
          int i = 0;
          for (Suit suit: suits) 
          {
              for (char value: values.toCharArray()) 
              {
                  Card card = new Card(value, suit);
                  masterPack[i++] = card;
              }
          }
      }
   }
   
   // Main method which executes test code for Deck
   public static void main(String[] args) 
   {
      System.out.println("Deck of 2 packs of cards:");
      Deck deck = new Deck(2);
      System.out.println("Dealing all unshuffled cards");
      while (deck.getTopCard() >= 0) 
      {
          Card card = deck.dealCard();
          System.out.print(card + " / ");
      }
      System.out.println();
      deck = new Deck(2);
      deck.shuffle();
      System.out.println("Dealing all SHUFFLED cards");
      while (deck.getTopCard() >= 0) 
      {
         Card card = deck.dealCard();
         System.out.print(card + " / ");
      }
      System.out.println("\n");
      System.out.println("Deck of 1 pack of cards:");
      deck = new Deck(1);
      System.out.println("Dealing all unshuffled cards");
      while (deck.getTopCard() >= 0)
      {
          Card card = deck.dealCard();
          System.out.print(card + " / ");
      }
      System.out.println();
      deck = new Deck(1);
      deck.shuffle();
      System.out.println("Dealing all SHUFFLED cards");
      while (deck.getTopCard() >= 0) 
      {
          Card card = deck.dealCard();
          System.out.print(card + " / ");
      }
      System.out.println();      
  }
 
}

class Hand 
{
   
   public static final int MAX_CARDS = 50;
   
   private Card[] myCards;
   private int numCards;
   
   // Constructor
   public Hand() 
   {
      resetHand();
   }
   
   // Method to reset Hand
   public void resetHand() 
   {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }
   
   // Method to take Card and add to Hand
   public boolean takeCard(Card card) 
   {
      if (numCards < MAX_CARDS) 
      {
         myCards[numCards++] = card;
         return true;
      } 
      else 
      {
         return false;
      }
   }
   
   public Card playCard()
   {
      if(numCards <= 0)
      {
         return new Card('?', Suit.Diamonds);
      }
      Card card = new Card(myCards[--numCards]); // cache temp location of top card deep copy
      myCards[numCards] = null; // implicitly call destructor by re-assigning to null
      return card; // return deep copy
   }
   
   // Method to play Card from Hand
   public Card playCard(int index) {
      if(index < 0 || index > numCards)
      {
         return new Card('?', Suit.Diamonds);
      }
      Card card = new Card(myCards[index]); // cache temp location of desired card deep copy

      for(int i = index; i < myCards.length - 1; i++)
      {
         myCards[i] = myCards[i + 1];
      }
      myCards[numCards--] = null; // empty top card spot
      return card; // return deep copy
   }
   
   // Method to get String representation of Hand
   public String toString() 
   {
      String result = "Hand = ( ";
      if (numCards > 0) 
      {
         for (int i = 0; i < numCards; i++) 
         {
            result += myCards[i].toString() + ", ";
         }
         result = result.substring(0,result.length()-2);
      }
      result += " )";
      return result;
   }
   
   // Method to get number of cards in Hand
   public int getNumCards() 
   {
      return numCards;
   }
   
   // Method to inspect Card in Hand
   public Card inspectCard(int k) 
   {
      if (k < 0 || k >= numCards) 
      {
         return new Card('0', Suit.Spades);
      } 
      else 
      {
         return myCards[k];
      }
   }

   void sort() 
   {
      Card.arraySort(myCards, numCards);
   }

   // Main method which executes test code for Hand
   public static void main(String[] args) 
   {
      Card[] cards = new Card[3];
      cards[0] = new Card('3', Suit.Clubs);
      cards[1] = new Card('T', Suit.Clubs);
      cards[2] = new Card('9', Suit.Hearts);
      Hand hand = new Hand();
      int i = 0;
      while (hand.getNumCards() < Hand.MAX_CARDS) 
      {
          hand.takeCard(cards[i % 3]);
          i++;
      }
      System.out.println("After deal");
      System.out.println(hand);
      System.out.println();
      System.out.println("Testing inspectCard()");
      System.out.println(hand.inspectCard(Hand.MAX_CARDS-1));
      System.out.println(hand.inspectCard(Hand.MAX_CARDS));
      System.out.println();
      while (hand.getNumCards() > 0) 
      {
          Card card = hand.playCard(0);
          System.out.println("Playing " + card);
      }
      System.out.println();
      System.out.println("After playing all cards");
      System.out.println(hand);
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
    if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
    {
       numUnusedCardsPerPack = 0;
    }
    if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
    {
       numPlayers = 4;
    }
    // one of many ways to assure at least one full deal to all players
    if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 + numJokersPerPack - numUnusedCardsPerPack) / numPlayers)
    {
       numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
    }

    scores = new int[numPlayers]; // init container for player scores


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
          deck.addCard(new Card('X', Suit.values()[j]));
       }
    }
    // shuffle the cards
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
       return card == null ? new Card('?', Suit.Spades) : card;
    }
    else
    {
       return new Card('?', Suit.Spades);
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
            printTime();
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
   private void printTime()
   {
      System.out.printf(toString());
   }

   /*
   Private helper method pauses for waitTime miliseconds
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
 * View methods to intergrate Timer class into GUI interface
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
      timerText.setForeground(Color.GREEN);
      timerText.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
      add(timerText);
   }
}




