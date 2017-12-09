package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.api.Graph;
import gr.james.influence.api.algorithms.VertexSimilarity;
import gr.james.influence.exceptions.IllegalVertexException;
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
 * <p>
 * The graph bound to an instance of this class may be mutated freely after the constructor is invoked. Methods will
 * reflect the changes to the underlying graph.
 *
 * @param <V> the vertex type
 */
public class JaccardSimilarity<V> implements VertexSimilarity<V, Double> {
    private final Graph<V, ?> g;

    /**
     * Construct a {@code JaccardSimilarity} instance from a {@link Graph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link Graph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public JaccardSimilarity(Graph<V, ?> g) {
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
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph referenced by the instance
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.getOutEdges(v1).keySet(), g.getOutEdges(v2).keySet()).size();
        final int union = g.getOutDegree(v1) + g.getOutDegree(v2) - intersection;
        assert union >= intersection && union <= g.getVerticesCount();
        assert intersection <= g.getOutDegree(v1) && intersection <= g.getOutDegree(v2);
        return (double) intersection / (double) union;
    }
}
