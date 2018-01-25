package gr.james.influence.algorithms.distance;

import gr.james.influence.annotation.UnmodifiableGraph;
import gr.james.influence.api.algorithms.distance.AllPairsShortestPaths;
import gr.james.influence.api.graph.DirectedEdge;
import gr.james.influence.api.graph.Graph;
import gr.james.influence.exceptions.IllegalVertexException;
import gr.james.influence.util.collections.VertexPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the <a href="http://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm">Floyd-Warshall
 * algorithm</a>.
 * <p>
 * The Floyd-Warshall algorithm is an algorithm for finding shortest paths in a weighted graph with positive or negative
 * edge weights (but with no negative cycles). A single execution of the algorithm will find the lengths (sum of edge
 * weights) of the shortest paths between all pairs of vertices, though it does not return details of the paths
 * themselves. The {@link #path(Object, Object)} method will always throw an {@link UnsupportedOperationException}.
 * <p>
 * You should use {@link DijkstraAllShortestPaths} instead of this class. {@link DijkstraAllShortestPaths} is a drop-in
 * replacement of {@code FloydWarshallShortestPaths} but faster in most cases.
 */
public class FloydWarshallShortestPaths<V> implements AllPairsShortestPaths<V> {
    private final Map<VertexPair<V>, Double> dist;

    /**
     * Construct an instance of this class from a given graph. The constructor will execute the Floyd-Warshall algorithm
     * and store the result internally. You can query this result using the {@link #distance(Object, Object)} method.
     *
     * @param g the graph to create this instance from
     * @throws NullPointerException if {@code g} is {@code null}
     */
    public FloydWarshallShortestPaths(@UnmodifiableGraph Graph<V, ?> g) {
        dist = new HashMap<>();

        for (V u : g) {
            for (V v : g) {
                dist.put(new VertexPair<>(u, v), Double.POSITIVE_INFINITY);
            }
        }

        for (V v : g) {
            for (DirectedEdge<V, ?> e : g.getOutEdges(v).values()) {
                dist.put(new VertexPair<>(v, e.target()), e.weight());
            }
        }

        for (V v : g) {
            dist.put(new VertexPair<>(v, v), 0.0);
        }

        for (V k : g) {
            for (V i : g) {
                for (V j : g) {
                    if (dist.get(new VertexPair<>(i, j)) > dist.get(new VertexPair<>(i, k)) + dist.get(new VertexPair<>(k, j))) {
                        dist.put(new VertexPair<>(i, j), dist.get(new VertexPair<>(i, k)) + dist.get(new VertexPair<>(k, j)));
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc} This method runs in constant time.
     *
     * @throws NullPointerException   {@inheritDoc}
     * @throws IllegalVertexException {@inheritDoc}
     */
    @Override
    public double distance(V from, V to) {
        final Double distance = dist.get(new VertexPair<>(from, to));
        if (distance == null) {
            throw new IllegalVertexException();
        }
        return distance;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The Floyd-Warshall algorithm does not support paths, thus this method will always throw
     * {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public List<DirectedEdge<V, ?>> path(V from, V to) {
        throw new UnsupportedOperationException("FloydWarshallShortestPaths doesn't support the path method");
    }
}
