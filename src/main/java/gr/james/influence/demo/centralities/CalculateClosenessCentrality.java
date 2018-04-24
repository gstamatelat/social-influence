package gr.james.influence.demo.centralities;

import gr.james.influence.algorithms.scoring.ClosenessCentrality;
import gr.james.influence.demo.CreateDirectedGraph;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Demonstration on how to calculate closeness centrality on a {@link DirectedGraph}.
 */
public final class CalculateClosenessCentrality {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        System.out.println(g);
        System.out.println(closenessCentrality(g));
    }

    /**
     * Run closeness centrality and return the {@link GraphState} result.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the {@link GraphState} result of closeness centrality
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> GraphState<V, Double> closenessCentrality(DirectedGraph<V, ?> g) {
        // Instantiate the ClosenessCentrality
        final ClosenessCentrality<V> closeness = new ClosenessCentrality<>(g);

        // Run and return the GraphState result
        return closeness.scores();
    }
}
