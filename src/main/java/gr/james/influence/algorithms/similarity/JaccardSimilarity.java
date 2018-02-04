package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.annotation.ModifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard index</a>.
 * <p>
 * The Jaccard index, also known as "Intersection over Union" and the "Jaccard similarity coefficient", is a statistic
 * used for comparing the similarity and diversity of sample sets. It measures similarity between finite sample sets,
 * and is defined as the size of the intersection divided by the size of the union of the sample sets. In this context,
 * the Jaccard index of the given two vertices is defined as the intersection of the outbound adjacent vertices (common
 * neighbors) divided by the union.
 * <p>
 * The Jaccard index has a value between 0 and 1. The value can also be {@code NaN} if and only if both input vertices
 * have no outbound edges.
 * <p>
 * The Jaccard index is commutative, which means that {@code jaccard(x, y) = jaccard(y, x)}. Also, by definition, it
 * holds that the similarity of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class JaccardSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link JaccardSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public JaccardSimilarity(@ModifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the Jaccard index between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v2} has more outgoing edges than {@code v1}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Jaccard index between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.adjacentOut(v1), g.adjacentOut(v2)).size();
        final int union = g.outDegree(v1) + g.outDegree(v2) - intersection;
        assert union >= intersection && union <= g.vertexCount();
        assert intersection <= g.outDegree(v1) && intersection <= g.outDegree(v2);
        return (double) intersection / (double) union;
    }
}
