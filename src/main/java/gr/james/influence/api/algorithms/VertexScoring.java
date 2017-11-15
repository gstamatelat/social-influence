package gr.james.influence.api.algorithms;

import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

/**
 * Represents an algorithm that assigns a score to a vertex.
 *
 * @param <V> the vertex type
 * @param <T> the score type
 */
public interface VertexScoring<V, T> {
    /**
     * Get the score assigned to vertex {@code v}.
     *
     * @param v the vertex to get the score of
     * @return the score of vertex {@code v}
     * @throws NullPointerException     if {@code v} is {@code null}
     * @throws IllegalArgumentException if {@code v} is not in the graph
     */
    default T score(V v) {
        Conditions.requireNonNull(v);
        T r = scores().get(v);
        if (r == null) {
            throw new IllegalArgumentException();
        }
        return r;
    }

    /**
     * Get the scores for all vertices.
     *
     * @return a {@link GraphState} object holding the scores for all vertices in the graph
     */
    GraphState<V, T> scores();
}
