package gr.james.socialinfluence.graph.generators;

import gr.james.socialinfluence.graph.MemoryGraph;
import gr.james.socialinfluence.graph.Vertex;

public class Master {
    public static MemoryGraph generate() {
        MemoryGraph g = new MemoryGraph();

        Vertex v1 = g.addVertex();
        Vertex v2 = g.addVertex();
        Vertex v3 = g.addVertex();
        v1.addEdge(v3);
        v1.addEdge(v2);
        v3.addEdge(v2);
        v2.addEdge(v1);

        return g.setName("Master").setMeta("Master");
    }
}