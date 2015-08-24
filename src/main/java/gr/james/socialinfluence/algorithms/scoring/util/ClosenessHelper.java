package gr.james.socialinfluence.algorithms.scoring.util;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.GraphState;
import gr.james.socialinfluence.util.collections.VertexPair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ClosenessHelper {
    public static GraphState<Double> execute(Graph g, ClosenessHandler handler, Collection<Vertex> filter, boolean in) {
        Map<VertexPair, Double> distanceMap = Dijkstra.executeDistanceMap(g);

        GraphState<Double> state = new GraphState<>();

        for (Vertex v : g) {
            Map<Vertex, Double> dist = new HashMap<>();
            for (VertexPair p : distanceMap.keySet()) {
                Vertex current = in ? p.getTarget() : p.getSource();
                Vertex other = in ? p.getSource() : p.getTarget();
                if (current.equals(v) && !current.equals(other) && filter.contains(other)) {
                    dist.put(other, distanceMap.get(p));
                }
            }
            state.put(v, handler.apply(v, dist));
        }

        return state;
    }
}
