package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.api.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class Wheel {
    public static <T extends Graph> Graph generate(Class<T> type, int totalVertices) {
        Graph g = new CycleGenerator<>(type, totalVertices - 1).create();
        Vertex n = g.addVertex();
        for (Vertex v : g.getVertices()) {
            if (!v.equals(n)) {
                g.addEdge(v, n, true);
            }
        }

        return g.setMeta("name", "Wheel")
                .setMeta("totalVertices", String.valueOf(totalVertices));
    }
}