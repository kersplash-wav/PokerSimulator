/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cardsimulator;

import static cardsimulator.CardSimulator.CoreFrames;
import static cardsimulator.CardSimulator.GameThreads;
import static cardsimulator.CardSimulator.displays;
import static cardsimulator.CardSimulator.handDict;

import java.awt.Canvas;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

/**
 *
 * @author Bread
 */

public class GameThread extends Thread implements KeyListener {
    // Data //
    public GraphicsDevice display;
    public CoreFrame coreFrame;
    public Canvas frameCanvas;
    public Color bgColour = Color.gray;

    // Constructor //
    public GameThread(GraphicsDevice display) {
        this.display = display;
        this.start();
    }

    // Override Methods //
    @Override
    public void run() {
        // Init //
        coreFrame = new CoreFrame(display);
        coreFrame.setUndecorated(true);
        coreFrame.setVisible(true);
        coreFrame.addKeyListener(this);
        coreFrame.setExtendedState(MAXIMIZED_BOTH);
        // coreFrame.getContentPane().add(frameCanvas);
        // Finish Rectangle //
        // display.setFullScreenWindow(coreFrame);
        // Finish //
        CoreFrames.put(display, coreFrame);
        Deck.addHand(display);

        while (true) {
            // Thread.sleep(1000);
            coreFrame.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Conditions //
        if (displays[0] != display)
            return;
        // Data //
        int keyCode = e.getKeyCode();
        // Checks //
        switch (keyCode) {
            case VK_ESCAPE: // ESC //
                System.exit(0);
                break;
            case VK_SPACE: // SPACE //
                // Return Cards //
                for (var hand : Deck.hands)
                    hand.returnCards();
                // Shuffle Cards //
                Deck.shuffleCards();
                // Draw Cards //
                for (var hand : Deck.hands)
                    hand.drawCard(5);
                // Reset //
                for (var display : displays) {
                    // Data //
                    GameThread thread = GameThreads.get(display);
                    // Settings //
                    thread.bgColour = Color.gray;
                }
                break;
            case VK_ENTER:
                // Conditions //
                if (displays.length == 1)
                    return;
                // Current Hand //
                //Hand currentHand = handDict.get(display);
                // Compare Hands //
                for (var currentHand : Deck.hands)
                {
                    int losses = 0;
                    int ties = 0;
                    GameThread currentThread = GameThreads.get(currentHand.display);
                    for (var otherHand : Deck.hands)
                    {
                        // Conditions //
                        if (currentHand == otherHand)
                            continue;
                        // Checks //
                        Boolean winner = currentHand.compareHand(otherHand);
                        // Settings //
                        if (winner == null) {
                            ties++;
                        } else if(!winner) {
                            losses++;
                        }
                    }

                    if(losses!=0)
                        currentThread.bgColour = Color.RED;
                    else if (ties == 0)
                        currentThread.bgColour = Color.GREEN;
                    else
                        currentThread.bgColour = Color.GRAY;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("Released!");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // System.out.println("Typed!");
    }
}
