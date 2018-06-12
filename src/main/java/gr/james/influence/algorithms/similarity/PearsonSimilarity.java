package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.BipartiteGraph;
import gr.james.influence.graph.DirectedEdge;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.UndirectedEdge;
import gr.james.influence.util.Conditions;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;

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
    private final DirectedGraph<V, ?> directedGraph;
    private final BipartiteGraph<V, ?> bipartiteGraph;
    private final int modCount;
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
    public PearsonSimilarity(DirectedGraph<V, ?> g) {
        Conditions.requireArgument(g.vertexCount() > 0, "Input graph is empty");

        this.directedGraph = g;
        this.bipartiteGraph = null;
        this.modCount = g.modCount();
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
     * Construct a {@link PearsonSimilarity} instance from a {@link BipartiteGraph}.
     * <p>
     * The constructor calculates the weight averages and variances of all vertices in time {@code O(V + E)}.
     *
     * @param g the {@link BipartiteGraph} to construct this instance from
     * @throws NullPointerException     if {@code g} is {@code null}
     * @throws IllegalArgumentException if {@code g} does not contain any vertices
     */
    public PearsonSimilarity(BipartiteGraph<V, ?> g) {
        Conditions.requireArgument(g.vertexCount() > 0, "Input graph is empty");

        this.directedGraph = null;
        this.bipartiteGraph = g;
        this.modCount = g.modCount();
        this.averages = new HashMap<>();
        this.variances = new HashMap<>();

        for (V v : g) {
            this.averages.put(v, g.strength(v) / g.otherSetOf(v).size());
        }

        for (V v : g) {
            double sum = 0;
            for (UndirectedEdge<V, ?> e : g.edges(v)) {
                sum += Math.pow(e.weight() - averages.get(v), 2);
            }
            sum += (g.otherSetOf(v).size() - g.degree(v)) * Math.pow(averages.get(v), 2);
            this.variances.put(v, Math.sqrt(sum));
        }
    }

    private static <V> double cov(Set<V> s1, Set<V> s2,
                                  ToDoubleFunction<V> f1, ToDoubleFunction<V> f2,
                                  double avg1, double avg2,
                                  Set<V> reference) {
        assert reference.containsAll(s1) && reference.containsAll(s2);
        double sum = 0;
        int unionSize = 0;
        for (V k : Sets.union(s1, s2)) {
            final double aik = f1.applyAsDouble(k);
            final double ajk = f2.applyAsDouble(k);
            assert !(aik == 0 && ajk == 0);
            sum += (aik - avg1) * (ajk - avg2);
            unionSize++;
        }
        assert unionSize <= reference.size();
        sum += (reference.size() - unionSize) * avg1 * avg2;
        return sum;
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
     * @throws NullPointerException            if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException          if either {@code v1} or {@code v2} is not in the graph
     * @throws UnsupportedOperationException   if the graph is bipartite and {@code v1} and {@code v2} are in the same
     *                                         group
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    @Override
    public Double similarity(V v1, V v2) {
        assert (directedGraph == null) ^ (bipartiteGraph == null);
        if (directedGraph != null) {
            Conditions.requireModCount(this.directedGraph, this.modCount);
            final double similarity = cov(directedGraph.adjacentOut(v1), directedGraph.adjacentOut(v2),
                    v -> directedGraph.getWeightElse(v1, v, 0), v -> directedGraph.getWeightElse(v2, v, 0),
                    averages.get(v1), averages.get(v2),
                    directedGraph.vertexSet()) / (variances.get(v1) * variances.get(v2));
            assert Double.isNaN(similarity) || (similarity > -1 - 1e-4 && similarity < 1 + 1e-4);
            assert Double.isNaN(similarity) == (variance(v1) == 0 || variance(v2) == 0);
            return similarity;
        } else {
            Conditions.requireModCount(this.bipartiteGraph, this.modCount);
            if (!bipartiteGraph.setOf(v1).equals(bipartiteGraph.setOf(v2))) {
                throw new UnsupportedOperationException();
            }
            assert bipartiteGraph.otherSetOf(v1).equals(bipartiteGraph.otherSetOf(v2));
            final double similarity = cov(bipartiteGraph.adjacent(v1), bipartiteGraph.adjacent(v2),
                    v -> bipartiteGraph.getWeightElse(v1, v, 0), v -> bipartiteGraph.getWeightElse(v2, v, 0),
                    averages.get(v1), averages.get(v2),
                    bipartiteGraph.otherSetOf(v1)) / (variances.get(v1) * variances.get(v2));
            assert Double.isNaN(similarity) || (similarity > -1 - 1e-4 && similarity < 1 + 1e-4);
            assert Double.isNaN(similarity) == (variance(v1) == 0 || variance(v2) == 0);
            return similarity;
        }
    }

    /**
     * Return the variance (sigma squared) of the outbound edge weights of a vertex.
     * <p>
     * This method runs in constant time.
     *
     * @param v the vertex to return the variance for
     * @return the variance of {@code v}
     * @throws NullPointerException            if {@code v} is {@code null}
     * @throws IllegalVertexException          if {@code v} is not in the graph
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public double variance(V v) {
        Conditions.requireNonNull(v);
        Conditions.requireModCount(this.directedGraph == null ? this.bipartiteGraph : this.directedGraph, this.modCount);
        assert (directedGraph == null) ^ (bipartiteGraph == null);
        final int count = (directedGraph != null) ? directedGraph.vertexCount() : bipartiteGraph.otherSetOf(v).size();
        final Double mapping = this.variances.get(v);
        if (mapping == null) {
            throw new IllegalVertexException("Vertex %s is not in the graph", v);
        }
        final double var = Math.pow(mapping, 2) / count;
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
     * @throws NullPointerException            if {@code v} is {@code null}
     * @throws IllegalVertexException          if {@code v} is not in the graph
     * @throws ConcurrentModificationException if the graph has been previously modified
     */
    public double average(V v) {
        Conditions.requireNonNull(v);
        Conditions.requireModCount(this.directedGraph == null ? this.bipartiteGraph : this.directedGraph, this.modCount);
        final Double mapping = this.averages.get(v);
        if (mapping == null) {
            throw new IllegalVertexException("Vertex %s is not in the graph", v);
        }
        assert mapping >= 0;
        return mapping;
    }
}
