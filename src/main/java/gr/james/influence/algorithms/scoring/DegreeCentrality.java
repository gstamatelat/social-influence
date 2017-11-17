package gr.james.influence.algorithms.scoring;

import gr.james.influence.api.Graph;
import gr.james.influence.api.algorithms.VertexScoring;
import gr.james.influence.graph.Direction;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

import java.util.HashSet;

/**
 * Implementation of the degree centrality.
 * <p>
 * The degree of a vertex is the number of edges that are incident to that vertex. Self loops are included.
 * <p>
 * Instances of this class expect that the graph will not be mutated after the constructor is invoked.
 * <p>
 * This class is mostly a boilerplate for implementing centrality measures and other vertex scoring algorithms.
 *
 * @param <V> the vertex type
 */
public class DegreeCentrality<V> implements VertexScoring<V, Integer> {
    private final GraphState<V, Integer> scores;

    /**
     * Construct a {@code DegreeCentrality} instance from a {@code Graph}. The constructor calculates the degrees of all vertices
     * in time {@code O(V)}.
     *
     * @param g         the {@link Graph} to construct this instance from
     * @param direction the direction of the degree to calculate (in or out)
     * @throws NullPointerException if either {@code g} or {@code direction} is {@code null}
     */
    public DegreeCentrality(Graph<V, ?> g, Direction direction) {
        Conditions.requireAllNonNull(g, direction);

        scores = GraphState.create();

        if (direction.isOutbound()) {
            for (V v : g) {
                scores.put(v, g.getOutDegree(v));
            }
        } else {
            for (V v : g) {
                scores.put(v, g.getInDegree(v));
            }
        }

        assert scores.keySet().equals(new HashSet<>(g.getVertices()));
        assert scores.values().stream().mapToInt(v -> v).sum() == g.getEdgesCount();
    }

    /**
     * Convenience function to calculate the degrees of all vertices in a graph. This method will instantiate
     * {@link DegreeCentrality} with the given graph {@code g} and invoke the {@link #scores()} method.
     *
     * @param g         the graph to calculate the degrees from
     * @param direction the direction of the degree to calculate (in or out)
     * @param <V>       the vertex type
     * @return the degrees of the vertices in {@code g}
     * @throws NullPointerException if either {@code g} or {@code direction} is {@code null}
     */
    public static <V> GraphState<V, Integer> execute(Graph<V, ?> g, Direction direction) {
        return new DegreeCentrality<>(g, direction).scores();
    }

    /**
     * Get the degrees of all vertices. This method runs in constant time.
     *
     * @return a {@link GraphState} object holding the degrees for all vertices in the graph
     */
    @Override
    public GraphState<V, Integer> scores() {
        return scores;
    }
}
