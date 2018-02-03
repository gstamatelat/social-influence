package gr.james.influence.main;

import gr.james.influence.algorithms.generators.random.BarabasiAlbertGenerator;
import gr.james.influence.algorithms.scoring.DegreeCentrality;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Graph;
import gr.james.influence.util.RandomHelper;
import gr.james.influence.util.collections.GraphState;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>Test class to demonstrate that certain generators (mostly the scale-free ones) produce the correct degree
 * distributions. {@link BarabasiAlbertGenerator} produces a
 * network whose degree distribution follows a power law and {@code m} doesn't contribute to the exponent &gamma;.</p>
 */
public class DegreeDistributionTest {
    public static void main(String[] args) {
        /**
         * Performs curve fitting on \sum_{i=1}^{p} c_i k^{e_i} with a brute-force method. Output (the best
         * coefficients) is in the form [c_1, e_1, c_2, e_2, ..., c_p, e_p]. Ideally,
         * [c_1, e_1] = [c_2, e_2] = ... = [c_p, e_p] in order for the graph to have scale-free degree
         * distribution. On this example p = coefficient_count / 2. Press Ctrl+C to terminate.
         */
        Graph<Integer, Object> g = new BarabasiAlbertGenerator<>(10000, 2, 1, 1.2).generate();
        int coefficient_count = 2;
        double speed = 1.0;

        // Find largest degree
        int largestDegree = 0;
        GraphState<Integer, Integer> degrees = DegreeCentrality.execute(g, Direction.INBOUND);
        for (int d : degrees.values()) {
            if (d > largestDegree) {
                largestDegree = d;
            }
        }

        // Get degree distribution
        double[] degreeDistribution = new double[largestDegree];
        for (Map.Entry<Integer, Integer> e : degrees.entrySet()) {
            degreeDistribution[e.getValue() - 1]++;
        }

        // Brute-force curve fitting
        double minDiff = Double.POSITIVE_INFINITY;
        double[] minCoefficients = new double[coefficient_count];
        while (true) {
            double[] coefficients = new double[minCoefficients.length];
            for (int i = 0; i < minCoefficients.length; i++) {
                coefficients[i] = speed * RandomHelper.getRandom().nextGaussian() + minCoefficients[i];
            }
            double diff = 0;
            for (int i = 0; i < largestDegree; i++) {
                double realValue = f(i + 1, coefficients);
                diff += Math.pow(realValue - degreeDistribution[i], 2);
            }
            if (diff < minDiff) {
                minDiff = diff;
                minCoefficients = coefficients;
                System.out.printf("%s - %5.2e\n", Arrays.toString(minCoefficients), minDiff);
            }
        }
    }

    public static double f(double x, double[] coefficients) {
        double realValue = 0;
        for (int j = 0; j < coefficients.length / 2; j++) {
            realValue += coefficients[2 * j] * Math.pow(x, coefficients[2 * j + 1]);
        }
        return realValue;
    }
}
