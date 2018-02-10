package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.annotation.ModifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Cosine_similarity">Cosine similarity</a>.
 * <p>
 * Cosine similarity is a measure of similarity between two non-zero vectors of an inner product space that measures the
 * cosine of the angle between them. In this context, the vectors represent the outbound edge weights of two vertices.
 * <p>
 * The Cosine similarity has a value between -1 and 1. The value can also be {@code NaN} if and only if one of the
 * vectors is zero, which can happen if one of the vertices doesn't have any outgoing edges.
 * <p>
 * The Cosine similarity is commutative, which means that {@code cosine(x, y) = cosine(y, x)}. Also, by definition, it
 * holds that the similarity of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class CosineSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link CosineSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public CosineSimilarity(@ModifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the Cosine similarity between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v2} has more outgoing edges than {@code v1}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Cosine similarity between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;
        for (V v : Sets.union(g.adjacentOut(v1), g.adjacentOut(v2))) {
            final double ai = g.getWeightElse(v1, v, 0);
            final double bi = g.getWeightElse(v2, v, 0);
            assert !(ai == 0 && bi == 0);
            numerator += ai * bi;
            denominator1 += ai * ai;
            denominator2 += bi * bi;
        }
        return numerator / Math.sqrt(denominator1 * denominator2);
    }
}
