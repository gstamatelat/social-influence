package gr.james.socialinfluence.util.states;

import gr.james.socialinfluence.api.AbstractGraphState;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;

import java.util.stream.Collectors;

/**
 * <p>GraphState maps every vertex in a graph with a double value. Useful as a holder for some metric, like PageRank.
 * This class also contains useful methods for statistical analysis of that metric.</p>
 */
public class DoubleGraphState extends AbstractGraphState<Double> {
    public DoubleGraphState() {
    }

    public DoubleGraphState(Graph g, double e) {
        for (Vertex v : g) {
            this.put(v, e);
        }
    }

    @Override
    protected Double add(Double x1, Double x2) {
        return x1 + x2;
    }

    @Override
    protected double add(Double x1, double x2) {
        return x1 + x2;
    }

    @Override
    protected Double subtract(Double x1, Double x2) {
        return x1 - x2;
    }

    @Override
    protected boolean greaterThan(Double x1, Double x2) {
        return x1 > x2;
    }

    @Override
    protected Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    protected Double pow(Double aDouble, int x) {
        return Math.pow(aDouble, x);
    }

    @Override
    public String toString() {
        return "{" + this.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(i -> String.format("%s=%.2f", i.getKey(), i.getValue())).collect(Collectors.joining(", ")) + "}";
    }
}
