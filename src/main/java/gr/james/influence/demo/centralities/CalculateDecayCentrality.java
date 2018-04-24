package gr.james.influence.demo.centralities;

import gr.james.influence.algorithms.scoring.DecayCentrality;
import gr.james.influence.demo.simple.CreateDirectedGraph;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Demonstration on how to calculate decay centrality on a {@link DirectedGraph}.
 */
public final class CalculateDecayCentrality {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        System.out.println(g);
        System.out.println(decayCentrality(g));
    }

    /**
     * Run decay centrality and return the {@link GraphState} result.
     * <p>
     * This method uses a delta parameter equal to {@code 0.5}.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the {@link GraphState} result of decay centrality
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> GraphState<V, Double> decayCentrality(DirectedGraph<V, ?> g) {
        // Instantiate the DecayCentrality
        final DecayCentrality<V> decay = new DecayCentrality<>(g, 0.5);

        // Run and return the GraphState result
        return decay.scores();
    }
}
