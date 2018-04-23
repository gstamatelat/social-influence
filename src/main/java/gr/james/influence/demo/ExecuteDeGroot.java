package gr.james.influence.demo;

import gr.james.influence.algorithms.scoring.DeGroot;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Demonstration on how to execute the DeGroot algorithm on a {@link DirectedGraph}.
 */
public final class ExecuteDeGroot {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        System.out.println(g);
        System.out.println(executeDeGroot(g));
    }

    /**
     * Run DeGroot and return the {@link GraphState} result.
     * <p>
     * This method uses an initial belief vector of {@code 0.5}. Thus, the limiting belief result should also be a
     * vector of {@code 0.5}.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the {@link GraphState} result of DeGroot
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> GraphState<V, Double> executeDeGroot(DirectedGraph<V, ?> g) {
        // Specify the initial opinion vector, which in this case is a vector of 0.5
        final GraphState<V, Double> initialVector = GraphState.create(g.vertexSet(), 0.5);

        // Instantiate the DeGroot algorithm
        final DeGroot<V> deGroot = new DeGroot<>(g, initialVector, -1);

        // Run and return the GraphState result
        return deGroot.run();
    }
}
