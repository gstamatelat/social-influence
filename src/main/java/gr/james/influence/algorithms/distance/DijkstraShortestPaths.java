package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.algorithms.distance.SingleSourceShortestPaths;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.List;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. This class implements the
 * {@link SingleSourceShortestPaths} interface. Instances of this class expect that the graph will not be mutated after
 * the constructor is invoked.
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
     * @throws InvalidVertexException if {@code source} is not in {@code g}
     */
    public DijkstraShortestPaths(Graph<V, ?> g, V source) {
        alg = new DijkstraClosestFirstIterator<>(g, source);
        alg.exhaust();
    }

    /**
     * {@inheritDoc} This method runs in constant time.
     */
    @Override
    public double distanceTo(V v) {
        return alg.distanceTo(v);
    }

    /**
     * {@inheritDoc} This method runs in time proportional to the shortest path length.
     */
    @Override
    public List<GraphEdge<V, ?>> pathTo(V v) {
        return alg.pathTo(v);
    }
}
