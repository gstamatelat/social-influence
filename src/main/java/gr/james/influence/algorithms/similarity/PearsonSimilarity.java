package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.VertexSimilarity;
import gr.james.influence.util.Conditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the Pearson correlation from the book "Networks: An Introduction", Newman (ch 7.12.2).
 * <p>
 * Pearson correlation is a measure of the linear correlation between the outbound edge weights of two vertices. It has
 * a value between -1 and 1, where 1 is total positive correlation, -1 is total negative correlation and 0 is no
 * correlation. The value of the coefficient is {@code NaN} when the result is undefined, for example when the variance
 * of either vertex is zero.
 * <p>
 * Pearson correlation is commutative, which means that {@code pearson(x, y) = pearson(y, x)}. Also, by definition, it
 * holds that the correlation of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class expects that the graph will not be mutated after the constructor is invoked.
 *
 * @param <V> the type of vertex
 */
public class PearsonSimilarity<V> implements VertexSimilarity<V, Double> {
    private final Graph<V, ?> g;
    private final Map<V, Double> averages;
    private final Map<V, Double> variances;

    /**
     * Construct a {@code PearsonSimilarity} instance from a {@code Graph}. The constructor calculates the weight
     * averages and variances of all vertices in time {@code O(V + E)}.
     *
     * @param g the {@code Graph} to construct this instance from
     */
    public PearsonSimilarity(Graph<V, ?> g) {
        this.g = g;
        this.averages = new HashMap<>();
        this.variances = new HashMap<>();

        for (V v : g) {
            this.averages.put(v, g.getOutStrength(v) / g.getVerticesCount());
        }

        for (V v : g) {
            double sum = 0;
            for (GraphEdge<V, ?> e : g.getOutEdges(v).values()) {
                sum += Math.pow(e.getWeight() - averages.get(v), 2);
            }
            sum += (g.getVerticesCount() - g.getOutEdges(v).size()) * Math.pow(averages.get(v), 2);
            this.variances.put(v, Math.sqrt(sum));
        }
    }

    /**
     * Calculates the Pearson similarity between two vertices. This method runs in time proportional to the out degrees
     * of the input vertices.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Pearson correlation as a {@link Double} or {@link Double#NaN} if undefined
     */
    @Override
    public Double similarity(V v1, V v2) {
        double sum = 0;
        final Set<V> union = Sets.union(g.getOutEdges(v1).keySet(), g.getOutEdges(v2).keySet());
        int unionSize = 0;
        for (V k : union) {
            final double aik = g.getEdgeWeightElse(v1, k, 0);
            final double ajk = g.getEdgeWeightElse(v2, k, 0);
            assert !(aik == 0 && ajk == 0);
            sum += (aik - averages.get(v1)) * (ajk - averages.get(v2));
            unionSize++;
        }
        assert unionSize <= g.getVerticesCount();
        sum += (g.getVerticesCount() - unionSize) * averages.get(v1) * averages.get(v2);
        final double similarity = sum / (variances.get(v1) * variances.get(v2));
        assert Double.isNaN(similarity) || (similarity > -1 - 1e-4 && similarity < 1 + 1e-4);
        return similarity;
    }

    /**
     * Return the variance of the outbound edge weights for a vertex. This method runs in constant time.
     *
     * @param v the vertex to return the variance for
     * @return the variance of {@code v}
     */
    public double variance(V v) {
        Conditions.requireNonNull(v);
        Conditions.requireArgument(this.variances.containsKey(v), "Vertex %s is not in the graph", v);
        return this.variances.get(v);
    }

    /**
     * Return the average of the outbound edge weights for a vertex. This method runs in constant time.
     *
     * @param v the vertex to return the average for
     * @return the average of {@code v}
     */
    public double average(V v) {
        Conditions.requireNonNull(v);
        Conditions.requireArgument(this.averages.containsKey(v), "Vertex %s is not in the graph", v);
        return this.averages.get(v);
    }
}
