package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.annotation.ModifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Simple_matching_coefficient">Simple matching
 * coefficient</a>.
 * <p>
 * The SMC is similar to the {@link JaccardSimilarity} and is defined as {@code (p + q) / V}, where {@code p} is the
 * intersection of the outbound adjacent vertices and {@code q} is the complement of their union.
 * <p>
 * The SMC has a value between 0 and 1. The value can also be {@code NaN} if and only if the graph is empty.
 * <p>
 * The SMC is commutative, which means that {@code smc(x, y) = smc(y, x)}. Also, by definition, it holds that the
 * similarity of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class SimpleMatchingSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link SimpleMatchingSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public SimpleMatchingSimilarity(@ModifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the SMC between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v2} has more outgoing edges than {@code v1}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the SMC between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.adjacentOut(v1), g.adjacentOut(v2)).size();
        final int union = g.outDegree(v1) + g.outDegree(v2) - intersection;
        final int unionComplement = g.vertexCount() - union;
        assert union >= intersection && union <= g.vertexCount();
        assert intersection <= g.outDegree(v1) && intersection <= g.outDegree(v2);
        assert union + unionComplement == g.vertexCount();
        assert unionComplement + intersection <= g.vertexCount();
        return (double) (unionComplement + intersection) / (double) g.vertexCount();
    }
}
