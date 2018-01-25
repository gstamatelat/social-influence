package gr.james.influence.api.algorithms.distance;

import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.exceptions.IllegalVertexException;

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
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    double distanceTo(V v);

    /**
     * Get the shortest path to a target vertex as a sequence of edges. If there is no path to the target or if the
     * target specified is the source vertex associated with this instance this method will return an empty sequence.
     *
     * @param v the vertex to get the shortest route to
     * @return an unmodifiable {@link List} of {@link DirectedEdge} representing the shortest route to {@code v}
     * @throws NullPointerException          if {@code v} is null
     * @throws IllegalVertexException        if {@code v} is not in the graph
     * @throws UnsupportedOperationException if the algorithm does not support this operation
     */
    default List<DirectedEdge<V, ?>> pathTo(V v) {
        throw new UnsupportedOperationException();
    }
}
