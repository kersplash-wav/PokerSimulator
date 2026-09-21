/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cardsimulator;

import static cardsimulator.Card.valueNames;
import static cardsimulator.CardSimulator.displays;
import static cardsimulator.CardSimulator.handDict;

import java.awt.GraphicsDevice;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Bread
 */
public final class Deck 
{
    public static final String[] rankIndexName = {"Royal Flush", "Straight Flush", "Four of a Kind", "Full House", "Flush", "Straight", "Three of a Kind", "Two Pairs", "One Pair", "High Card"};
    public static Hand[] hands = new Hand[CardSimulator.displays.length];
    public static Card[] inventory = new Card[52];
    
    public static int playerAmount = 1;
    public static int cardsLeft()
    {
        // Find last index!
        for (int i = inventory.length - 1; i >= 0; i--)
        {
            Card currentCard = inventory[i];
            // Conditions //
            if (currentCard == null)
                continue;
            // Settings //
            return i + 1;
        }
        // None //
        return 0;
    }
    
    public static Card drawCard()
    {
        for (int i = inventory.length - 1; i >= 0; i--)
        {
            // Card //
            Card drawnCard = inventory[i];
            // Conditions //
            if (drawnCard == null)
                continue;
            // Move Card //
            inventory[i] = null;
            // Debug //
            //System.out.println("Drawn: " + drawnCard.value + "_of_" + drawnCard.suit + "!");
            // Return Card //
            //System.out.println("Cards remaining in deck: " + cardsLeft());
            return drawnCard;
        }
        return null;
    }
    
    public static Card drawCard(int value, String suit)
    {
        // Search //
        for (int i = 0; i < inventory.length; i++)
        {
            // Card //
            Card drawnCard = inventory[i];
            // Conditions //
            if (drawnCard == null) 
                return null;

            if (value != drawnCard.cardIndex)
                continue;

            if (suit != drawnCard.suit)
                continue;
            // Draw //
            inventory[i] = null;
            // Debug //
            //System.out.println("Drawn: " + drawnCard.value + "_of_" + drawnCard.suit + "!");
            // Return Card //
            //System.out.println("Cards remaining in deck: " + cardsLeft());
            // Return Card //
            return drawnCard;
        }
        // Fail //
        return null;
    }

    public static void shuffleCards()
    {
        int cardAmount = 0;
        // Get Card Amount //
        for (int i = inventory.length - 1; i >= 0; i--)
        {
            // Card //
            Card currentCard = inventory[i];
            // Conditions //
            if (currentCard == null)
                continue;
            // Get Amount //
            cardAmount = i + 1;
            // End //
            break;
        }
        
        // Inventory //
        for (int i = 0; i < cardAmount; i++)
        {
            // Index //
            int randomCardIndex = (int)(Math.random() * cardAmount);
            // Data //
            Card currentCard = inventory[i];
            Card randomCard = inventory[randomCardIndex];
            // 
            inventory[i] = randomCard;
            inventory[randomCardIndex] = currentCard;
        }
    }
    
    public static void makeCards()
    {
        for (int x = 0; x < Card.possibleRange; x++)
        {
            for (int y = 0; y < Card.possibleSuits.length; y++)
            {
                try 
                {
                    Card newCard = new Card(x, Card.possibleSuits[y]);
                    inventory[x * 4 + y] = newCard;
                    newCard.xPos = 500;
                    newCard.yPos = 500;
                }
                catch(Exception _) {}
            }
        }
    }
    
    public static void addHand(GraphicsDevice display)
    {
        // Init //
        Hand newHand = new Hand(display);
        newHand.drawCard(5);
        // Store Hand //
        handDict.put(display, newHand);
        for (int i = 0; i < hands.length; i++)
        {
            // Conditions //
            if (hands[i] != null)
                continue;
            // Settings //
            hands[i] = newHand;
            // Reset //
            break;
        }
        // Debug //
        //System.out.println(newHand.getPairAmount());
    }
    
    public static void addHand()
    {
        // Init //
        Hand newHand = new Hand();
        newHand.drawCard(5);
        // Store Hand //
        for (int i = 0; i < hands.length; i++)
        {
            // Conditions //
            if (hands[i] != null)
                continue;
            // Settings //
            hands[i] = newHand;
            // Reset //
            break;
        }
        // Debug //
        //System.out.println(newHand.getPairAmount());
    }
}
