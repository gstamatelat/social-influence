package gr.james.influence.algorithms.distance;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.Graph;

import java.util.List;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. This class implements the
 * {@link SingleSourceShortestPaths} interface.
 *
 * @param <V> the vertex type
 */
public class DijkstraShortestPaths<V> implements SingleSourceShortestPaths<V> {
    private final DijkstraClosestFirstIterator<V> alg;

    /**
     * Construct an instance of {@link DijkstraShortestPaths} with a given {@link Graph} and a {@code source}. The
     * constructor will execute the algorithm until all vertices have been marked.
     *
     * @param g      the {@link Graph} in which to perform the algorithm
     * @param source the source vertex
     * @throws NullPointerException   if {@code g} or {@code source} is {@code null}
     * @throws IllegalVertexException if {@code source} is not in {@code g}
     */
    public DijkstraShortestPaths(@UnmodifiableGraph Graph<V, ?> g, V source) {
        alg = new DijkstraClosestFirstIterator<>(g, source);
        alg.exhaust();
    }

    /**
     * {@inheritDoc} This method runs in constant time.
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public double distanceTo(V v) {
        return alg.distanceTo(v);
    }

    /**
     * {@inheritDoc} This method runs in time proportional to the shortest path length.
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public List<DirectedEdge<V, ?>> pathTo(V v) {
        return alg.pathTo(v);
    }
}
