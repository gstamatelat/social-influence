package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.BipartiteGraph;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

import java.util.Set;

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
    private final DirectedGraph<V, ?> directedGraph;
    private final BipartiteGraph<V, ?> bipartiteGraph;

    /**
     * Construct a {@link SimpleMatchingSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public SimpleMatchingSimilarity(DirectedGraph<V, ?> g) {
        this.directedGraph = Conditions.requireNonNull(g);
        this.bipartiteGraph = null;
    }

    /**
     * Construct a {@link SimpleMatchingSimilarity} instance from a {@link BipartiteGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link BipartiteGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public SimpleMatchingSimilarity(BipartiteGraph<V, ?> g) {
        this.directedGraph = null;
        this.bipartiteGraph = Conditions.requireNonNull(g);
    }

    private static <V> double smc(Set<V> s1, Set<V> s2, Set<V> reference) {
        assert reference.containsAll(s1) && reference.containsAll(s2);
        final int intersection = Sets.intersection(s1, s2).size();
        final int union = s1.size() + s2.size() - intersection;
        final int unionComplement = reference.size() - union;
        assert union >= intersection && union <= reference.size();
        assert intersection <= s1.size() && intersection <= s2.size();
        assert union + unionComplement == reference.size();
        assert unionComplement + intersection <= reference.size();
        return (double) (unionComplement + intersection) / (double) reference.size();
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
     * @throws NullPointerException          if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException        if either {@code v1} or {@code v2} is not in the graph
     * @throws UnsupportedOperationException if the graph is bipartite and {@code v1} and {@code v2} are in the same
     *                                       group
     */
    @Override
    public Double similarity(V v1, V v2) {
        assert (directedGraph == null) ^ (bipartiteGraph == null);
        if (directedGraph != null) {
            return smc(
                    directedGraph.adjacentOut(v1),
                    directedGraph.adjacentOut(v2),
                    directedGraph.vertexSet());
        } else {
            if (!bipartiteGraph.setOf(v1).equals(bipartiteGraph.setOf(v2))) {
                throw new UnsupportedOperationException();
            }
            assert bipartiteGraph.otherSetOf(v1).equals(bipartiteGraph.otherSetOf(v2));
            return smc(
                    bipartiteGraph.adjacent(v1),
                    bipartiteGraph.adjacent(v2),
                    bipartiteGraph.otherSetOf(v1));
        }
    }
}
