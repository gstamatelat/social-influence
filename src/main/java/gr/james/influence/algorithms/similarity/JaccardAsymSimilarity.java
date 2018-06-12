package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the Jaccard asymmetric index.
 * <p>
 * The Jaccard asymmetric index is similar to {@link JaccardSimilarity}. For two sets {@code A} and {@code B} it is
 * defined as the intersection of {@code A} and {@code B} over the cardinality of {@code A}. In this context, the sets
 * {@code A} and {@code B} represent the outbound adjacent vertices of two respective vertices.
 * <p>
 * The Jaccard asymmetric index has a value between 0 and 1. The value can also be {@code NaN} if {@code A} has no
 * outbound edges.
 * <p>
 * The Jaccard asymmetric index is not commutative. Also, by definition, it holds that the similarity of a vertex with
 * itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class JaccardAsymSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link JaccardAsymSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public JaccardAsymSimilarity(DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the Jaccard asymmetric index between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Jaccard asymmetric index between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.adjacentOut(v1), g.adjacentOut(v2)).size();
        assert intersection <= g.outDegree(v1) && intersection <= g.outDegree(v2);
        return (double) intersection / (double) g.outDegree(v1);
    }
}
