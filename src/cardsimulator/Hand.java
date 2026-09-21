/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cardsimulator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;

/**
 *
 * @author Bread
 */
public class Hand {
    // Properties //
    public Card[] inventory = new Card[5];
    public GraphicsDevice display;

    // Constructor //
    public Hand(GraphicsDevice display) {
        this.display = display;
    }

    public Hand() {}
    // Base Methods //
    public static void BubbleSort(Card[] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array.length - x - 1; y++) {
                // Data //
                int firstValue = array[y].cardIndex;
                int secondValue = array[y + 1].cardIndex;
                Card tempValue = array[y];
                // Conditions //
                if (firstValue <= secondValue)
                    continue;
                // Settings //
                array[y] = array[y + 1];
                array[y + 1] = tempValue;
            }
        }
    }

    /*
     * Could be improved with binary search
     */
    public int cardAmount() {
        // Find Last Card Index //
        for (int i = inventory.length - 1; i >= 0; i--) {
            // Conditions //
            if (inventory[i] != null && i == inventory.length - 1)
                return inventory.length;

            if (inventory[i] != null)
                continue;
            // Returns //
            return i;
        }
        return 0;
    }

    public int getSuitAmount() {
        // Data //
        String[] suitTypes = new String[4];
        int suitAmount = 0;
        // Checks //
        for (int x = 0; x < inventory.length; x++) {
            // Init //
            Card currentCard = inventory[x];
            // Data //
            boolean duplicate = false;
            // Settings //
            for (int y = 0; y < suitTypes.length; y++) {
                String suitType = suitTypes[y];
                // Checks //
                if (currentCard.suit == suitType)
                    duplicate = true;
                // Post-Conditions //
                if (duplicate)
                    break;
            }
            // Post-Conditions //
            if (duplicate)
                continue;
            // Add Suit //
            suitTypes[suitAmount] = currentCard.suit;
            suitAmount++;
        }
        // Return //
        return suitAmount;
    }

    public int getPairAmount() {
        // Data //
        int pairAmount = 0;
        // Check For Pairs //
        for (int x = 0; x < inventory.length - 1; x++) {
            // Init //
            Card currentCard = inventory[x];
            Card nextCard = inventory[x + 1];
            // Conditions //
            if (currentCard.cardIndex != nextCard.cardIndex)
                continue;
            // Checks //
            if (x <= 2 && inventory[x + 2].cardIndex == currentCard.cardIndex)
                x += 2;
            // Success //
            pairAmount++;
            x++;
        }
        // Success //
        return pairAmount;
    }

    public String handName() {
        // Check hand types in order //
        if (isRoyalFlush())
            return "Royal Flush";

        if (isStraightFlush())
            return "Straight Flush";

        if (isFourOfAKind())
            return "Four of a Kind ";

        if (isFullHouse())
            return "Full House";

        if (isFlush())
            return "Flush";

        if (isStraight())
            return "Straight";

        if (isThreeOfAKind())
            return "Three of a Kind";

        if (isTwoPairs())
            return "Two Pairs";

        if (isPair())
            return "One Pair";

        return "High Card";
    }

    public int handRank() {
        if (isRoyalFlush())
            return 0;

        if (isStraightFlush())
            return 1;

        if (isFourOfAKind())
            return 2;

        if (isFullHouse())
            return 3;

        if (isFlush())
            return 4;

        if (isStraight())
            return 5;

        if (isThreeOfAKind())
            return 6;

        if (isTwoPairs())
            return 7;

        if (isPair())
            return 8;

        return 9;
    }

    public Boolean compareHand(Hand opponent) {
        int opponentRank = opponent.handRank();
        int thisRank = handRank();
        // Conditions //
        if (opponentRank != thisRank)
            return thisRank < opponentRank ? Boolean.TRUE : Boolean.FALSE;
        
        return tieBreaker(opponent);
    }

    private Boolean tieBreaker(Hand opponent) 
    {
        Card[] thisTempInventory = sortHandWithHighAce(inventory.clone());
        Card[] opponentTempInventory = sortHandWithHighAce(opponent.inventory.clone());
        int groupSize = thisTempInventory.length > 4 ? 4 : thisTempInventory.length; // get max length of group of
                                                                                     // matching cards
        for (int x = groupSize; x > 0; x--) {
            for (int y = thisTempInventory.length - x; y >= 0; y--) {
                if (thisTempInventory[y].cardIndex == thisTempInventory[y + x - 1].cardIndex) {
                    if (thisTempInventory[y].cardIndex != opponentTempInventory[y].cardIndex)
                        return (thisTempInventory[y].cardIndex+14)%15 > (opponentTempInventory[y].cardIndex+14)%15 ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        
        return null;
    }

    private Card[] sortHandWithHighAce(Card[] cards){
        Card tempCard;
        for(int  x= cards.length>4?4:cards.length; x>=0; x--){
                if(cards[x].cardIndex==0){
                    for(int y = cards.length - 1; y>x; y--){
                        tempCard = cards[y];
                        cards[y]=cards[x];
                        cards[x]=tempCard;
                    }
                }
        }
        return cards;
    }

    private boolean isRoyalFlush() {
        // Conditions //
        if (!isStraightFlush())
            return false;

        if (inventory[0].cardIndex != 0)
            return false;

        if (inventory[4].cardIndex != 12)
            return false;
        // Success //
        return true;
    }

    private boolean isStraightFlush() {
        // Conditions //
        if (!isFlush())
            return false;

        if (!isStraight())
            return false;
        // Success //
        return true;
    }

    private boolean isFlush() {
        return getSuitAmount() == 1;
    }

    private boolean isFourOfAKind() {
        // Conditions //
        if (inventory[0].cardIndex == inventory[3].cardIndex)
            return true;

        if (inventory[1].cardIndex == inventory[4].cardIndex)
            return true;
        // Fail //
        return false;
    }

    private boolean isFullHouse() {
        // Conditions //
        if (!isThreeOfAKind())
            return false;

        if (getPairAmount() != 2)
            return false;

        // Success //
        return true;
    }

    private boolean isStraight() {
        // Loop //
        for (int i = 2; i < inventory.length; i++) {
            // Init //
            Card previousCard = inventory[i - 1];
            Card currentCard = inventory[i];
            // Conditions //
            if (currentCard.cardIndex - previousCard.cardIndex != 1)
                return false;
        }

        // Conditions //
        if (inventory[0].cardIndex == inventory[1].cardIndex - 1)
            return true;

        if (inventory[0].cardIndex == 0 && inventory[4].cardIndex == 12)
            return true;

        // Fail //
        return false;
    }

    private boolean isThreeOfAKind() {
        for (int x = 0; x < inventory.length - 2; x++) {
            if (inventory[x].cardIndex == inventory[x + 1].cardIndex
                    && inventory[x].cardIndex == inventory[x + 2].cardIndex) {
                return true;
            }

        }
        return false;
    }

    private boolean isPair() {
        for (int x = 0; x < inventory.length - 1; x++) {
            if (inventory[x].cardIndex == inventory[x + 1].cardIndex)
                return true;
        }
        return false;
    }

    public int groupOneValue() {
        for (int x = 0; x < inventory.length - 1; x++) {
            if (inventory[x].cardIndex == inventory[x + 1].cardIndex)
                return inventory[x].cardIndex;
        }
        return -1;
    }

    public int groupTwoValue() {
        for (int x = 0; x < inventory.length - 1; x++) {
            if (inventory[x].cardIndex == inventory[x + 1].cardIndex) {
                for (int y = x + 2; y < inventory.length - 1; y++) {
                    if (inventory[y].cardIndex == inventory[y + 1].cardIndex) {
                        return inventory[y].cardIndex;
                    }
                }
                break;
            }
        }
        return -1;
    }

    private boolean isTwoPairs() {
        for (int x = 0; x < inventory.length - 1; x++) {
            if (inventory[x].cardIndex == inventory[x + 1].cardIndex) {
                for (int y = x + 2; y < inventory.length - 1; y++) {
                    if (inventory[y].cardIndex == inventory[y + 1].cardIndex) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    // Action Methods //
    public Card drawCard() {
        // Conditions //
        if (inventory[inventory.length - 1] != null)
            return null;
        // Draw Card //
        Card drawnCard = Deck.drawCard();
        // Place Card in Hand //
        for (int i = 0; i < inventory.length; i++) {
            // Conditions //
            if (inventory[i] != null)
                continue;
            // Settings //
            inventory[i] = drawnCard;
            // Return Card //
            return drawnCard;
        }
        // Card? //
        return drawnCard;
    }

    public Card drawCard(int value, String suit) {
        // Draw Card //
        Card drawnCard = Deck.drawCard(value, suit);
        // Place Card in Hand //
        for (int i = 0; i < inventory.length; i++) {
            // Conditions //
            if (inventory[i] != null)
                continue;
            // Settings //
            inventory[i] = drawnCard;
            // Return Card //
            return drawnCard;
        }
        // Card? //
        return drawnCard;
    }

    public Card[] drawCard(int amount) {
        // Arrays //
        Card[] cardArray = new Card[amount];
        // Draw Cards //
        for (int x = 0; x < amount; x++) {
            Card drawnCard = drawCard();
            cardArray[x] = drawnCard;
        }
        // Return Drawn Cards //
        return cardArray;
    }

    public void returnCards() {
        for (int x = inventory.length - 1; x >= 0; x--) {
            // Card //
            Card currentCard = inventory[x];
            // Find Empty Slot //
            for (int y = Deck.inventory.length - 2; y >= 0; y--) {
                // Conditions //
                if (Deck.inventory[y] == null)
                    continue;
                // Cards //
                Deck.inventory[y + 1] = currentCard;
                // Skip //
                break;
            }
            // Reset //
            inventory[x] = null;
        }
    }

    public void removeCard(int index) {
        inventory[index] = null;

        for (int i = index; i < inventory.length; i++) {
            inventory[i - 1] = inventory[i];
            inventory[i] = null;
        }
    }

    // Visual Functions //
    public void showCards(GraphicsDevice display) {
        // Gui Containers //
        CoreFrame coreFrame = (CoreFrame) CardSimulator.CoreFrames.get(display);
        DrawCanvas drawCanvas = (DrawCanvas) coreFrame.getContentPane();
        // Gui Data //
        Dimension frameSize = drawCanvas.size;
        // Sort Cards //
        BubbleSort(inventory);
        // Conditions //
        if (frameSize == null)
            return;
        // Positioning Data //
        int startingPosX = frameSize.width / 2 - Card.width * ((cardAmount() + 1) / 2);
        for (int i = 0; i < cardAmount(); i++) {
            Card currentCard = inventory[i];
            currentCard.xPos = startingPosX + Card.width * (i + 1);
            currentCard.yPos = this.display == display ? frameSize.height / 4 * 3 : frameSize.height / 4;
            currentCard.hidden = display != this.display;
            try {
                currentCard.Update();
            } catch (IOException ex) {
                Logger.getLogger(Hand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Display Hand Name //
        showHandInfo(display);
    }

    public void showHandInfo(GraphicsDevice display) {
        // Gui Containers //
        CoreFrame coreFrame = (CoreFrame) CardSimulator.CoreFrames.get(display);
        DrawCanvas drawCanvas = (DrawCanvas) coreFrame.getContentPane();
        // Graphics //
        Graphics2D graphics = drawCanvas.graphics;
        // Gui Data //
        Dimension frameSize = drawCanvas.size;
        // Conditions //
        if (frameSize == null)
            return;

        if (this.display != display)
            return;
        // Display //
        int size = 50;
        int gap = 10;
        graphics.setFont(new Font("Calibri", 1, size));
        graphics.setColor(Color.BLACK);
        graphics.drawString("Hand: " + handName(), gap, size + gap);
        graphics.drawString("Suits: " + getSuitAmount(), gap, (size + gap) * 2);
        graphics.drawString("Pairs: " + getPairAmount(), gap, (size + gap) * 3);
        graphics.drawString("High Card: " + (inventory[0].cardIndex == 0 ? 14 : inventory[4].cardIndex + 1), gap,
                (size + gap) * 4);
    }
}