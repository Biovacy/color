public class outputNode {
    /*
        Network layout:
            Input  = two doubles   (hiddenNode evaluations)
            Output = Single double (output)
    */

    // Variables
    private static double weight_1;
    private static double weight_2;

    private static double bias;

    // nodeFunctions
    private static nodeFunctions nF = new nodeFunctions();

    // Public functions
    public static double evaluate( double nodeInput_1,
                            double nodeInput_2 )
    {
        /*
            This function makes this output node evaluating input.
        */

        double nodeResult;

        nodeResult = weight_1 * nodeInput_1 +
                     weight_2 * nodeInput_2 +
                     bias;

        nodeResult = nF.sigmoid(nodeResult);

        return nodeResult;
    }

    public static void randomize(double min, double max){
        /*
            This function gives random values to all weights and
            biases in this node. The minimum and maximum values
            can be set.
        */

        weight_1 = nF.getRandom(min, max);
        weight_2 = nF.getRandom(min, max);

        bias = nF.getRandom(min, max);
    }

}
