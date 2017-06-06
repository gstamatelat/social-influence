package gr.james.influence.algorithms.distance;

import gr.james.influence.api.Graph;
import gr.james.influence.util.Conditions;
import gr.james.influence.util.collections.GraphState;

import java.util.*;

public class Diameter {
    private static final String GRAPH_NULL = "Input graph must not be null";
    private static final String GRAPH_MORE_THAN_ONE = "Input graph must have more than one vertex";

    public static <V, E> double execute(Graph<V, E> g) {
        Conditions.requireNonNull(g, GRAPH_NULL);
        Conditions.requireArgument(g.getVerticesCount() > 1, GRAPH_MORE_THAN_ONE, g.getVerticesCount());

        double max = Double.MIN_VALUE;
        for (V v : g) {
            Optional<Double> ret = Dijkstra.execute(g, v).values().stream().max(Double::compare);
            assert ret.isPresent();
            max = Double.max(max, ret.get());
        }

        assert max > 0;

        return max;
    }

    public static <V, E> int executeUnweighted(Graph<V, E> g) {
        Conditions.requireNonNull(g, GRAPH_NULL);
        Conditions.requireArgument(g.getVerticesCount() > 1, GRAPH_MORE_THAN_ONE, g.getVerticesCount());

        int max = 0;

        for (V v : g) {
            Queue<V> bfs = new LinkedList<>();
            Set<V> visited = new HashSet<>();
            GraphState<V, Integer> distances = new GraphState<>(g, -1);
            bfs.add(v);
            distances.put(v, 0);
            visited.add(v);
            while (!bfs.isEmpty()) {
                V w = bfs.remove();
                g.getOutEdges(w).keySet().stream().filter(v1 -> !visited.contains(v1)).forEach(f -> {
                    bfs.add(f);
                    distances.put(f, distances.get(w) + 1);
                    boolean inserted = visited.add(f);
                    assert inserted;
                });
                max = Integer.max(max, distances.get(w));
            }
            if (visited.size() != g.getVerticesCount()) {
                return -1;
            }
        }

        assert max > 0 && max < g.getVerticesCount();

        return max;
    }
}
