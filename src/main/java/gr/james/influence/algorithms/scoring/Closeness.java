package gr.james.influence.algorithms.scoring;

import gr.james.influence.algorithms.distance.Dijkstra;
import gr.james.influence.api.Graph;
import gr.james.influence.graph.Direction;
import gr.james.influence.graph.Vertex;
import gr.james.influence.util.Finals;
import gr.james.influence.util.collections.GraphState;
import gr.james.influence.util.collections.VertexPair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Closeness {
    private static GraphState<Double> execute(Graph g, ClosenessHandler handler, Collection<Vertex> filter, Direction direction) {
        // TODO: We only need to calculate distances for the filter
        Map<VertexPair, Double> distanceMap = Dijkstra.executeDistanceMap(g);

        GraphState<Double> state = new GraphState<>();

        for (Vertex v : g) {
            Map<Vertex, Double> dist = new HashMap<>();
            for (VertexPair p : distanceMap.keySet()) {
                Vertex current = (direction.isInbound()) ? p.getTarget() : p.getSource();
                Vertex other = (direction.isInbound()) ? p.getSource() : p.getTarget();
                if (current.equals(v) && !current.equals(other) && filter.contains(other)) {
                    dist.put(other, distanceMap.get(p));
                }
            }
            state.put(v, handler.apply(v, dist));
        }

        return state;
    }

    public static GraphState<Double> closeness(Graph g, Direction direction) {
        return closeness(g, direction, g.getVertices());
    }

    public static GraphState<Double> closeness(Graph g, Direction direction, Collection<Vertex> filter) {
        return execute(
                g,
                (value, d) -> d.size() / d.values().stream().mapToDouble(i -> i).sum(),
                filter,
                direction
        );
    }

    public static GraphState<Double> closeness(Graph g) {
        return closeness(g, Finals.DEFAULT_DIRECTION, g.getVertices());
    }

    public static GraphState<Double> closeness(Graph g, Collection<Vertex> filter) {
        return closeness(g, Finals.DEFAULT_DIRECTION, filter);
    }

    private interface ClosenessHandler {
        double apply(Vertex v, Map<Vertex, Double> distances);
    }
}
