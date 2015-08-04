package gr.james.socialinfluence.util.states;

import gr.james.socialinfluence.api.AbstractGraphState;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;

/**
 * <p>GraphState maps every vertex in a graph with a double value. Useful as a holder for some metric, like PageRank.
 * This class also contains useful methods for statistical analysis of that metric.</p>
 */
public class DoubleGraphState extends AbstractGraphState<Double> {
    public DoubleGraphState() {
    }

    public DoubleGraphState(Graph g, double e) {
        for (Vertex v : g.getVerticesAsList()) {
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
}
