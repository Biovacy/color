public class network {

    // Nodes
    private static inputNode input_1 = new inputNode();
    private static inputNode input_2 = new inputNode();
    private static inputNode input_3 = new inputNode();

    private static hiddenNode hidden_1 = new hiddenNode();
    private static hiddenNode hidden_2 = new hiddenNode();

    private static outputNode output = new outputNode();

    // Public functions

    public static double evaluate(int[] colorValues) {
        /*
            This function evaluates a single color based on the current
            node weights and biases. The function is a number between
            0 and 1 where a higher number indicates that the user
            enjoys the color more.
        */

        double input_1_output = input_1.evaluate(colorValues[0]);
        double input_2_output = input_2.evaluate(colorValues[1]);
        double input_3_output = input_3.evaluate(colorValues[2]);

        double hidden_1_output = hidden_1.evaluate(
            input_1_output,
            input_2_output,
            input_3_output
        );

        double hidden_2_output = hidden_2.evaluate(
            input_1_output,
            input_2_output,
            input_3_output
        );

        double output_output = output.evaluate(
            hidden_1_output,
            hidden_2_output
        );

        return output_output;
    }

    public static double cost(int[][] colorValues, boolean[] correctAnswers) {
        /*

            The cost function evaluates how well the network is preforming.
            The lower the cost the better the network is preforming. To
            calculate the cost function of the network multiple inputs
            and their corresponding answer has to be given.

            The cost of a single input is calculated as:

                (NO - EO) ^ 2

            Where:
                NO = Network Output
                EO = Expected Output (Correct answer)

            The sum of all costs calculated for each input is the total
            network cost. This is shown below

                cost = Sum( (NO - EO) ^ 2 )
                <=>
                cost = (NO - EO) ^ 2 +
                       (NO - EO) ^ 2 +
                       (NO - EO) ^ 2 +
                       [...] +
                       (NO - EO) ^ 2
        */


        // Convert the true/false values from correctValues to 0 (false)
        // or 1 (true)
        int[] intCorrectAnswers = new int[correctAnswers.length];

        for (int i = 0; i < correctAnswers.length; i++) {
            intCorrectAnswers[i] = (correctAnswers[i] == true) ? 1 : 0;
        }


        double cost = 0.0;

        for (int i = 0; i < colorValues.length; i++) {

            // cost += (NO - EO) ^ 2
            cost += Math.pow( ( evaluate(colorValues[i]) - intCorrectAnswers[i] ), 2);
        }

        return cost;
    }

    public static void randomize(double min, double max){
        /*
            Give all variables in all nodes a random values
            between min and max
        */

        input_1.randomize(min, max);
        input_2.randomize(min, max);
        input_3.randomize(min, max);

        hidden_1.randomize(min, max);
        hidden_2.randomize(min, max);

        output.randomize(min, max);
    }

}
