import java.util.concurrent.ThreadLocalRandom;

public class networkInterface {
    /*
        This class functions as the interface for the network.
        It has the functions:
            > Operating the network
            > Generate colors
            > Collect data
    */

    // Data variables
    private static int[][]   colors       = new int[0][3];
    private static boolean[] colorRatings = new boolean[0];

    // Network variables
    private final static int networkAmount = 1000000;
    private final static int maxRandom =  10;
    private final static int minRandom = -10;

    private static network[] networks = createNetworks();
    private static network leadingNetwork = networks[0]; // This networks results are presented to the user

    // Manage network

    private static network[] createNetworks() {
        // Create new networks and assign random values to the nodes.

        network[] newNetworks = new network[networkAmount];

        for (int i = 0; i < newNetworks.length; i++) {
            newNetworks[i] = new network();
            newNetworks[i].randomize(minRandom, maxRandom);
        }

        return newNetworks;
    }

    private static network getLeadningNetwork() {
        // Get the best preforming network

        double[] costs = new double[networks.length];
        int indexMinCost = 0;

        for (int i = 0; i < networks.length; i++) {
            costs[i] = networks[i].cost(colors, colorRatings);
        }

        for (int i = 0; i < costs.length; i++) {
            if (costs[i] < costs[indexMinCost]){
                indexMinCost = i;
            }
        }

        return networks[indexMinCost];
    }

    public static void nextGeneration(){
        // Generates the next generation.
        // This evaluates all current networks and the best preforming one
        // is set as leadingNetwork, new networks are also generated
        // to compete against the leadningNetwork.
        // All given data is utilized to test the networks

        network newLeadingNetwork = getLeadningNetwork();

        if (newLeadingNetwork.cost(colors, colorRatings) < leadingNetwork.cost(colors, colorRatings)){
            // If the newLeadingNetwork is better preforming than the last leadningNetwork

            leadingNetwork = newLeadingNetwork;
        }

        System.out.println("New leadingNetwork cost: " + leadingNetwork.cost(colors, colorRatings));
        networks = createNetworks();
    }

    public static double evaluate(int R, int G, int B) {
        // Utilizes the best preforming network to give a
        // prediction whether the user admires the color

        int[] colorValues = {R, G, B};

        double evaluation = leadingNetwork.evaluate(colorValues);

        // Round to two decimals, credit: https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java

        evaluation = (double) Math.round(evaluation * 100d) / 100d;

        return evaluation;
    }

    // Manage data

    public static int[] generateColor(){
        // Generate a random number

        // TODO: Have a smarter generation of color where the next
        // color that is generated is the most

        int min = 0;
        int max = 256;

        int R = ThreadLocalRandom.current().nextInt(min, max);
        int G = ThreadLocalRandom.current().nextInt(min, max);
        int B = ThreadLocalRandom.current().nextInt(min, max);

        int[] returnColor = {R, G, B};

        return returnColor;
    }

    public static void colorDataInsert(int R, int G, int B, boolean like) {
        //System.out.println("R: " + R + " G: " + G + " B: " + B + " Like: " + like);

        colorsAppend(R, G, B);
        colorRatingsAppend(like);

    }

    private static void colorsAppend(int R, int G, int B) {
        // Append a new color to the end of colors array

        int[][] newArray = new int[colors.length + 1][3];

        for (int i = 0; i < colors.length; i++) {

            newArray[i][0] = colors[i][0];
            newArray[i][1] = colors[i][1];
            newArray[i][2] = colors[i][2];

        } // end for

        newArray[colors.length][0] = R;
        newArray[colors.length][1] = G;
        newArray[colors.length][2] = B;

        colors = newArray;
    }

    private static void colorRatingsAppend(boolean like) {
        // Append a new rating to the end of colorRatings array

        boolean[] newArray = new boolean[colorRatings.length + 1];

        for (int i = 0; i < colorRatings.length; i++) {
            newArray[i] = colorRatings[i];
        }

        newArray[colorRatings.length] = like;

        colorRatings = newArray;
    }

    // Print arrays

    private static void printArray(int[][] array){

        for (int x = 0; x < array.length; x++) {

            for (int y = 0; y < array[x].length; y++) {

                System.out.print(array[x][y] + " ");

            } // end for y

            System.out.println();

        } // end for x

        System.out.println();

    }

    private static void printArray(boolean[] array){

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

        System.out.println();

    }

}
