package gr.james.influence.api.algorithms;

import gr.james.influence.api.GraphEdge;

import java.util.List;

/**
 * Represents an algorithm that solves the all pairs shortest paths problem.
 *
 * @param <V> the vertex type
 */
public interface AllPairsShortestPaths<V> {
    /**
     * Get the distance of the shortest path from a source to a target vertex.
     *
     * @param from the source vertex
     * @param to   the target vertex
     * @return the distance of the shortest path from {@code from} to {@code to}
     * @throws NullPointerException     if either {@code from} or {@code to} is null
     * @throws IllegalArgumentException if either {@code from} or {@code to} is not in the graph
     */
    double distance(V from, V to);

    /**
     * Get the shortest path from a source to a target vertex as a sequence of edges.
     *
     * @param from the source vertex
     * @param to   the target vertex
     * @return a {@link List} of {@link GraphEdge} representing the shortest route from {@code from} to {@code to}
     * @throws NullPointerException     if either {@code from} or {@code to} is null
     * @throws IllegalArgumentException if either {@code from} or {@code to} is not in the graph
     */
    List<GraphEdge<V, ?>> path(V from, V to);
}
