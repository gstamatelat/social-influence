package gr.james.influence.algorithms.similarity;

import gr.james.influence.exceptions.IllegalVertexException;

/**
 * Represents an algorithm that calculates the pairwise similarity between vertices of a graph.
 * <p>
 * The documentation of implementations must mention
 * <ul>
 * <li>the possible values of this quantity</li>
 * <li>weather or not the measure is commutative</li>
 * <li>the size of the state (space complexity)</li>
 * </ul>
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
     * @throws NullPointerException          if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException        if either {@code v1} or {@code v2} is not in the graph referenced by the
     *                                       instance
     * @throws UnsupportedOperationException if for some reason the operation is not supported
     */
    T similarity(V v1, V v2);
}
