/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cardsimulator;

import static cardsimulator.Card.valueNames;
import static cardsimulator.Card.possibleSuits;
import static cardsimulator.CardSimulator.CoreFrames;
import static cardsimulator.CardSimulator.GameThreads;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Scanner;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;

/**
 *
 * @author: Nicholas Ranin
 * @date: Sep 16, 2024
 * filename: CardSimulator.java
 * @description: Classes Lab
 */

class DrawCanvas extends JPanel {
    // Data //
    private long paintDelta;
    public GraphicsDevice display;
    public Dimension size;
    public Graphics2D graphics;

    // Constructor //
    public DrawCanvas(GraphicsDevice display) {
        this.display = display;
    }

    // Draw Action //
    @Override
    protected void paintComponent(Graphics g) {
        // Graphics //
        graphics = (Graphics2D) g;
        // Debug //
        /*
         * System.out.println("Last Time: " + paintDelta);
         * System.out.println("Current Time : " + System.currentTimeMillis());
         * System.out.println("Difference: " + (System.currentTimeMillis() -
         * paintDelta));
         */
        // Delta Time //
        long deltaTime = System.currentTimeMillis() - paintDelta;
        paintDelta = System.currentTimeMillis();
        // Data //
        this.size = this.getSize();
        // Background //
        CoreFrames.get(display).setBackground(GameThreads.get(display).bgColour);
        // Draw //
        for (Hand currentHand : Deck.hands) {
            // Conditions //
            if (currentHand == null)
                continue;
            // Settings //
            for (Card currentCard : currentHand.inventory) {
                // Settings //
                currentCard.hidden = currentHand.display != this.display;
                currentCard.graphics = graphics;
            }
            // Display //
            currentHand.showCards(display);
        }
    }
}

class CoreFrame extends JFrame {
    public CoreFrame(GraphicsDevice display) {
        super(display.getDefaultConfiguration());
        frameInit();
        this.setContentPane(new DrawCanvas(display));
    }
}

public class CardSimulator {
    // Graphics //
    public static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static final GraphicsDevice[] displays = GE.getScreenDevices();
    public static final Dictionary<GraphicsDevice, CoreFrame> CoreFrames = new Hashtable<>();
    public static final Dictionary<GraphicsDevice, GameThread> GameThreads = new Hashtable<>();
    public static final Dictionary<GraphicsDevice, Hand> handDict = new Hashtable<>();

    // Main Method //
    public static void main(String[] args)
    {
        for (int x = 0; x < Card.possibleRange; x++)
        {
            for (int y = 0; y < Card.possibleSuits.length; y++)
            {
                String address = "src/CardImages/" + valueNames[x].toLowerCase() + "_of_" + possibleSuits[y] + ".png";
                try 
                {
                    ImageIO.read(new File(address));
                }

                catch(Exception _) 
                {
                    System.out.println("Fatal Error! Failed to load card Image!");
                }
            }
        }

        try
        {
            ImageIO.read(new File("src/CardImages/backside.png"));
        }

        catch(Exception _)
        {
            System.out.println("Failed to load backside image!");
        }
        // Initialize Deck! //
        Deck.makeCards();
        Deck.shuffleCards();
        Deck.addHand();
        // Prompt for Test Mode //
        System.out.println("BEGIN!");
        boolean testMode = JOptionPane.showConfirmDialog(null, "Activate Test Mode?", "Test Mode?", 0) < 1;
        // If Testing //
        while (testMode) {
            // Data //
            String userInput = JOptionPane.showInputDialog("Hand Amount: ");
            try {
                int handAmount = Integer.valueOf(userInput);
                int[] results = Database.generateHands(handAmount);
                // Output //
                String message = "";
                // Settings //
                for (int i = 0; i < results.length; i++) {
                    message += String.format("%s: %.5f%%\n", Deck.rankIndexName[i],
                            +100d / (double) handAmount * (double) results[i]);
                }
                // Output //
                int restart = JOptionPane.showConfirmDialog(null, message, "Results", OK_CANCEL_OPTION);
                // Checks //
                if (restart != 0)
                    break;
            }

            catch (Exception _) {
                // Conditions //
                if (userInput == null) // CANCEL! //
                    break;
            }
        }
        // Reset Test //
        Deck.hands[0] = null;
        // TODO code application logic here
        for (GraphicsDevice display : displays) {
            GameThreads.put(display, new GameThread(display));
        }
    }
}