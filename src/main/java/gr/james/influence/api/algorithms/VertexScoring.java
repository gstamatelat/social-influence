package gr.james.influence.api.algorithms;

import gr.james.influence.exceptions.IllegalVertexException;
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
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    T score(V v);

    /**
     * Get the scores for all vertices.
     * <p>
     * The {@link GraphState} returned by this method will have as many entries as there are vertices in the graph. The
     * {@link GraphState} returned by this method may be empty if the graph is empty but can't be {@code null}.
     *
     * @return a {@link GraphState} object holding the scores for all vertices in the graph
     */
    GraphState<V, T> scores();
}
