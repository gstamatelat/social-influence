package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.helper.Helper;
import gr.james.socialinfluence.graph.Graph;
import gr.james.socialinfluence.graph.Vertex;

public class Wheel {
    public static Graph generate(int totalVertices) {
        Graph g = Path.generate(totalVertices - 1, true);
        Vertex n = g.addVertex();
        for (Vertex v : g.getVertices()) {
            if (!v.equals(n)) {
                g.addEdge(v, n, true);
            }
        }

        return g.setName("Wheel").setMeta(String.format("%s,totalVertices=%d", "Wheel", totalVertices));
    }
}