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
 * Game controller class: Holds all logic
 */
