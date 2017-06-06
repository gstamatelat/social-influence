package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.util.Conditions;

import java.util.Optional;

public class Diameter {
    private static final String GRAPH_NULL = "Input graph must not be null";
    private static final String GRAPH_MORE_THAN_ONE = "Input graph must have more than one vertex";

    public static <V, E> double execute(Graph<V, E> g) {
        Conditions.requireNonNull(g, GRAPH_NULL);
        Conditions.requireArgument(g.getVerticesCount() > 1, GRAPH_MORE_THAN_ONE, g.getVerticesCount());

        double max = Double.MIN_VALUE;
        for (V v : g) {
            Optional<Double> ret = Dijkstra.execute(g, v).values().stream().max(Double::compare);
            assert ret.isPresent();
            max = Double.max(max, ret.get());
        }

        assert max > 0;

        return max;
    }
}
