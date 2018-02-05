package gr.james.influence.algorithms.scoring;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Graphs;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

/**
 * Implementation of the degree centrality.
 * <p>
 * The degree of a vertex is the number of edges that are incident to that vertex. Self loops are included.
 * <p>
 * This class is mostly a demonstration of implementing centrality measures and other vertex scoring algorithms using
 * the {@link AbstractSingleVertexScoring} class.
 *
 * @param <V> the vertex type
 */
public class DegreeCentrality<V> extends AbstractSingleVertexScoring<V, Integer> {
    private final DirectedGraph<V, ?> g;
    private final Direction direction;

    /**
     * Construct a {@code DegreeCentrality} instance from a {@code DirectedGraph}.
     * <p>
     * The constructor initializes this instance in constant time.
     *
     * @param g         the {@link DirectedGraph} to construct this instance from
     * @param direction the direction of the degree to calculate (in or out)
     * @throws NullPointerException if either {@code g} or {@code direction} is {@code null}
     */
    public DegreeCentrality(@UnmodifiableGraph DirectedGraph<V, ?> g, Direction direction) {
        super(g);
        this.g = g;
        this.direction = Conditions.requireNonNull(direction);
    }

    /**
     * Convenience function to calculate the degrees of all vertices in a graph.
     * <p>
     * This method will instantiate {@link DegreeCentrality} with the given graph {@code g} and invoke the
     * {@link #scores()} method.
     * <p>
     * This method runs in time {@code O(V)}.
     *
     * @param g         the graph to calculate the degrees from
     * @param direction the direction of the degree to calculate (in or out)
     * @param <V>       the vertex type
     * @return the degrees of the vertices in {@code g}
     * @throws NullPointerException if either {@code g} or {@code direction} is {@code null}
     */
    public static <V> GraphState<V, Integer> execute(DirectedGraph<V, ?> g, Direction direction) {
        return new DegreeCentrality<>(g, direction).scores();
    }

    @Override
    Integer scoreProtected(V v) {
        if (direction.isInbound()) {
            return g.inDegree(v);
        } else {
            return g.outDegree(v);
        }
    }

    /**
     * Get the degree assigned to vertex {@code v}.
     * <p>
     * This method runs in constant time.
     *
     * @param v the vertex to get the degree of
     * @return the degree of vertex {@code v}
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public Integer score(V v) {
        return super.score(v);
    }

    /**
     * Get the degrees of all vertices.
     * <p>
     * The {@link GraphState} returned by this method will have as many entries as there are vertices in the graph. The
     * {@link GraphState} returned by this method may be empty if the graph is empty but can't be {@code null}.
     * <p>
     * This method runs lazily in time {@code O(V)} in the worst case.
     *
     * @return a {@link GraphState} object holding the degrees of all vertices in the graph
     */
    @Override
    public GraphState<V, Integer> scores() {
        final GraphState<V, Integer> scores = super.scores();
        assert scores.values().stream().mapToInt(v -> v).sum() == Graphs.getEdgesCount(g);
        return scores;
    }
}
