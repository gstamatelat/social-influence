package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.distance.DijkstraShortestPaths;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.collections.GraphState;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Closeness_centrality">closeness centrality</a>.
 * <p>
 * Closeness centrality is defined to be the reciprocal of farness. The farness of a vertex {@code v} is the sum of the
 * distances from {@code v} to all other vertices in the graph. This class provides the normalized value of closeness
 * centrality, which is defined to be {@code V / farness}.
 * <p>
 * The closeness centrality of a vertex may be zero if the graph is not strongly connected, that is if the distances
 * assume {@link Double#POSITIVE_INFINITY} values.
 * <p>
 * This class uses internal state of size {@code O(V)}.
 *
 * @param <V> the vertex type
 * @see <a href="https://en.wikipedia.org/wiki/Closeness_centrality">Closeness centrality @ Wikipedia</a>
 */
public class ClosenessCentrality<V> extends AbstractSingleVertexScoring<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct an instance of {@link ClosenessCentrality} using the specified input {@link DirectedGraph} g.
     * <p>
     * The constructor does not perform any calculations and runs in constant time.
     *
     * @param g the input {@link DirectedGraph}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public ClosenessCentrality(@UnmodifiableGraph DirectedGraph<V, ?> g) {
        super(g);
        this.g = g;
    }

    @Override
    protected Double scoreProtected(V v) {
        final DijkstraShortestPaths<V> dijkstra = new DijkstraShortestPaths<>(g, v);
        double farness = 0;
        for (V w : g) {
            if (!v.equals(w)) {
                farness += dijkstra.distanceTo(w);
            }
        }
        return g.vertexCount() / farness;
    }

    /**
     * Get the closeness centrality assigned to vertex {@code v}.
     * <p>
     * This method is buffered and runs in time {@code O(E lgV)}.
     *
     * @param v the vertex to get its closeness centrality value
     * @return the closeness centrality of vertex {@code v}
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public Double score(V v) {
        return super.score(v);
    }

    /**
     * Get the closeness centralities of all vertices.
     * <p>
     * The {@link GraphState} returned by this method will have as many entries as there are vertices in the graph. The
     * {@link GraphState} returned by this method may be empty if the graph is empty but can't be {@code null}.
     * <p>
     * This method is buffered and runs in time {@code O(V E lgV)}.
     *
     * @return a {@link GraphState} object holding the closeness centralities of all vertices in the graph
     */
    @Override
    public GraphState<V, Double> scores() {
        return super.scores();
    }
}
