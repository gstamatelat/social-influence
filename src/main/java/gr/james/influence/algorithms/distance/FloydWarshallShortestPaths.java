package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.api.algorithms.AllPairsShortestPaths;
import gr.james.influence.util.collections.VertexPair;

import java.util.HashMap;
import java.util.Map;

// TODO: Add javadoc: use DijkstraAllShortestPaths instead of this class because it's faster

/**
 * Implementation of the <a href="http://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm">Floyd-Warshall
 * algorithm</a>. The Floyd-Warshall algorithm is an algorithm for finding shortest paths in a weighted graph with
 * positive or negative edge weights (but with no negative cycles). A single execution of the algorithm will find the
 * lengths (sum of edge weights) of the shortest paths between all pairs of vertices, though it does not return details
 * of the paths themselves. The {@link #path(Object, Object)} method will always throw an
 * {@link UnsupportedOperationException}.
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
    public FloydWarshallShortestPaths(Graph<V, ?> g) {
        dist = new HashMap<>();

        for (V u : g) {
            for (V v : g) {
                dist.put(new VertexPair<>(u, v), Double.POSITIVE_INFINITY);
            }
        }

        for (V v : g) {
            for (GraphEdge<V, ?> e : g.getOutEdges(v).values()) {
                dist.put(new VertexPair<>(v, e.getTarget()), e.getWeight());
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
     */
    @Override
    public double distance(V from, V to) {
        Double distance = dist.get(new VertexPair<>(from, to));
        if (distance == null) {
            throw new IllegalArgumentException();
        }
        return distance;
    }
}
