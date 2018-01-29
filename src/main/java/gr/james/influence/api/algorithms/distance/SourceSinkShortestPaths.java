package gr.james.influence.api.algorithms.distance;

import gr.james.influence.graph.DirectedEdge;

import java.util.List;

/**
 * Represents an algorithm for solving the source to sink shortest path problem.
 *
 * @param <V> the vertex type
 */
public interface SourceSinkShortestPaths<V> {
    /**
     * Get the distance of the shortest path. If there is no such path, this method will return
     * {@link Double#POSITIVE_INFINITY}.
     *
     * @return the distance of the shortest path
     */
    double distance();

    /**
     * Get the shortest path as a sequence of edges. If there is no path or if the source and target associated with
     * this instance are the same this method will return an empty sequence.
     *
     * @return an unmodifiable {@link List} of {@link DirectedEdge} representing the shortest route
     * @throws UnsupportedOperationException if the algorithm does not support this operation
     */
    default List<DirectedEdge<V, ?>> path() {
        throw new UnsupportedOperationException();
    }
}
