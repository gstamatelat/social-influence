package gr.james.influence.algorithms.scoring;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.Graph;
import gr.james.influence.api.algorithms.VertexScoring;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

import java.util.HashSet;

/**
 * This class provides a skeletal implementation of the {@link VertexScoring} interface to minimize the effort required
 * to implement it.
 * <p>
 * This abstract class is intended to be used for algorithms in which each vertex score is calculated independently of
 * other scores, for example degree or closeness. The reason of extending this class instead of
 * {@link AbstractMultiVertexScoring} is for performance reasons, for example if the client only requires to get the
 * scores for a fraction of the population.
 * <p>
 * Instances of this class expect that the graph will not be mutated after the constructor is invoked.
 *
 * @param <V> the vertex type
 * @param <T> the score type
 * @see AbstractMultiVertexScoring
 */
public abstract class AbstractSingleVertexScoring<V, T> implements VertexScoring<V, T> {
    private final Graph<V, ?> g;
    private final GraphState<V, T> scores;

    /**
     * Construct an instance of {@link AbstractSingleVertexScoring} using the specified input {@link Graph} g.
     * <p>
     * The constructor does not perform any calculations and runs in constant time.
     *
     * @param g the input {@link Graph}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public AbstractSingleVertexScoring(@UnmodifiableGraph Graph<V, ?> g) {
        this.g = Conditions.requireNonNull(g);
        this.scores = GraphState.create();
    }

    /**
     * Calculate the score of a single vertex.
     * <p>
     * This method is invoked at most once for each vertex in the graph. The input is guaranteed to be a non-null vertex
     * of the graph.
     *
     * @param v the vertex to get the score of
     * @return the score of vertex {@code v}
     */
    protected abstract T scoreProtected(V v);

    /**
     * {@inheritDoc}
     * <p>
     * This method is buffered.
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public T score(V v) {
        Conditions.requireNonNullAndExists(v, g);
        return scores.computeIfAbsent(v, this::scoreProtected);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is buffered.
     */
    @Override
    public GraphState<V, T> scores() {
        if (scores.size() < g.getVerticesCount()) {
            for (V v : g) {
                scores.putIfAbsent(v, score(v));
            }
        }
        assert scores.keySet().equals(new HashSet<>(g.getVertices()));
        return this.scores;
    }
}
