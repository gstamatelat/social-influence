package gr.james.socialinfluence.util.collections.states;

import gr.james.socialinfluence.api.AbstractGraphState;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class IntegerGraphState extends AbstractGraphState<Integer> {
    public IntegerGraphState() {
    }

    public IntegerGraphState(Graph g, int e) {
        for (Vertex v : g.getVertices()) {
            this.put(v, e);
        }
    }

    @Override
    protected Integer add(Integer x1, Integer x2) {
        return x1 + x2;
    }

    @Override
    protected double add(Integer x1, double x2) {
        return x1 + x2;
    }

    @Override
    protected Integer subtract(Integer x1, Integer x2) {
        return x1 - x2;
    }

    @Override
    protected boolean greaterThan(Integer x1, Integer x2) {
        return x1 > x2;
    }

    @Override
    protected Integer abs(Integer x) {
        return Math.abs(x);
    }
}
