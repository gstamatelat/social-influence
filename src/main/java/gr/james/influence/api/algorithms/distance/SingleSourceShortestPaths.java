package gr.james.influence.api.algorithms.distance;

import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.exceptions.InvalidVertexException;

import java.util.List;

/**
 * Represents an algorithm that solves the single source shortest paths problem.
 *
 * @param <V> the vertex type
 */
public interface SingleSourceShortestPaths<V> {
    /**
     * Get the distance of the shortest path to a target vertex. If there is no such path, this method will return
     * {@link Double#POSITIVE_INFINITY}.
     *
     * @param v the vertex to get the distance to
     * @return the distance of the shortest path to {@code v}
     * @throws NullPointerException   if {@code v} is null
     * @throws InvalidVertexException if {@code v} is not in the graph
     */
    double distanceTo(V v);

    /**
     * Get the shortest path to a target vertex as a sequence of edges. If there is no path to the target or if the
     * target specified is the source vertex associated with this instance this method will return an empty sequence.
     *
     * @param v the vertex to get the shortest route to
     * @return an unmodifiable {@link List} of {@link GraphEdge} representing the shortest route to {@code v}
     * @throws NullPointerException          if {@code v} is null
     * @throws InvalidVertexException        if {@code v} is not in the graph
     * @throws UnsupportedOperationException if the algorithm does not support this operation
     */
    default List<GraphEdge<V, ?>> pathTo(V v) {
        throw new UnsupportedOperationException();
    }
}