package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

public class Wheel {
    public static MemoryGraph generate(int totalVertices) {
        MemoryGraph g = Path.generate(totalVertices - 1, true);
        Vertex n = g.addVertex();
        for (Vertex v : g.getVertices()) {
            if (!v.equals(n)) {
                g.addEdge(v, n, true);
            }
        }

        return g.setName("Wheel").setMeta(String.format("%s,totalVertices=%d", "Wheel", totalVertices));
    }
}