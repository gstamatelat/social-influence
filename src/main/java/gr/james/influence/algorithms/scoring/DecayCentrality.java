package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.distance.DijkstraShortestPaths;
import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.graph.DirectedGraph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

/**
 * Implementation of the decay centrality from "Social and Economic Networks", ch. 2.2.4.
 * <p>
 * The decay centrality is similar to the {@link ClosenessCentrality closeness centrality}. The decay centrality of
 * vertex {@code v} is defined to be the sum of the powers of the distances from {@code v} to all other vertices in
 * the graph.
 * <p>
 * This class is suitable for use in graphs that are not strongly connected.
 * <p>
 * This class uses internal state of size {@code O(V)}.
 *
 * @param <V> the vertex type
 * @see "Matthew Jackson. Social and economic networks. Princeton university press, 2010. Chapter 2.2.4."
 */
public class DecayCentrality<V> extends AbstractSingleVertexScoring<V, Double> {
    private final DirectedGraph<V, ?> g;
    private final double decay;

    /**
     * Construct an instance of {@link DecayCentrality} using the specified input {@link DirectedGraph} g.
     * <p>
     * The constructor does not perform any calculations and runs in constant time.
     *
     * @param g     the input {@link DirectedGraph}
     * @param decay the decay value in (0,1)
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public DecayCentrality(@UnmodifiableGraph DirectedGraph<V, ?> g, double decay) {
        super(g);
        Conditions.requireArgument(decay > 0 && decay < 1, "decay argument must be in (0,1)");
        this.g = g;
        this.decay = decay;
    }

    @Override
    Double scoreProtected(V v) {
        final DijkstraShortestPaths<V> dijkstra = new DijkstraShortestPaths<>(g, v);
        double sum = 0;
        for (V w : g) {
            if (!v.equals(w)) {
                sum += Math.pow(decay, dijkstra.distanceTo(w));
            }
        }
        return sum;
    }

    /**
     * Get the decay centrality assigned to vertex {@code v}.
     * <p>
     * This method is buffered and runs in time {@code O(E lgV)}.
     *
     * @param v the vertex to get its decay centrality value
     * @return the decay centrality of vertex {@code v}
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public Double score(V v) {
        return super.score(v);
    }

    /**
     * Get the decay centralities of all vertices.
     * <p>
     * The {@link GraphState} returned by this method will have as many entries as there are vertices in the graph. The
     * {@link GraphState} returned by this method may be empty if the graph is empty but can't be {@code null}.
     * <p>
     * This method is buffered and runs in time {@code O(V E lgV)}.
     *
     * @return a {@link GraphState} object holding the decay centralities of all vertices in the graph
     */
    @Override
    public GraphState<V, Double> scores() {
        return super.scores();
    }
}
