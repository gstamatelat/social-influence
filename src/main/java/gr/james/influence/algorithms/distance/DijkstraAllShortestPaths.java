package gr.james.influence.algorithms.distance;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.algorithms.distance.AllPairsShortestPaths;
import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.Conditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Dijkstra's shortest paths algorithm using a binary heap. This class implements the
 * {@link AllPairsShortestPaths} interface.
 *
 * @param <V> the vertex type
 */
public class DijkstraAllShortestPaths<V> implements AllPairsShortestPaths<V> {
    private final Graph<V, ?> g;
    private final Map<V, DijkstraShortestPaths<V>> alg;

    /**
     * Construct an instance of {@link DijkstraAllShortestPaths} with a given {@link Graph}. The constructor runs in
     * constant time.
     *
     * @param g the {@link Graph} in which to perform the algorithm
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public DijkstraAllShortestPaths(@UnmodifiableGraph Graph<V, ?> g) {
        Conditions.requireNonNull(g);
        this.g = g;
        this.alg = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public double distance(V from, V to) {
        Conditions.requireAllNonNull(from, to);
        if (!g.containsVertex(from) || !g.containsVertex(to)) {
            throw new IllegalVertexException();
        }
        if (!alg.containsKey(from)) {
            alg.put(from, new DijkstraShortestPaths<>(g, from));
        }
        return alg.get(from).distanceTo(to);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public List<DirectedEdge<V, ?>> path(V from, V to) {
        Conditions.requireAllNonNull(from, to);
        if (!g.containsVertex(from) || !g.containsVertex(to)) {
            throw new IllegalVertexException();
        }
        if (!alg.containsKey(from)) {
            alg.put(from, new DijkstraShortestPaths<>(g, from));
        }
        return alg.get(from).pathTo(to);
    }
}
