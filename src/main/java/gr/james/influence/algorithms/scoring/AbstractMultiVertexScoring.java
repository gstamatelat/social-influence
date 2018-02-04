package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.VertexScoring;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

/**
 * This class provides a skeletal implementation of the {@link VertexScoring} interface to minimize the effort required
 * to implement it.
 * <p>
 * This abstract class is intended to be used for algorithms in which the score of a vertex depends on the scores of all
 * other vertices, for example PageRank and HITS.
 * <p>
 * Instances of this class expect that the graph will not be mutated after the constructor is invoked.
 *
 * @param <V> the vertex type
 * @param <T> the score type
 * @see AbstractSingleVertexScoring
 */
public abstract class AbstractMultiVertexScoring<V, T> implements VertexScoring<V, T> {
    private final DirectedGraph<V, ?> g;
    private final GraphState<V, T> scores;

    /**
     * Construct an instance of {@link AbstractMultiVertexScoring} using the specified input {@link DirectedGraph} g.
     * <p>
     * The constructor invokes the method {@link #scoresProtected()}.
     *
     * @param g the input {@link DirectedGraph}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public AbstractMultiVertexScoring(@UnmodifiableGraph DirectedGraph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
        this.scores = scoresProtected();
        assert scores.keySet().equals(g.vertexSet());
    }

    /**
     * Calculate the scores of all vertices.
     * <p>
     * This method is invoked only once during the constructor.
     *
     * @return a {@link GraphState} object holding the scores for all vertices in the graph
     */
    protected abstract GraphState<V, T> scoresProtected();

    /**
     * {@inheritDoc}
     * <p>
     * This method runs in constant time.
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public T score(V v) {
        Conditions.requireNonNull(v);
        final T r = scores.get(v);
        if (r == null) {
            throw new IllegalVertexException();
        }
        return r;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method runs in constant time.
     */
    @Override
    public GraphState<V, T> scores() {
        return this.scores;
    }
}
