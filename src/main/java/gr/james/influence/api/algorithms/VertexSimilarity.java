package gr.james.influence.api.algorithms;

import gr.james.influence.exceptions.InvalidVertexException;

/**
 * Represents an algorithm that calculates the pairwise similarity between vertices of a graph.
 *
 * @param <V> the vertex type
 * @param <T> the result type
 */
@FunctionalInterface
public interface VertexSimilarity<V, T> {
    /**
     * Get the pairwise similarity between two vertices.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return the similarity between {@code v1} and {@code v2}
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws InvalidVertexException if either {@code v1} or {@code v2} is not in the graph referenced by the instance
     */
    T similarity(V v1, V v2);
}
