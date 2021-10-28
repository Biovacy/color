public class hiddenNode {
    /*
        Network layout:
            Input  = three doubles  (inputNode evaluations)
            Output = Single double  (Node output)
    */

    // Variables
    private static double weight_1;
    private static double weight_2;
    private static double weight_3;

    private static double bias;

    // nodeFunctions
    private static nodeFunctions nF = new nodeFunctions();

    // Public functions
    public static double evaluate( double nodeInput_1,
                            double nodeInput_2,
                            double nodeInput_3 )
    {
        /*
            This function makes this hidden node evaluating the input.
        */

        double nodeResult;

        nodeResult = weight_1 * nodeInput_1 +
                     weight_2 * nodeInput_2 +
                     weight_3 * nodeInput_3 +
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
        weight_3 = nF.getRandom(min, max);

        bias = nF.getRandom(min, max);
    }

}
