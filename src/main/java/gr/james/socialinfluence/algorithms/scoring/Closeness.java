package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.algorithms.scoring.util.ClosenessHelper;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.GraphState;

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
