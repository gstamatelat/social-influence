package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the Jaccard min index.
 * <p>
 * The Jaccard min index is defined to be the intersection of two sets over the minimum cardinality of those. In this
 * context, the Jaccard min index of the given two vertices is defined as the intersection of the outbound adjacent
 * vertices (common neighbors) divided by the minimum outgoing degree of those two vertices.
 * <p>
 * The Jaccard min index has a value between 0 and 1. The value can also be {@code NaN} if and only if either input
 * vertex has no outbound edges.
 * <p>
 * The Jaccard min index is commutative, which means that {@code jaccardMin(x, y) = jaccardMin(y, x)}. Also, by
 * definition, it holds that the similarity of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class JaccardMinSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link JaccardMinSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public JaccardMinSimilarity(DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the Jaccard min index between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v2} has more outgoing edges than {@code v1}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Jaccard min index between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.adjacentOut(v1), g.adjacentOut(v2)).size();
        final int min = Math.min(g.outDegree(v1), g.outDegree(v2));
        assert intersection <= g.outDegree(v1) && intersection <= g.outDegree(v2);
        return (double) intersection / (double) min;
    }
}
