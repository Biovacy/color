import java.util.concurrent.ThreadLocalRandom;

public class nodeFunctions {

    // Constants
    private final static double e = Math.E;

    // Functions
    public static double sigmoid(double input){
        /*
            Mathmatical function to convert a large negative or positive
            number to a number between 0 and 1.

                         1
            f(x) = --------------
                     1 + e ^ -x
        */

        double sigmoid = 1 / ( 1 + Math.pow(e, -input));

        return sigmoid;
    }

    public static double getRandom(double min, double max) {
        double randomNum = ThreadLocalRandom.current().nextDouble(min, max);
        return randomNum;
    }

}
