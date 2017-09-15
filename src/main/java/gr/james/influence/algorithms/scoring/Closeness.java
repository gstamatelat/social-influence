package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.distance.Dijkstra;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Direction;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.VertexPair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Closeness {
    private static <V, E> GraphState<V, Double> execute(Graph<V, E> g, ClosenessHandler<V> handler, Collection<V> filter, Direction direction) {
        // TODO: We only need to calculate distances for the filter
        Map<VertexPair<V>, Double> distanceMap = Dijkstra.executeDistanceMap(g);

        GraphState<V, Double> state = GraphState.create();

        for (V v : g) {
            Map<V, Double> dist = new HashMap<>();
            for (VertexPair<V> p : distanceMap.keySet()) {
                V current = (direction.isInbound()) ? p.getTarget() : p.getSource();
                V other = (direction.isInbound()) ? p.getSource() : p.getTarget();
                if (current.equals(v) && !current.equals(other) && filter.contains(other)) {
                    dist.put(other, distanceMap.get(p));
                }
            }
            state.put(v, handler.apply(v, dist));
        }

        return state;
    }

    public static <V, E> GraphState<V, Double> closeness(Graph<V, E> g, Direction direction) {
        return closeness(g, direction, g.getVertices());
    }

    public static <V, E> GraphState<V, Double> closeness(Graph<V, E> g, Direction direction, Collection<V> filter) {
        return execute(
                g,
                (value, d) -> d.size() / d.values().stream().mapToDouble(i -> i).sum(),
                filter,
                direction
        );
    }

    public static <V, E> GraphState<V, Double> closeness(Graph<V, E> g) {
        return closeness(g, Finals.DEFAULT_DIRECTION, g.getVertices());
    }

    public static <V, E> GraphState<V, Double> closeness(Graph<V, E> g, Collection<V> filter) {
        return closeness(g, Finals.DEFAULT_DIRECTION, filter);
    }

    private interface ClosenessHandler<V> {
        double apply(V v, Map<V, Double> distances);
    }
}
