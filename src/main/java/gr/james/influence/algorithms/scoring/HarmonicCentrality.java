package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.distance.DijkstraShortestPaths;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Closeness_centrality#In_disconnected_graphs">harmonic
 * centrality</a>.
 * <p>
 * The harmonic centrality is similar to the {@link ClosenessCentrality closeness centrality}. The harmonic centrality
 * of vertex {@code v} is defined to be the sum of the reciprocals of the distances from {@code v} to all other vertices
 * in the graph.
 * <p>
 * This class is suitable for use in graphs that are not strongly connected.
 * <p>
 * This class uses internal state of size {@code O(V)}.
 *
 * @param <V> the vertex type
 * @see <a href="https://en.wikipedia.org/wiki/Closeness_centrality#In_disconnected_graphs">Harmonic centrality @
 * Wikipedia</a>
 */
public class HarmonicCentrality<V> extends AbstractSingleVertexScoring<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct an instance of {@link HarmonicCentrality} using the specified input {@link DirectedGraph} g.
     * <p>
     * The constructor does not perform any calculations and runs in constant time.
     *
     * @param g the input {@link DirectedGraph}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public HarmonicCentrality(@UnmodifiableGraph DirectedGraph<V, ?> g) {
        super(g);
        this.g = g;
    }

    @Override
    Double scoreProtected(V v) {
        final DijkstraShortestPaths<V> dijkstra = new DijkstraShortestPaths<>(g, v);
        double sum = 0;
        for (V w : g) {
            if (!v.equals(w)) {
                sum += 1 / dijkstra.distanceTo(w);
            }
        }
        return sum;
    }

    /**
     * Get the harmonic centrality assigned to vertex {@code v}.
     * <p>
     * This method is buffered and runs in time {@code O(E lgV)}.
     *
     * @param v the vertex to get its harmonic centrality value
     * @return the harmonic centrality of vertex {@code v}
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public Double score(V v) {
        return super.score(v);
    }

    /**
     * Get the harmonic centralities of all vertices.
     * <p>
     * The {@link GraphState} returned by this method will have as many entries as there are vertices in the graph. The
     * {@link GraphState} returned by this method may be empty if the graph is empty but can't be {@code null}.
     * <p>
     * This method is buffered and runs in time {@code O(V E lgV)}.
     *
     * @return a {@link GraphState} object holding the harmonic centralities of all vertices in the graph
     */
    @Override
    public GraphState<V, Double> scores() {
        return super.scores();
    }
}
