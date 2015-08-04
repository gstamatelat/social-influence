package gr.james.socialinfluence.algorithms.scoring;

import gr.james.socialinfluence.algorithms.distance.Dijkstra;
import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.api.GraphState;
import gr.james.socialinfluence.graph.Vertex;
import gr.james.socialinfluence.util.collections.VertexPair;
import gr.james.socialinfluence.util.states.DoubleGraphState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Closeness centrality as defined in <a href="https://en.wikipedia.org/wiki/Centrality#Closeness_centrality">
 * https://en.wikipedia.org/wiki/Centrality#Closeness_centrality</a>.</p>
 */
public class Closeness {
    public static GraphState<Double> execute(Graph g, boolean in, Collection<Vertex> includeOnly, ClosenessHandler handler) {
        Map<VertexPair, Double> distanceMap = Dijkstra.executeDistanceMap(g);

        GraphState<Double> state = new DoubleGraphState();

        for (Vertex v : g.getVerticesAsList()) {
            Map<Vertex, Double> dist = new HashMap<>();
            for (VertexPair p : distanceMap.keySet()) {
                Vertex current = in ? p.getTarget() : p.getSource();
                Vertex other = in ? p.getSource() : p.getTarget();
                if (current.equals(v) && !current.equals(other) && includeOnly.contains(other)) {
                    dist.put(other, distanceMap.get(p));
                }
            }
            state.put(v, handler.handleVertex(v, dist));
        }

        return state;
    }

    public interface ClosenessHandler {
        double handleVertex(Vertex v, Map<Vertex, Double> distanceMap);
    }

    public static class ClosenessSumHandler implements ClosenessHandler {
        @Override
        public double handleVertex(Vertex v, Map<Vertex, Double> distanceMap) {
            double sum = 0;
            for (Vertex u : distanceMap.keySet()) {
                sum += distanceMap.get(u);
            }
            return distanceMap.size() / sum;
        }
    }
}
