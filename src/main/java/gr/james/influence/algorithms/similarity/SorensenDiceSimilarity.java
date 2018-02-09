package gr.james.influence.algorithms.similarity;

import com.google.common.collect.Sets;
import gr.james.influence.annotation.ModifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient">Sørensen–Dice
 * coefficient</a>.
 * <p>
 * The Sørensen–Dice coefficient is a statistic used for comparing the similarity of two samples. In this context, the
 * Sørensen–Dice coefficient indicates the similarity between the outbound adjacent vertices of two vertices.
 * <p>
 * The Sørensen–Dice coefficient has a value between 0 and 1. The value can also be {@code NaN} if and only if both
 * input vertices have no outbound edges.
 * <p>
 * The Sørensen–Dice coefficient is commutative, which means that {@code dice(x, y) = dice(y, x)}. Also, by definition,
 * it holds that the similarity of a vertex with itself is 1 (or {@code NaN}).
 * <p>
 * This class has no internal state.
 *
 * @param <V> the vertex type
 */
public class SorensenDiceSimilarity<V> implements VertexSimilarity<V, Double> {
    private final DirectedGraph<V, ?> g;

    /**
     * Construct a {@link SorensenDiceSimilarity} instance from a {@link DirectedGraph}.
     * <p>
     * The constructor doesn't perform any calculations.
     *
     * @param g the {@link DirectedGraph} to construct this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public SorensenDiceSimilarity(@ModifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
    }

    /**
     * Calculates the Sørensen–Dice coefficient between two vertices.
     * <p>
     * This method runs in time proportional to the out degrees of the input vertices and uses constant extra space.
     * This method runs slightly faster when {@code v2} has more outgoing edges than {@code v1}.
     *
     * @param v1 one vertex
     * @param v2 the other vertex
     * @return the Sørensen–Dice coefficient between {@code v1} and {@code v2} or {@link Double#NaN} if undefined
     * @throws NullPointerException   if either {@code v1} or {@code v2} is {@code null}
     * @throws IllegalVertexException if either {@code v1} or {@code v2} is not in the graph
     */
    @Override
    public Double similarity(V v1, V v2) {
        final int intersection = Sets.intersection(g.adjacentOut(v1), g.adjacentOut(v2)).size();
        assert intersection <= g.outDegree(v1) && intersection <= g.outDegree(v2);
        final double dice = (2.0 * intersection) / (g.outDegree(v1) + g.outDegree(v2));
        assert Double.isNaN(dice) || (dice > -1 - 1e-4 && dice < 1 + 1e-4);
        assert Double.isNaN(dice) == Double.isNaN(new JaccardSimilarity<>(g).similarity(v1, v2));
        assert Double.isNaN(dice) || Math.abs(dice - 2 * (new JaccardSimilarity<>(g).similarity(v1, v2)) /
                (1 + new JaccardSimilarity<>(g).similarity(v1, v2))) < 1e-4; // S = 2J/(1+J)
        return dice;
    }
}
