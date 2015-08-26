package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.scoring.util.ClosenessHelper;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.collections.GraphState;

import java.util.Collection;

public class Closeness {
    public static GraphState<Double> executeSum(Graph g, boolean in) {
        return executeSum(g, in, g.getVertices());
    }

    public static GraphState<Double> executeSum(Graph g, boolean in, Collection<Vertex> filter) {
        return ClosenessHelper.execute(
                g,
                (value, d) -> d.size() / d.values().stream().mapToDouble(i -> i).sum(),
                filter,
                in
        );
    }
}
