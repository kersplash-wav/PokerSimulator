 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cardsimulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author Bread
 */
public class Card 
{
    public static ArrayList<Card> cardList = new ArrayList<Card>();
    public static final String[] possibleSuits = {"hearts", "diamonds", "clubs", "spades"};
    public static final String[] valueSymbols = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; 
    public static final String[] valueNames = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    public static final int possibleRange = valueSymbols.length;
    // Properties //
    public Graphics2D graphics;
    public File physicalFile;
    public boolean hidden = true;
    public int cardIndex;
    // Visuals //
    public Image cardImage;
    public String suit;
    public String value;
    // Coordinates //
    public int xPos;
    public int yPos;
    public static int width = 150;
    public static int height = 200;
    // Constructor //
    public Card(int cardIndex, String suit) throws IOException
    {
        // Data //
        this.suit = suit;
        this.value = valueSymbols[cardIndex];
        String address = "src/CardImages/" + valueNames[cardIndex].toLowerCase() + "_of_" + suit + ".png";
        // Settings //
        this.cardIndex = cardIndex;
        // Debug //
        // System.out.println("Suit: " + suit);
        // Update //
        updateImage();
        // Save //
        cardList.add(this);
    }
    // Update //
    public void Update() throws IOException
    {
        // Conditions //
        if (graphics == null)
            return;
        // Update Visuals //
        updateImage();
        // Create Visual //
        graphics.drawImage(cardImage, xPos - width / 2, yPos - width / 2, width, height, null);
        // System.out.println(this.getImageAddress());
    }
    // Action Functions //
    public void updateImage() throws IOException
    {
        String address = getImageAddress();
        this.cardImage = ImageIO.read(new File(address));
    }
    
    public String getImageAddress()
    {
        if (hidden)
            return "src/CardImages/backside.png";
        else
            return "src/CardImages/" + valueNames[cardIndex].toLowerCase() + "_of_" + suit + ".png";
    }
    // Setup //
    public static Card main() throws IOException
    {
        return new Card((int)(Math.random() * possibleRange), "hearts");
    }
}
