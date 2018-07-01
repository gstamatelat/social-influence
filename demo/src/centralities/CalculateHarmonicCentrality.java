package centralities;

import gr.james.influence.algorithms.scoring.HarmonicCentrality;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;
import simple.CreateDirectedGraph;

/**
 * Demonstration on how to calculate harmonic centrality on a {@link DirectedGraph}.
 */
public final class CalculateHarmonicCentrality {
    public static void main(String[] args) {
        final DirectedGraph<String, Object> g = CreateDirectedGraph.createDirectedGraph();
        System.out.println(g);
        System.out.println(harmonicCentrality(g));
    }

    /**
     * Run harmonic centrality and return the {@link GraphState} result.
     *
     * @param g   the graph
     * @param <V> the vertex type
     * @return the {@link GraphState} result of harmonic centrality
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public static <V> GraphState<V, Double> harmonicCentrality(DirectedGraph<V, ?> g) {
        // Instantiate the HarmonicCentrality
        final HarmonicCentrality<V> harmonic = new HarmonicCentrality<>(g);

        // Run and return the GraphState result
        return harmonic.scores();
    }
}
