package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.algorithms.distance.SourceSinkShortestPaths;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.List;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. This class implements the
 * {@link SourceSinkShortestPaths} interface. Instances of this class expect that the graph will not be mutated after
 * the constructor is invoked.
 *
 * @param <V> the vertex type
 */
public class DijkstraOneShortestPath<V> implements SourceSinkShortestPaths<V> {
    private final DijkstraClosestFirstIterator<V> alg;
    private final V to;

    /**
     * Construct an instance of {@link DijkstraOneShortestPath} with a given {@link Graph}, a {@code source} and a
     * {@code target}. The constructor will execute the algorithm until the {@code target} vertex has been marked and
     * will then stop.
     *
     * @param g      the {@link Graph} in which to perform the algorithm
     * @param source the source vertex
     * @param target the target vertex
     * @throws NullPointerException   if {@code g} or {@code source} or {@code target} is {@code null}
     * @throws InvalidVertexException if {@code source} or {@code target} is not in {@code g}
     */
    public DijkstraOneShortestPath(Graph<V, ?> g, V source, V target) {
        alg = new DijkstraClosestFirstIterator<>(g, source);
        alg.exhaust(target);
        to = target;
    }

    /**
     * {@inheritDoc} This method runs in constant time.
     */
    @Override
    public double distance() {
        return alg.distanceTo(to);
    }

    /**
     * {@inheritDoc} This method runs in time proportional to the shortest path length.
     */
    @Override
    public List<GraphEdge<V, ?>> path() {
        return alg.pathTo(to);
    }
}
