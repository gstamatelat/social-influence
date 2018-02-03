package gr.james.influence.algorithms.scoring;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.algorithms.VertexScoring;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

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
    private final DirectedGraph<V, ?> g;
    private final GraphState<V, T> scores;

    /**
     * Construct an instance of {@link AbstractSingleVertexScoring} using the specified input {@link DirectedGraph} g.
     * <p>
     * The constructor does not perform any calculations and runs in constant time.
     *
     * @param g the input {@link DirectedGraph}
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public AbstractSingleVertexScoring(@UnmodifiableGraph DirectedGraph<V, ?> g) {
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
        Conditions.requireVertexInGraph(g, v);
        return scores.computeIfAbsent(v, this::scoreProtected);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is buffered.
     */
    @Override
    public GraphState<V, T> scores() {
        if (scores.size() < g.vertexCount()) {
            for (V v : g) {
                scores.putIfAbsent(v, score(v));
            }
        }
        assert scores.keySet().equals(g.vertexSet());
        return this.scores;
    }
}
