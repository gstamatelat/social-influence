package gr.james.influence.api.algorithms.distance;

import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.List;

/**
 * Represents an algorithm that solves the all pairs shortest paths problem.
 *
 * @param <V> the vertex type
 */
public interface AllPairsShortestPaths<V> {
    /**
     * Get the distance of the shortest path from a source to a target vertex. If there is no such path, this method
     * will return {@link Double#POSITIVE_INFINITY}.
     *
     * @param from the source vertex
     * @param to   the target vertex
     * @return the distance of the shortest path from {@code from} to {@code to}
     * @throws NullPointerException   if either {@code from} or {@code to} is null
     * @throws InvalidVertexException if either {@code from} or {@code to} is not in the graph
     */
    double distance(V from, V to);

    /**
     * Get the shortest path from a source to a target vertex as a sequence of edges. If there is no such path or if the
     * {@code from} and {@code to} are the same vertices this method will return an empty sequence.
     *
     * @param from the source vertex
     * @param to   the target vertex
     * @return an unmodifiable {@link List} of {@link GraphEdge} representing the shortest route from {@code from} to
     * {@code to}
     * @throws NullPointerException          if either {@code from} or {@code to} is null
     * @throws InvalidVertexException        if either {@code from} or {@code to} is not in the graph
     * @throws UnsupportedOperationException if the algorithm does not support this operation
     */
    default List<GraphEdge<V, ?>> path(V from, V to) {
        throw new UnsupportedOperationException();
    }
}
