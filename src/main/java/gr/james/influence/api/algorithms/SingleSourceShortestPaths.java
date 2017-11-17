package gr.james.influence.api.algorithms;

import gr.james.influence.api.GraphEdge;

import java.util.List;

/**
 * Represents an algorithm that solves the single source shortest paths problem.
 *
 * @param <V> the vertex type
 */
public interface SingleSourceShortestPaths<V> {
    /**
     * Get the distance of the shortest path to a target vertex.
     *
     * @param v the vertex to get the distance to
     * @return the distance of the shortest path to {@code v}
     * @throws NullPointerException     if {@code v} is null
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    double distanceTo(V v);

    /**
     * Get the shortest path to a target vertex as a sequence of edges.
     *
     * @param v the vertex to get the shortest route to
     * @return a {@link List} of {@link GraphEdge} representing the shortest route to {@code v}
     * @throws NullPointerException     if {@code v} is null
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    List<GraphEdge<V, ?>> pathTo(V v);
}
