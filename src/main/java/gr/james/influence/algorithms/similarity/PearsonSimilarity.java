package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the Pearson correlation from the book "Networks: An Introduction", Newman (ch 7.12.2).
 * <p>
 * Pearson correlation is a measure of the linear correlation between the outbound edge weights of two vertices. It has
 * a value between -1 and 1, where 1 is total positive correlation, -1 is total negative correlation and 0 is no
 * correlation. The value of the coefficient is {@code NaN} if and only if the variance of either vertex is zero.
 * <p>
 * Pearson correlation is commutative, which means that {@code pearson(x, y) = pearson(y, x)}. Also, by definition, it
 * holds that the correlation of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class uses internal state of size {@code O(V)}.
 *
 * @param <V> the vertex type
 */
public class PearsonSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;
    private final Map<V, Double> averages;
    private final Map<V, Double> variances;

    /**
     * Construct a {@link PearsonSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor calculates the weight averages and variances of all vertices in time {@code O(V + E)}.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if {@code g} does not contain any vertices
     */
    public PearsonSimilarity(@UnmodifiableGraph DirectedGraph<V, ?> g) {
        Conditions.requireArgument(g.vertexCount() > 0, "Input graph is empty");

        this.g = g;
        this.averages = new HashMap<>();
        this.variances = new HashMap<>();

        for (V v : g) {
            this.averages.put(v, g.outStrength(v) / g.vertexCount());
        }

        for (V v : g) {
            double sum = 0;
            for (DirectedEdge<V, ?> e : g.outEdges(v)) {
                sum += Math.pow(e.weight() - averages.get(v), 2);
            }
            sum += (g.vertexCount() - g.outDegree(v)) * Math.pow(averages.get(v), 2);
            this.variances.put(v, Math.sqrt(sum));
        }
    }

    /**
     * Calculates the Pearson similarity between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v1} has more outgoing edges than {@code v2}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Pearson correlation between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        double sum = 0;
        final Set<V> union = Sets.union(g.adjacentOut(v1), g.adjacentOut(v2));
        int unionSize = 0;
        for (V k : union) {
            final double aik = g.getWeightElse(v1, k, 0);
            final double ajk = g.getWeightElse(v2, k, 0);
            assert !(aik == 0 && ajk == 0);
            sum += (aik - averages.get(v1)) * (ajk - averages.get(v2));
            unionSize++;
        }
        assert unionSize <= g.vertexCount();
        sum += (g.vertexCount() - unionSize) * averages.get(v1) * averages.get(v2);
        final double similarity = sum / (variances.get(v1) * variances.get(v2));
        assert Double.isNaN(similarity) || (similarity > -1 - 1e-4 && similarity < 1 + 1e-4);
        assert Double.isNaN(similarity) == (variance(v1) == 0 || variance(v2) == 0);
        return similarity;
    }

    /**
     * Return the variance (sigma squared) of the outbound edge weights of a vertex.
     * <p>
     * This method runs in constant time.
     *
     * @param v the vertex to return the variance for
     * @return the variance of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    public double variance(V v) {
        Conditions.requireNonNull(v);
        final Double mapping = this.variances.get(v);
        if (mapping == null) {
            throw new IllegalVertexException("Vertex %s is not in the graph", v);
        }
        final double var = Math.pow(mapping, 2) / g.vertexCount();
        assert var >= 0;
        return var;
    }

    /**
     * Return the average of the outbound edge weights of a vertex.
     * <p>
     * This method runs in constant time.
     *
     * @param v the vertex to return the average for
     * @return the average of {@code v}
     * @throws NullPointerException   if {@code v} is {@code null}
     * @throws IllegalVertexException if {@code v} is not in the graph
     */
    public double average(V v) {
        Conditions.requireNonNull(v);
        final Double mapping = this.averages.get(v);
        if (mapping == null) {
            throw new IllegalVertexException("Vertex %s is not in the graph", v);
        }
        assert mapping >= 0;
        return mapping;
    }
}
