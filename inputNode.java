public class inputNode {
    /*
        Network layout:
            Input  = Single int     (Color value)
            Output = Single double  (Node output)
    */

    // Variables
    private static double weight;
    private static double bias;

    // nodeFunctions
    private static nodeFunctions nF = new nodeFunctions();

    // Public functions

    public static double evaluate(int colorValue){
        /*
            This function makes this input node evaluating the
            color of the input.
        */

        double nodeResult;

        nodeResult = weight * colorValue + bias;

        nodeResult = nF.sigmoid(nodeResult);

        return nodeResult;
    }

    public static void randomize(double min, double max){
        /*
            This function gives random values to all weights and
            biases in this node. The minimum and maximum values
            can be set.
        */

        weight = nF.getRandom(min, max);

        bias = nF.getRandom(min, max);
    }

}
