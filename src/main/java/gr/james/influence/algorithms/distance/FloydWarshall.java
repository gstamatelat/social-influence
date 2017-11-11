package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.api.GraphEdge;
import gr.james.influence.util.collections.VertexPair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Implementation of the <a href="http://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm">Floyd-Warshall
 * algorithm</a>. In computer science, the Floyd-Warshall algorithm is an algorithm for finding shortest paths in a
 * weighted graph with positive or negative edge weights (but with no negative cycles). A single execution of the
 * algorithm will find the lengths (summed weights) of the shortest paths between all pairs of vertices, though it does
 * not return details of the paths themselves.</p>
 * <p>You should use {@link Dijkstra#executeDistanceMap(Graph)}, which is faster.</p>
 */
public class FloydWarshall {
    public static <V, E> Map<VertexPair<V>, Double> execute(Graph<V, E> g) {
        Map<VertexPair<V>, Double> dist = new HashMap<>();

        for (V u : g) {
            for (V v : g) {
                if (u.equals(v)) {
                    dist.put(new VertexPair<>(u, v), 0.0);
                } else {
                    dist.put(new VertexPair<>(u, v), Double.POSITIVE_INFINITY);
                }
            }
        }

        for (V v : g) {
            for (Map.Entry<V, GraphEdge<V, E>> e : g.getOutEdges(v).entrySet()) {
                dist.put(new VertexPair<>(v, e.getKey()), e.getValue().getWeight());
            }
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

        return Collections.unmodifiableMap(dist);
    }
}
