package cardsimulator;

public final class Database 
{
    // Base Methods //
    public static int[] generateHands(int length) 
    {
        // Data //
        int[] rankCounter = new int[10];
        // Hand //
        Hand currentHand = Deck.hands[0];
        // Reset //
        currentHand.returnCards();
        // New Cards //
        for (int i = 1; i <= length; i++) {
            // Init //
            Deck.shuffleCards();
            currentHand.drawCard(5);
            Hand.BubbleSort(currentHand.inventory);
            // Get Data //
            int rank = currentHand.handRank();
            // Save //
            rankCounter[rank]++;
            // System.out.println(rank);
            // Reset //
            currentHand.returnCards();
        }
        // Display //
        for (int i = 0; i < rankCounter.length; i++) {
            int rankCount = rankCounter[i];

            // System.out.println(i + ": " + rankCount);
        }
        // Return Information //
        return rankCounter;
    }
}
